package mim2.test.fastaPlayground;


/**
 * Created by robertkofler on 21/12/2017.
 */
public class fastaPlay {

    public static void play()
    {

    }

    public static void writeFasta() {

        String hw="AAATTTCCC";
        int i=0;
        while(i<hw.length())
        {
            int e=i+3;
            if(e>hw.length())e=hw.length();
            System.out.println(hw.substring(i,e));
            i=e;
        }


    }

    //System.out.print(fasta);
}
