package mimcore.data.fasta;

/**
 * Created by robertkofler on 21/12/2017.
 */
public class FastaRecordBuilder {
    private char[] sequence;
    private final String header;

    public FastaRecordBuilder(FastaRecord fastaRecord)
    {
        this.sequence=fastaRecord.getSequence().toCharArray();
        this.header=fastaRecord.getHeader();
    }

    public void replaceCharacter(int position, char refChar, char targetChar, boolean reportErrors)
    {
        if(reportErrors && this.sequence[position]!=refChar) throw new IllegalArgumentException("Reference characters from fasta-file and the MimicrEE2 haplotype file do not match for "+header+" position "+position);

        this.sequence[position]=targetChar;
    }


    public FastaRecord getFastaRecord(String headerAddendum)
    {
        return new FastaRecord(this.header+headerAddendum,new String(this.sequence));
    }
}
