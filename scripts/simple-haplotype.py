import os
import sys
import re
import argparse
import random
import inspect




def get_popline(popsize,frequency, haploids, hardy):
     if(haploids):
          countA=int(frequency*popsize)
          countB=popsize-countA
          ar=["G",]*countA+["A",]*countB
          return " ".join(ar)
     if(hardy):
          omf=1.0-frequency
          cA=int(frequency*frequency*popsize)
          cB=int(omf*omf*popsize)
          cAB=int(popsize-cA-cB)
          ar= ["GG",]*cA+["GA",]*cAB+["AA",]*cB
          return " ".join(ar)
     else:
          countA=int(frequency*popsize)
          countB=popsize-countA
          ar=["GG",]*countA+["AA",]*countB
          return " ".join(ar)

def sitepos(leng,sites):
     toret=set([])
     while(len(toret)<sites):
          r=random.randint(1,leng)
          toret.add(r)
     return toret

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

parser.add_argument("--chr-name", type=str, required=True, dest="name", default=None, help="the name of the chromosomes")
parser.add_argument("--chr-len", type=int, required=True, dest="len", default=None, help="the length of the chromosomes")
parser.add_argument("--S", type=int, required=True, dest="sites", default=None, help="the population size")
parser.add_argument("--N", type=int, required=True, dest="popsize", default=None, help="the population size")
parser.add_argument("--frequency", type=float, required=True, dest="freq", default=None, help="the frequency of haplotype A; frequency of haplotype B = 1-frequency")
parser.add_argument("--haploid", action="store_true", dest="haploid", default=None, help="simulate haploid genomes; if not provided used diploids instead")
parser.add_argument("--hardy-weinberg", action="store_true", dest="hardy", default=None, help="simulate a population in Hardy-Weinberg equilibrium; if not provided all genotypes are homozygous")



args = parser.parse_args()

pl=get_popline(args.popsize, args.freq, args.haploid, args.hardy)

sites=sitepos(args.len,args.sites)

for pos in sorted(sites):
     toprint=[]
     toprint.append(args.name)
     toprint.append(str(pos))
     toprint.append("G")
     toprint.append("G/A")
     toprint.append(pl)
     print("\t".join(toprint))
