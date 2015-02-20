package edu.stanford.cs246.q2;

import java.io.IOException;
import java.util.ArrayList;

public class PageRank {

	public static long powerIterationInitAndLoadTimeInNanoSec = 0;
	public static void main(String[] args) throws IOException {

		ArrayList<Integer> testArray = new ArrayList<Integer>();
		testArray.add(2);
		testArray.add(2);
		long powerItStart = System.nanoTime();
		PowerIteration powerIteration = new PowerIteration("C:\\Users\\max\\OneDrive\\Stanford\\CS246\\ProblemSets\\HW3\\Q2\\graph.txt");
		powerIteration.Run();
		System.out.format("Power Iteration runtime in MS:%d\n", System.nanoTime() - powerItStart - powerIterationInitAndLoadTimeInNanoSec);
		
//		long MCStart = System.nanoTime();
//		MC mc1 = new MC(powerIteration);
//		mc1.Run();
//		System.out.format("MC runtime in MS:%d\n", System.nanoTime() - MCStart);
//		System.out.format("PowerIteration init in NS:%d\n", powerIterationInitAndLoadTimeInNanoSec);
		int numIterations = 100;
		double absoluteError = 0;
		int numTopElements = 100;

		for (int iteration = 0; iteration < numIterations; iteration++) {
			MC mc = new MC(powerIteration);
			mc.Run();
			ArrayList<Double> mcPageRank = mc.GetPageRank();
			ArrayList<Double> powIterPageRank = powerIteration.GetPageRank();

			ArrayList<Integer> topK = Utilities.topElements(mcPageRank,
					numTopElements);

			for (int i = 0; i < numTopElements; i++) {
				absoluteError += Math.abs(mcPageRank.get(topK.get(i))
						- powIterPageRank.get(topK.get(i)));
			}
		}
		
		System.out.println(absoluteError / (numTopElements * numIterations));		
	}
}
