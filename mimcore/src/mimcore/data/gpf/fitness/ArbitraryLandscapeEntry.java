package mimcore.data.gpf.fitness;

/**
 * Created by robertkofler on 27/10/2017.
 */
public class ArbitraryLandscapeEntry {
    private final double phenotypicValue;
    private final double fitness;
    public ArbitraryLandscapeEntry(double phenotypicValue, double fitness)
    {
        this.phenotypicValue=phenotypicValue;
        this.fitness=fitness;

    }

    public double getPhenotypicValue(){return this.phenotypicValue;}

    public double getFitness(){return this.fitness;}
}
