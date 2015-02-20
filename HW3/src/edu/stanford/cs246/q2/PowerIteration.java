package edu.stanford.cs246.q2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.sun.javafx.geom.transform.BaseTransform.Degree;

public class PowerIteration {

	ArrayList<ArrayList<Integer>> M = null; 
	ArrayList<Integer> Degrees  = null; 
	ArrayList<Integer> currentNode = null;
	ArrayList<Double> newRanks = null;
	ArrayList<Double> oldRanks = null;
	double beta = .8;
	int numberOfIterations = 40;
	int numNodes = 0;
	File inputFile = null;
	public PowerIteration(String inputFilestr) throws IOException {
		long runtimeStart = System.nanoTime();

	    inputFile = new File(inputFilestr);
		
		BufferedReader br = null;
		br = new BufferedReader(new FileReader(inputFile));
		String line;
		while ((line = br.readLine()) != null) {
			String[] tokens = line.split("\\W+");
			if (tokens.length < 2)
				continue;
			int sourceNode = Integer.parseInt(tokens[0]);
			if(sourceNode > numNodes)
			{
				numNodes = sourceNode;
			}
		}
		
		M = new ArrayList<ArrayList<Integer>>(numNodes);
		Degrees = new ArrayList<Integer>(numNodes);
		newRanks = new ArrayList<Double>(numNodes);
		oldRanks = new ArrayList<Double>(numNodes);

		for(int i=0; i < numNodes;i++)
		{
			M.add(new ArrayList<Integer>());
			newRanks.add(beta * 1 / (double)numNodes);
			oldRanks.add(beta * 1 / (double)numNodes);
			Degrees.add(0);	
		}
		
		br.close();
		PageRank.powerIterationInitAndLoadTimeInNanoSec += System.nanoTime() - runtimeStart;

	}
	public void Run() throws NumberFormatException, IOException{
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		String line;
		long runtimeStart = System.nanoTime();
		while ((line = br.readLine()) != null) {
			String[] tokens = line.split("\\W+");
			if (tokens.length < 2)
				continue;
			int sourceNode = Integer.parseInt(tokens[0]);
			int destinationNode = Integer.parseInt(tokens[1]);
			
			currentNode = M.get(sourceNode - 1);
			currentNode.add(destinationNode);
			
			int currentDegree = Degrees.get(sourceNode - 1);
			Degrees.set(sourceNode - 1, ++currentDegree);		
		}
		br.close();
		PageRank.powerIterationInitAndLoadTimeInNanoSec += System.nanoTime() - runtimeStart;

		for (int iteration = 0; iteration < numberOfIterations; iteration++) {
			for (int i = 0; i < numNodes; i++) {
				ArrayList<Integer> currentNodeNeighbours = M.get(i);
				for (int item : currentNodeNeighbours) {
					double currentRank = newRanks.get(item - 1);
					newRanks.set(item - 1, currentRank
							+ (beta / Degrees.get(i)) * oldRanks.get(i));
				}
			}
			
			for(int i=0; i < numNodes;i++)
			{
				oldRanks.set(i, newRanks.get(i));
				newRanks.set(i, (1-beta) * 1 / (double)numNodes);
			}
		}
		System.out.println();
	}
	
	public ArrayList<ArrayList<Integer>> GetM()
	{
		return M;
	}
	
	public ArrayList<Integer> GetDegrees()
	{
		return Degrees;
	}
	
	public int GetNumNodes()
	{
		return numNodes;
	}
	
	public ArrayList<Double> GetPageRank()
	{
		return oldRanks;
	}
}
