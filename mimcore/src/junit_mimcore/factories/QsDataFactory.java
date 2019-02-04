package junit_mimcore.factories;

import mimcore.data.gpf.fitness.*;
import mimcore.io.fitnessfunction.FitnessFunctionReader;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import junit_mimcore.factories.SharedFactory;

/**
 * Created by robertkofler on 30/10/2017.
 */
public class QsDataFactory {


    /**
     * Increase from 0/0 to 1/1
     * @return
     */
    public static FitnessFunctionArbitraryLandscape getLinearIncrease()
    {
        ArrayList<ArbitraryLandscapeEntry> e=new ArrayList<ArbitraryLandscapeEntry>();
        e.add(new ArbitraryLandscapeEntry(1,1));
        e.add(new ArbitraryLandscapeEntry(0,0));
        FitnessFunctionArbitraryLandscape ffal=new FitnessFunctionArbitraryLandscape(e);
        return ffal;

    }

    public static FitnessFunctionArbitraryLandscape getNegPos()
    {
        ArrayList<ArbitraryLandscapeEntry> e=new ArrayList<ArbitraryLandscapeEntry>();
        e.add(new ArbitraryLandscapeEntry(0,1));
        e.add(new ArbitraryLandscapeEntry(1,0.5));
        e.add(new ArbitraryLandscapeEntry(-1,0.5));
        e.add(new ArbitraryLandscapeEntry(2,0));
        e.add(new ArbitraryLandscapeEntry(-2,0));
        FitnessFunctionArbitraryLandscape ffal=new FitnessFunctionArbitraryLandscape(e);
        return ffal;

    }

    /**
     *  0/0 - 1/1 - 3/0 - 7/1 - 12/0 - 18/1
     * @return
     */
    public static FitnessFunctionArbitraryLandscape getRugged()
    {
        ArrayList<ArbitraryLandscapeEntry> e=new ArrayList<ArbitraryLandscapeEntry>();
        e.add(new ArbitraryLandscapeEntry(3,0));
        e.add(new ArbitraryLandscapeEntry(1,1));
        e.add(new ArbitraryLandscapeEntry(7,1));
        e.add(new ArbitraryLandscapeEntry(0,0));
        e.add(new ArbitraryLandscapeEntry(12,0));
        e.add(new ArbitraryLandscapeEntry(18,1));
        FitnessFunctionArbitraryLandscape ffal=new FitnessFunctionArbitraryLandscape(e);
        return ffal;

    }


    /**
     * Increase from 0/10 to 1/20
     * @return
     */
    public static FitnessFunctionArbitraryLandscape getHighIncrease()
    {
        ArrayList<ArbitraryLandscapeEntry> e=new ArrayList<ArbitraryLandscapeEntry>();
        e.add(new ArbitraryLandscapeEntry(1,20));
        e.add(new ArbitraryLandscapeEntry(0,10));
        FitnessFunctionArbitraryLandscape ffal=new FitnessFunctionArbitraryLandscape(e);
        return ffal;
    }


    /**
     * phenotype betwen 0 and 1 (use 0.5)
     * Value changes at 1, 5 and 30 generations to fitness 1,5 and 30 respectively
     * @return
     */
    public static FitnessFunctionContainer getFitnessFunctionContainer()
    {
        HashMap<Integer,IFitnessCalculator> tos=new HashMap<Integer,IFitnessCalculator>();

        // 1
        ArrayList<ArbitraryLandscapeEntry> e=new ArrayList<ArbitraryLandscapeEntry>();
        e.add(new ArbitraryLandscapeEntry(1,1));
        e.add(new ArbitraryLandscapeEntry(0,1));
        tos.put(1,new FitnessFunctionArbitraryLandscape(e));

        e=new ArrayList<ArbitraryLandscapeEntry>();
        e.add(new ArbitraryLandscapeEntry(1,5));
        e.add(new ArbitraryLandscapeEntry(0,5));
        tos.put(5,new FitnessFunctionArbitraryLandscape(e));

        e=new ArrayList<ArbitraryLandscapeEntry>();
        e.add(new ArbitraryLandscapeEntry(1,30));
        e.add(new ArbitraryLandscapeEntry(0,30));
        tos.put(30,new FitnessFunctionArbitraryLandscape(e));

        FitnessFunctionContainer ffc = new FitnessFunctionContainer(tos);
        return ffc;


    }

    public static IFitnessCalculator getErrorBenjamin()
    {
        String benstring=
                "[interpolate]\n"+
                "1\t-0.5\t0.999995\n"+
                "1\t-0.4898989898989899\t0.999999999489848\n"+
                "1\t-0.4797979797979798\t0.9999947959391898\n"+
                "1\t-0.4696969696969697\t0.9999793893480257\n"+
                "1\t-0.4595959595959596\t0.9999537797163555\n"+
                "1\t-0.4494949494949495\t0.9999179670441791\n"+
                "1\t-0.4393939393939394\t0.9998719513314968\n"+
                "1\t-0.4292929292929293\t0.9998157325783084\n"+
                "1\t-0.41919191919191917\t0.9997493107846138\n"+
                "1\t-0.40909090909090906\t0.9996726859504133\n"+
                "1\t-0.398989898989899\t0.9995858580757065\n"+
                "1\t-0.3888888888888889\t0.9994888271604938\n"+
                "1\t-0.3787878787878788\t0.999381593204775\n"+
                "1\t-0.3686868686868687\t0.9992641562085501\n"+
                "1\t-0.35858585858585856\t0.9991365161718192\n"+
                "1\t-0.3484848484848485\t0.9989986730945822\n"+
                "1\t-0.33838383838383834\t0.9988506269768391\n"+
                "1\t-0.3282828282828283\t0.99869237781859\n"+
                "1\t-0.3181818181818182\t0.9985239256198347\n"+
                "1\t-0.30808080808080807\t0.9983452703805734\n"+
                "1\t-0.29797979797979796\t0.9981564121008061\n"+
                "1\t-0.28787878787878785\t0.9979573507805326\n"+
                "1\t-0.2777777777777778\t0.9977480864197531\n"+
                "1\t-0.2676767676767676\t0.9975286190184675\n"+
                "1\t-0.25757575757575757\t0.9972989485766759\n"+
                "1\t-0.24747474747474746\t0.9970590750943781\n"+
                "1\t-0.23737373737373735\t0.9968089985715743\n"+
                "1\t-0.22727272727272724\t0.9965487190082645\n"+
                "1\t-0.21717171717171713\t0.9962782364044486\n"+
                "1\t-0.20707070707070707\t0.9959975507601265\n"+
                "1\t-0.19696969696969696\t0.9957066620752985\n"+
                "1\t-0.18686868686868685\t0.9954055703499642\n"+
                "1\t-0.17676767676767674\t0.9950942755841241\n"+
                "1\t-0.16666666666666663\t0.9947727777777777\n"+
                "1\t-0.15656565656565652\t0.9944410769309254\n"+
                "1\t-0.1464646464646464\t0.994099173043567\n"+
                "1\t-0.13636363636363635\t0.9937470661157025\n"+
                "1\t-0.12626262626262624\t0.9933847561473319\n"+
                "1\t-0.11616161616161613\t0.9930122431384553\n"+
                "1\t-0.10606060606060602\t0.9926295270890726\n"+
                "1\t-0.09595959595959591\t0.9922366079991838\n"+
                "1\t-0.0858585858585858\t0.9918334858687889\n"+
                "1\t-0.07575757575757575\t0.991420160697888\n"+
                "1\t-0.06565656565656564\t0.990996632486481\n"+
                "1\t-0.055555555555555525\t0.9905629012345679\n"+
                "1\t-0.045454545454545414\t0.9901189669421487\n"+
                "1\t-0.035353535353535304\t0.9896648296092235\n"+
                "1\t-0.025252525252525193\t0.9892004892357923\n"+
                "1\t-0.015151515151515138\t0.9887259458218549\n"+
                "1\t-0.0050505050505050275\t0.9882411993674115\n"+
                "1\t0.005050505050505083\t0.987746249872462\n"+
                "1\t0.015151515151515249\t0.9872410973370064\n"+
                "1\t0.025252525252525304\t0.9867257417610448\n"+
                "1\t0.03535353535353536\t0.9862001831445771\n"+
                "1\t0.045454545454545525\t0.9856644214876032\n"+
                "1\t0.05555555555555558\t0.9851184567901234\n"+
                "1\t0.06565656565656575\t0.9845622890521375\n"+
                "1\t0.0757575757575758\t0.9839959182736455\n"+
                "1\t0.08585858585858586\t0.9834193444546475\n"+
                "1\t0.09595959595959602\t0.9828325675951434\n"+
                "1\t0.10606060606060608\t0.9822355876951332\n"+
                "1\t0.11616161616161624\t0.9816284047546169\n"+
                "1\t0.1262626262626263\t0.9810110187735945\n"+
                "1\t0.13636363636363646\t0.9803834297520662\n"+
                "1\t0.14646464646464652\t0.9797456376900316\n"+
                "1\t0.15656565656565657\t0.9790976425874911\n"+
                "1\t0.16666666666666674\t0.9784394444444444\n"+
                "1\t0.1767676767676768\t0.9777710432608917\n"+
                "1\t0.18686868686868696\t0.977092439036833\n"+
                "1\t0.19696969696969702\t0.9764036317722682\n"+
                "1\t0.20707070707070718\t0.9757046214671972\n"+
                "1\t0.21717171717171724\t0.9749954081216202\n"+
                "1\t0.2272727272727273\t0.9742759917355371\n"+
                "1\t0.23737373737373746\t0.973546372308948\n"+
                "1\t0.24747474747474751\t0.9728065498418529\n"+
                "1\t0.2575757575757577\t0.9720565243342516\n"+
                "1\t0.26767676767676774\t0.9712962957861443\n"+
                "1\t0.2777777777777778\t0.9705258641975308\n"+
                "1\t0.28787878787878796\t0.9697452295684114\n"+
                "1\t0.297979797979798\t0.9689543918987858\n"+
                "1\t0.3080808080808082\t0.9681533511886542\n"+
                "1\t0.31818181818181823\t0.9673421074380165\n"+
                "1\t0.3282828282828284\t0.9665206606468728\n"+
                "1\t0.33838383838383845\t0.965689010815223\n"+
                "1\t0.3484848484848485\t0.9648471579430671\n"+
                "1\t0.3585858585858587\t0.9639951020304051\n"+
                "1\t0.36868686868686873\t0.963132843077237\n"+
                "1\t0.3787878787878789\t0.9622603810835629\n"+
                "1\t0.38888888888888895\t0.9613777160493827\n"+
                "1\t0.3989898989898991\t0.9604848479746965\n"+
                "1\t0.40909090909090917\t0.9595817768595041\n"+
                "1\t0.4191919191919192\t0.9586685027038058\n"+
                "1\t0.4292929292929294\t0.9577450255076012\n"+
                "1\t0.43939393939393945\t0.9568113452708907\n"+
                "1\t0.4494949494949496\t0.9558674619936741\n"+
                "1\t0.45959595959595967\t0.9549133756759515\n"+
                "1\t0.4696969696969697\t0.9539490863177227\n"+
                "1\t0.4797979797979799\t0.9529745939189879\n"+
                "1\t0.48989898989898994\t0.9519898984797469\n"+
                "1\t0.5\t0.950995\n";

        BufferedReader br=new BufferedReader(new StringReader(benstring));

        FitnessFunctionContainer cont= new FitnessFunctionReader("fakefile",br, SharedFactory.getNullLogger()).readFitnessFunction();
        return cont.getFitnessCalculator(1,1);

    }


}
