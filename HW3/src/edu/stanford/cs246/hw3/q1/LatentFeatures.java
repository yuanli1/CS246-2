package edu.stanford.cs246.hw3.q1;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import javax.swing.text.AbstractDocument.BranchElement;

import Jama.Matrix;

public class LatentFeatures {

	//public static String trainingDataFile = "C:\\Users\\max\\Documents\\GitHub\\CS246\\HW3\\Q1Data\\sanity.txt";
	private static String trainingDataFile = "C:\\Users\\max\\Documents\\GitHub\\CS246\\HW3\\Q1Data\\ratings.train.txt";
	private static String testDataFile = "C:\\Users\\max\\Documents\\GitHub\\CS246\\HW3\\Q1Data\\ratings.val.txt";

	private static int numLatentFactors = 20;
    private static int maxNumberOfIterations = 40;
	private static double learningRate = .04;
	private static double regularizationFactor = 0.2;
	
	public static void main(String[] args) throws IOException {
	    File trainingfile = new File(trainingDataFile);
		
	    BufferedReader br = null;
	    try {
			 br = new BufferedReader(new FileReader(trainingfile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    
	    String line;
	    int maxUserID = 0;
	    int maxMovieID = 0;
		while ((line = br.readLine()) != null) {
			String[] tokens = line.split("\\W+");
			if (tokens.length < 3)
				continue;
			
			int movieId = Integer.parseInt(tokens[1]);
			int userId = Integer.parseInt(tokens[0]);
			
			if (movieId > maxMovieID)
			{
				maxMovieID = movieId;
			}
			
			if(userId > maxUserID)
			{
				maxUserID = userId;
			}
		}
			/****************** Initializing the matrices *********************/		
		double initialMatrixValue = Math.sqrt((double)5 / numLatentFactors); 
		Matrix Q = new Matrix(maxUserID, numLatentFactors, 0);
		Matrix P = new Matrix(maxMovieID, numLatentFactors, 0);
		Random randomGenerator = new Random();
		
		for (int i=0;i < Q.getRowDimension();i++)
		{
			for(int j=0;j < Q.getColumnDimension();j++)
			{
				Q.set(i, j, initialMatrixValue * randomGenerator.nextDouble());
			}
		}
		
		for (int i=0;i < P.getRowDimension();i++)
		{
			for(int j=0;j < P.getColumnDimension();j++)
			{
				P.set(i, j, initialMatrixValue * randomGenerator.nextDouble());
			}
		}
		
		//Q.print(2,2);
		//P.print(2, 2);
		double error = Utilities.ErrorWithRegularization(trainingDataFile, Q, P, regularizationFactor);
		System.out.format("Initial error: %f\n",error);
			/*************************** Update P,Q *************************/
		for (int iterations = 0; iterations < maxNumberOfIterations; iterations++) {
			//System.out.format("iteration: %d\n ", iterations);
			try {
				br = new BufferedReader(new FileReader(trainingfile));
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			}

			while ((line = br.readLine()) != null) {
				String[] tokens = line.split("\\W+");

				if (tokens.length < 3)
					continue;
				int currentUserId = Integer.parseInt(tokens[0]);
				int currentMovieId = Integer.parseInt(tokens[1]);
				double currentRating = Double.parseDouble(tokens[2]);
				double[] newQi = new double[Q.getColumnDimension()];
				double[] newPi = new double[Q.getColumnDimension()];
				double qipx = 0;

				for (int i = 0; i < Q.getColumnDimension(); i++) {
					qipx += Q.get(currentUserId - 1, i)
							* P.get(currentMovieId - 1, i);
				}

				double errorDerivation = 2 * (currentRating - qipx);

				for (int i = 0; i < Q.getColumnDimension(); i++) {
					newQi[i] = Q.get(currentUserId - 1, i)
							+ learningRate
							* (errorDerivation * P.get(currentMovieId - 1, i) - regularizationFactor
									* Q.get(currentUserId - 1, i));
					newPi[i] = P.get(currentMovieId - 1, i)
							+ learningRate
							* (errorDerivation * Q.get(currentUserId - 1, i) - regularizationFactor
									* P.get(currentMovieId - 1, i));
				}

				for (int i = 0; i < Q.getColumnDimension(); i++) {
					Q.set(currentUserId - 1, i, newQi[i]);
					P.set(currentMovieId - 1, i, newPi[i]);
				}
			}

			error = Utilities.ErrorWithRegularization(trainingDataFile, Q, P, regularizationFactor);
			double testError = Utilities.Error(testDataFile, Q, P);
			double trainingErrorWithoutRegularizaiton = Utilities.Error(trainingDataFile, Q, P);
			//System.out.format("training error with reg:%f\n training error wo reg:%f\n test error:%f\n",
				//	error, trainingErrorWithoutRegularizaiton, testError );
			System.out.format("%f,%f,%f\n", error, trainingErrorWithoutRegularizaiton, testError);
		}
		error = Utilities.ErrorWithRegularization(trainingDataFile, Q, P, regularizationFactor);
		System.out.format("Training error: %f\n",error);
		//Test error:
		double testError = Utilities.Error(testDataFile, Q, P);
		System.out.format("Test error: %f\n", testError);
	}
}
