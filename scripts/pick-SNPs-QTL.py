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
		

class ProviderDefault:
	def __init__(self,a,d):
		self.a=a
		self.d=d
		
	def getAD(self):
		return ((self.a,self.d))


class ProviderFile:
	def __init__(self,file):
		scl=[]
		for l in open(file):
			l=l.rstrip()
			a=re.split("\s+",l)
			if(len(a)!=2):
				raise Exception("Wrong format of selection coefficients; must have two columns; space separated"+a)
			scl.append(a)
		self.scl=scl
	
	def getAD(self):
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
    Randomly pick QTLs  MimicrEE2""",formatter_class=argparse.RawDescriptionHelpFormatter,
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
parser.add_argument("--d", type=float, required=False, dest="dom", default=0.0, help="dominance effect for a QTL")
parser.add_argument("--a", type=float, required=False, dest="add", default=None, help="the additive effect of a QTL")
parser.add_argument("--f", type=float, required=False, dest="minfreq", default=0.0, help="the heterozygous effect of the SNPs")


args = parser.parse_args()

provider=None
if(args.effectfile is not None):
	provider=ProviderFile(args.effectfile)
else:
	if(args.add is None):
		parser.print_usage()
		raise Exception("either a default selection coefficient --s or a file with selection coefficients --effect-file must be provided")
	provider=ProviderDefault(args.add,args.dom)
	
f=float(args.minfreq)

mimhap=args.mimhap
fh=None
if(mimhap.endswith(".gz")):
     fh=gzip.open(mimhap, 'rb')
else:
     fh=open(mimhap)
cand=[]

for line in fh:
	line=line.rstrip()
	(chr,pos,anc,der,minorfreq)=parse_line(line)
	if(minorfreq<f):
		continue
	cand.append(SelectedCandidate(chr,pos,anc,der))

if(len(cand)<args.numsel):
	raise ValueError("Not enough SNPs with a minor allele frequency larger than the provided value")


selected=random.sample(cand,args.numsel)


for t in selected:
	o=[]
	o.append(t.chr)
	o.append(t.pos)
	if(random.random()<0.5):
		o.append(t.anc+"/"+t.der)
	else:
		o.append(t.der+"/"+t.anc)
	a,d=provider.getAD()
	o.append(str(a))
	o.append(str(d))
	tmp="\t".join(o)
	print tmp