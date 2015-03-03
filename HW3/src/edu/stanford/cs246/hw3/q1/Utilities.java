package edu.stanford.cs246.hw3.q1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import Jama.Matrix;

public class Utilities {

	public static double ErrorWithRegularization(String trainingDataFilePath, Matrix Q, Matrix P, double regularizationFactor) 
			throws NumberFormatException, IOException {
		double error = 0;
		
		 File trainingfile = new File(trainingDataFilePath);
		    BufferedReader br = null;
		    
		    try {
				 br = new BufferedReader(new FileReader(trainingfile));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    String line;

			while ((line = br.readLine()) != null) {
				String[] tokens = line.split("\\W+");

				if (tokens.length < 3)
					continue;
				
				int currentUserId = Integer.parseInt(tokens[0]);
				int currentMovieId = Integer.parseInt(tokens[1]);
				double currentRating = Double.parseDouble(tokens[2]);
					
				double currentSum = 0;
				for (int k = 0; k < Q.getColumnDimension(); k++) {
					currentSum += Q.get(currentUserId -1, k) * P.get(currentMovieId -1, k);
				}
				
				double partialError = Math.pow((currentSum - currentRating), 2);
				error += partialError;
			}
			//Need to add regularization error as well:
			error += regularizationFactor * (Math.pow(Q.normF(),2) + Math.pow(P.normF(), 2));
		return error;
	}
	
	public static double Error(String trainingDataFilePath, Matrix Q, Matrix P) 
			throws NumberFormatException, IOException {
		double error = 0;
		
		 File trainingfile = new File(trainingDataFilePath);
		    BufferedReader br = null;
		    
		    try {
				 br = new BufferedReader(new FileReader(trainingfile));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    String line;

			while ((line = br.readLine()) != null) {
				String[] tokens = line.split("\\W+");

				if (tokens.length < 3)
					continue;
				
				int currentUserId = Integer.parseInt(tokens[0]);
				int currentMovieId = Integer.parseInt(tokens[1]);
				double currentRating = Double.parseDouble(tokens[2]);
					
				double currentSum = 0;
				for (int k = 0; k < Q.getColumnDimension(); k++) {
					currentSum += Q.get(currentUserId -1, k) * P.get(currentMovieId -1, k);
				}
				
				double partialError = Math.pow((currentSum - currentRating), 2);
				error += partialError;
			}

			return error;
	}
}
