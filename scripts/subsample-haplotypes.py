#!/usr/bin/env python
import sys
import collections
import argparse
import copy
import math
import random
import gzip
import re

def createRandomIndices(fullSize,subSize):
	if subSize> fullSize:
		raise Exception("Invalid; sample size must be smaller than size of full data set")
	toDraw=list(range(0,fullSize))
	toret=[]
	for i in range(0,subSize):
		index=int(random.random()*len(toDraw))
		toret.append(toDraw.pop(index))
	return toret


parser = argparse.ArgumentParser(description="""           
Description
-----------
    Subsample MimicrEE2 haplotypes""",formatter_class=argparse.RawDescriptionHelpFormatter,
epilog="""
Prerequisites
-------------
    python version 3+

Authors
-------
    Robert Kofler 
""")


parser.add_argument("--haplotypes", type=str, required=True, dest="haplotypes", default=None, help="MimicreEE2 haplotypes; either *.gz or not")
parser.add_argument("--count", type=int, required=True, dest="count", default=None, help="the number of diploid individuals to sample")
args = parser.parse_args()



# handle input
fh=None
file=args.haplotypes
if(file.endswith(".gz")):
	fh=gzip.open(file,mode='rb')
else:
	fh=open(file)


randindex=None 

for line in fh:
	line=line.rstrip()
	a=re.split("\\s+",line)
	overhead=a[:4]
	haps=a[4:]

	# create random indice only once, when requested
	if randindex is None:
		randindex=createRandomIndices(len(haps),int(args.count))
		sec=" ".join([str(i) for i in randindex])
		print("# "+sec)

	novelhaps=[haps[i] for i in randindex]
	
	hapset=set(novelhaps)
	if(len(hapset)>1):
		hapl=" ".join(novelhaps)
		overhead.append(hapl)
		
		topr="\t".join(overhead)
		print(topr)

	
