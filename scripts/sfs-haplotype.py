import os
import sys
import re
import argparse
import random
import inspect


def parseSFS(toparse):
     suma=0.0
     sfs=[toparse]
     if("," in toparse):
          sfs=toparse.split(",")
     sfs=[float(s) for s in sfs]
     for s in sfs:
          suma+=s
     sfs=[s/suma for s in sfs]
     return sfs

class SFSCategory:
     def __init__(self,start,end,freq,cumfreq):
          self.start=start
          self.end=end
          self.freq=freq
          self.cumfreq=cumfreq
          
     def getrandom(self):
          r=random.uniform(self.start,self.end)
          return r

class randomSFS:
     def __init__(self,sfs):
          self.sfs=sfs
          sfscategories=len(sfs)
          popfreqsteps=1.0/float(sfscategories)
          
          runcumfreq=0.0
          cat=[]
          start=0.0
          for s in sfs:
               end=start+popfreqsteps
               runpopfreq=start
               runcumfreq+=s
               sc=SFSCategory(start,end,s,runcumfreq)
               cat.append(sc)
               start=end
          self.cat=cat
     
     def getFreq(self):
          r=random.random()
          for c in self.cat:
               if r<=c.cumfreq:
                    pos=c.getrandom()
                    return pos
               
               
          
          
     


def get_popline(popsize,frequency, haploids):
     ps=popsize
     if(not haploids):
          ps=2*popsize
     countA=int(frequency*ps)
     # ensure segregation
     if(countA<1):
          countA=1
     if(countA==ps):
          countA=ps-1
     countB=ps-countA
     ar=["A",]*countA+["G",]*countB
     if(haploids):
          random.shuffle(ar)
          return " ".join(ar)
     else:
          random.shuffle(ar)
          topr=[]
          while(len(ar)>0):
               tmp=ar.pop(0)
               tmp+=ar.pop(0)
               topr.append(tmp)
          return " ".join(topr)


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
parser.add_argument("--sfs", type=str, required=True, dest="sfs", default=None, help="the site frequency spectrum; a coma separated list of frequencies 10,5,6,1,2")
parser.add_argument("--haploid", action="store_true", dest="haploid", default=None, help="simulate haploid genomes; if not provided used diploids instead")

args = parser.parse_args()

sfs=parseSFS(args.sfs)
rsfs=randomSFS(sfs)


sites=sitepos(args.len,args.sites)

for pos in sorted(sites):
     freq=rsfs.getFreq()
     pl=get_popline(args.popsize, freq, args.haploid)
     toprint=[]
     toprint.append(args.name)
     toprint.append(str(pos))
     toprint.append("G")
     toprint.append("G/A")
     toprint.append(pl)
     print("\t".join(toprint))
