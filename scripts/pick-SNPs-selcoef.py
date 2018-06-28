import sys
import collections
import math
import re
import random
import argparse
import gzip

class SelectedCandidate:
	def __init__(self,chr,pos,anc,der):
		self.chr=chr
		self.pos=pos
		self.anc=anc
		self.der=der
		

class SelCoefProviderDefault:
	def __init__(self,selcoef,het):
		self.selcoef=selcoef
		self.het=het
		
	def getSH(self):
		return ((self.selcoef,self.h))


class SelCoefProviderFile:
	def __init__(self,file):
		scl=[]
		for l in open(file):
			a=l.rstrip("\t").split(" ")
			if(len(a)!=2):
				raise Exception("Wrong format of selection coefficients; must have two columns; space separated")
			scl.append(a)
		self.scl=scl
	
	def getSH(self):
		# random choice is with replacment
		return random.choice(self.scl)

def parse_line(line):
	"""
	2L      686891    G      G/A    AG GG GG GG GG
	2L      936681    A      A/G    GG AA AA AA AA
	2L      861026    T      A/T    TT AT AA AA TT
	"""
	a=line.split("\t")
	chr=a[0]
	pos=a[1]
	anc,der=a[3].split("/")
	b=a[4].split(" ")
	nucs=[]
	for i in b:
		nucs.extend(list(i))
	mincount=0
	totcount=0
	for n in nucs:
		totcount+=1
		if(n==der):
			mincount+=1

	minfreq=float(mincount)/float(totcount)
	if((1.0-minfreq)<minfreq):
		minfreq=1.0-minfreq
	
	return (chr,pos,anc,der,minfreq)


parser = argparse.ArgumentParser(description="""           
Description
-----------
    Generate haplotypes suitable for MimicrEE2""",formatter_class=argparse.RawDescriptionHelpFormatter,
epilog="""
Prerequisites
-------------
    python version 3+

Authors
-------
    Robert Kofler 
""")

parser.add_argument("--mimhap", type=str, required=True, dest="mimhap", default=None, help="the name of the chromosomes")
parser.add_argument("--effect-file", type=str, required=False, dest="effectfile", default=None, help="a file with effect sizes; optional")
parser.add_argument("--n", type=int, required=True, dest="numsel", default=None, help="the number of SNPs to pick")
parser.add_argument("--e", type=float, required=False, dest="het", default=None, help="the heterozygous effect of the SNPs")
parser.add_argument("--s", type=float, required=False, dest="selcoef", default=None, help="the heterozygous effect of the SNPs")
parser.add_argument("--f", type=float, required=False, dest="minfreq", default=None, help="the heterozygous effect of the SNPs")


args = parser.parse_args()

provider=None
if(args.effectfile is not None):
	provider=SelCoefProviderDefault(args.effectfile)
else:
	provider=SelCoefProviderDefault(args.selcoef,args.het)
	

h=float(args.het)
s=float(args.selcoef)
f=float(args.minfreq)

mimhap=args.mimhap
fh=None
if(mimhap.endswith(".gz")):
     fh=gzip.open(mimhap, 'rb')
else:
     fh=open(mimhap)
cand=[]

for line in open(fh):
	line=line.rstrip()
	(chr,pos,anc,der,minorfreq)=parse_line(line)
	if(minorfreq<f):
		continue
	cand.append(SelectedCandidate(chr,pos,anc,der))

if(len(cand)<args.numsel):
	raise ValueError("Not enough SNPs with a minor allele frequency larger than the provided value")


selected=random.sample(cand,args.numsel)


print("[s]")
for t in selected:
	o=[]
	o.append(t.chr)
	o.append(t.pos)
	o.append(t.anc+"/"+t.der)
	s,h=provider.getSH()
	o.append(str(s))
	o.append(str(h))
	tmp="\t".join(o)
	print tmp