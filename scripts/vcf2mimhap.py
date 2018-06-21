import os
import sys
import re
import argparse
import random
import inspect
import gzip
import collections


def parsechrfilter(chrstring):
    tmp=[chrstring]
    if "," in chrstring:
        tmp=chrstring.split(",")
    return set(tmp)

def format_haps(haps,haploid):
    toret=[]
    while(len(haps)>0):
        h1=haps.pop(0)
        if(haploid):
            toret.append(h1)
        else:
            h2=haps.pop(0)
            toret.append(h1+h2)
    return " ".join(toret)
    


parser = argparse.ArgumentParser(description="""           
Description
-----------
    Convert vcf files into MimicrEE2 haplotypes""",formatter_class=argparse.RawDescriptionHelpFormatter,
epilog="""
Prerequisites
-------------
    python version 3+

Authors
-------
    Christos Vlachos and Robert Kofler 
""")

parser.add_argument("--vcf", type=str, required=True, dest="vcf", default=None, help="the vcf file")
parser.add_argument("--chr-filter", type=str, required=True, dest="chrfilter", default=None, help="the chromosome filter")
parser.add_argument("--haploid", action="store_true", dest="haploid", default=None, help="simulate haploid genomes; if not provided used diploids instead")

args = parser.parse_args()
chrfilter=parsechrfilter(args.chrfilter)

for line in open(args.vcf) :
    a=line.rstrip().split("\t")
    chrom=a[0]
    if(chrom not in chrfilter):
        continue
    pos=int(a[1])
 
    reference=a[3]
    alternative=a[4]
    tmp,tmp2,mut_type=a[2].split("_")
    if mut_type!="SNP":
        continue
    

    count_ancestral,count_derived=a[7].split(";")
    nothing,count_ancestral=count_ancestral.split("=")
    nothing,count_derived=count_derived.split("=")

    toprint=[]
    toprint.append(chrom)
    toprint.append(str(pos))
    toprint.append(reference)
    toprint.append(reference+"/"+alternative)
    l=len(a)    

    haps=[]
    for i in range(9,l):
        tmp2=a[i].split("/")
        for h in tmp2:
            if h =="0": 
                haps.append(reference)
            elif h=="1":
                haps.append(alternative)
            elif h==".":
                if int(count_ancestral)>=int(count_derived):
                    haps.append(reference)
                else: 
                    haps.append(alternative)
            else:
                raise Exception("Invalid allele "+h+" "+line)
    toprint.append(format_haps(haps,args.haploid))
    print "\t".join(toprint)
            



