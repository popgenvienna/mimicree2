package mimcore.data.migration;

/**
 * Created by robertkofler on 24/10/2017.
 */
public class MigrationEntry {
    private int generation;
    private int migrantCount;
    private String pathToSourcePopulation;

    public MigrationEntry(int generation, int migrantCount, String pathToSourcePopulation)
    {
        this.generation=generation;
        this.migrantCount=migrantCount;
        this.pathToSourcePopulation=pathToSourcePopulation;
    }


    public int getGeneration(){return this.generation;}
    public int getMigrantCount(){return this.migrantCount;}
    public String getPathToSourcePopulation(){return this.pathToSourcePopulation;}

    public boolean useDefault()
    {
        if(this.pathToSourcePopulation==null)
        {
            return true;
        }
        else return false;
    }
}
