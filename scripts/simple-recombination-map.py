import os
import sys
import re
import argparse
import random
import inspect
import gzip
import collections




parser = argparse.ArgumentParser(description="""           
Description
-----------
    Generate a simple recombination map for simulations with MimicrEE2""",formatter_class=argparse.RawDescriptionHelpFormatter,
epilog="""
Prerequisites
-------------
    python version 3+

Authors
-------
    Robert Kofler 
""")

parser.add_argument("--mimhap", type=str, required=True, dest="mimhap", default=None, help="the MimicrEE2 haplotype file")
parser.add_argument("--rr", type=int, required=True, dest="rr", default=None, help="the recombination rate in cM/Mbp")
args = parser.parse_args()

mimhap=args.mimhap
fh=None
if(mimhap.endswith(".gz")):
     fh=gzip.open(mimhap, 'rb')
else:
     fh=open(mimhap)

     


chrsize=collections.defaultdict(lambda:0)

for line in fh:
     a=re.split("\s+",line)
     chrom=a[0]
     pos=int(a[1])
     
     if(pos>chrsize[chrom]):
          chrsize[chrom]=pos

print("[cM/Mb]")
for chrom,end in chrsize.items():
     line="{0}:{1}..{2}\t{3}".format(chrom,1,end,args.rr)
     print(line)
     
     
     