package edu.stanford.cs246.q2;

import java.util.ArrayList;
import java.util.Random;

public class MC {
	ArrayList<ArrayList<Integer>> M = null;
	ArrayList<Integer> Degrees = null;
	int R = 5;
	int numNodes = 0;
	double beta = .8;
	ArrayList<Double> pageRank = new ArrayList<Double>();
	Random randGenerator = new Random();
	public MC(PowerIteration powIter) {
		M = powIter.GetM();
		Degrees = powIter.GetDegrees();
		numNodes = powIter.GetNumNodes();
		for(int i=0; i < numNodes;i++)
		{
			pageRank.add((double) 0);
		}
	}

	public void Run() {
		int MaxRandValue = 9;
		for (int j = 0; j < numNodes; j++) {
			for (int i = 0; i < R; i++) {
				// First see if we are done or stepping:
				pageRank.set(j, pageRank.get(j) + 1);

				boolean step = true;
				while (true) {
					int randValue = randGenerator.nextInt(MaxRandValue + 1);
					step = randValue <= beta * MaxRandValue ? true : false;
					if (!step) {
						break;
					}
					
					int nextNodeIndex = randGenerator
							.nextInt(Degrees.get(j));
					int nextNode = M.get(j).get(nextNodeIndex);
					pageRank.set(nextNode -1 , pageRank.get(nextNode - 1) + 1);
				}
			}
		}
		
		for(int i=0;i < numNodes;i++)
		{
			pageRank.set(i, pageRank.get(i) / (numNodes * R /(1-beta)));
		}
	}
	
	public ArrayList<Double> GetPageRank()
	{
		return pageRank;
	}
}
