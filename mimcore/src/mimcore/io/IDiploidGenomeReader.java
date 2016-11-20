package qmimcore.io;

import qmimcore.data.DiploidGenome;

import java.util.ArrayList;

public interface IDiploidGenomeReader {
	
	public abstract ArrayList<DiploidGenome> readGenomes();

}
