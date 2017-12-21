package mimcore.data.fasta;

import mimcore.data.Chromosome;
import mimcore.data.haplotypes.HaploidGenome;
import mimcore.data.haplotypes.SNP;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by robertkofler on 21/12/2017.
 */
public class FastaCollectionBuilder {
    private final HashMap<Chromosome,FastaRecordBuilder>  builder;
    private final boolean stringent;

    public FastaCollectionBuilder(ArrayList<FastaRecord> fastas, boolean stringent)
    {
        HashMap<Chromosome,FastaRecordBuilder>  tostore=new HashMap<Chromosome,FastaRecordBuilder>();
        for(FastaRecord fa:fastas)
        {
                FastaRecordBuilder fab=new FastaRecordBuilder(fa);
                Chromosome chr=Chromosome.getChromosome(fa.getHeader());
                tostore.put(chr,fab);
        }
        this.builder=tostore;
        this.stringent=stringent;
    }


    public void introduceChanges(HaploidGenome hapGenome)
    {
        for(SNP s: hapGenome.getSNPCollection().getSNPs())
        {
            char refChar=s.referenceCharacter();
            char replaceChar=hapGenome.getAllele(s.genomicPosition());

            Chromosome chr=s.genomicPosition().chromosome();
            int indexPosition=s.genomicPosition().position()-1;
            builder.get(chr).replaceCharacter(indexPosition,refChar,replaceChar,stringent);
        }

    }


    public ArrayList<FastaRecord> getRecords(String headerAddendum)
    {
        ArrayList<FastaRecord> records=new ArrayList<FastaRecord>();
        for(FastaRecordBuilder fab: this.builder.values())
        {
            records.add(fab.getFastaRecord(headerAddendum));
        }
        return records;
    }

}
