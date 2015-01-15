package edu.stanford.cs246.Apriori;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class Apriori {

	public static void main(String[] args) {
		int supportThreshold = 100;
		// Pass I:
		// Create the ItemsCount:
		HashMap<String, Integer> ItemsCount = new HashMap<String, Integer>();
		ArrayList<String> PrunedItems = new ArrayList<String>();
		int lineCounter = 0;
		try {
			System.out.println("Single Items:");
			for (String line : Files.readAllLines(Paths.get(args[0]))) {
				lineCounter++;
				if (lineCounter % 10000 == 0) {
					System.out.println("Processing line:" + lineCounter);
					break;
				}

				ArrayList<String> lineItems = new ArrayList<String>();
				// Create a list of items:
				for (String token : line.split("\\s+")) {
					if (!lineItems.contains(token)) {
						lineItems.add(token);
					}
				}

				for (String item : lineItems) {
					int newCount = 1;
					if (ItemsCount.containsKey(item)) {
						newCount += ItemsCount.get(item);
					}

					ItemsCount.put(item, newCount);
				}
			}
			for (Map.Entry<String, Integer> entry : ItemsCount.entrySet()) {
				if ((entry.getValue() >= supportThreshold)
						&& (!PrunedItems.contains(entry.getKey()))) {
					PrunedItems.add(entry.getKey());
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Pass II:
		// Create the frequent Tuples of size 2:
		System.out.println("Two Items:");

		HashMap<String, Integer> frequestTuplesOfSizeTwo = new HashMap<String, Integer>(
				10000);
		ArrayList<String> prunedTuplesOfSizeTwo = new ArrayList<String>(10000);
		try {
			lineCounter = 0;
			for (String line : Files.readAllLines(Paths.get(args[0]))) {
				lineCounter++;
				if (lineCounter % 10000 == 0) {
					System.out.println("Processing line:" + lineCounter);
					break;
				}

				ArrayList<String> lineItems = new ArrayList<String>();
				// Create a list of items:
				for (String token : line.split("\\s+")) {
					if (!lineItems.contains(token)) {
						lineItems.add(token);
					}
				}

				for (int i = 0; i < lineItems.size(); i++) {
					for (int j = i + 1; j < lineItems.size(); j++) {
						if ((PrunedItems.contains(lineItems.get(i)))
								&& (PrunedItems.contains(lineItems.get(j)))) {
							// Create the key:
							String sortedKey = GetSortedKey(lineItems.get(i)
									+ "," + lineItems.get(j));
							int newCount = 1;
							if (frequestTuplesOfSizeTwo.containsKey(sortedKey)) {
								newCount += frequestTuplesOfSizeTwo
										.get(sortedKey);
							}

							frequestTuplesOfSizeTwo.put(sortedKey, newCount);
						}
					}
				}

				for (Map.Entry<String, Integer> entry : frequestTuplesOfSizeTwo
						.entrySet()) {
					if ((entry.getValue() >= supportThreshold)
							&& (!prunedTuplesOfSizeTwo.contains(entry.getKey()))) {
						prunedTuplesOfSizeTwo.add(entry.getKey());
					}
				}				
			}
			// Need to update the prune items here:
			PrunedItems.clear();
			for (String item : prunedTuplesOfSizeTwo) {
				for (String token : item.split(",")) {
					if (!PrunedItems.contains(token)) {
						PrunedItems.add(token);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Pass III:
		System.out.println("Three Items:");

		// Create the frequent Tuples of size 3:
		HashMap<String, Integer> frequentTuplesOfSizeThree = new HashMap<String, Integer>();
		ArrayList<String> prunedTuplesOfSizeThree = new ArrayList<String>();
		try {
			lineCounter = 0;
			for (String line : Files.readAllLines(Paths.get(args[0]))) {
				lineCounter++;
				if (lineCounter % 10000 == 0) {
					System.out.println("Processing line:" + lineCounter);
					break;
				}
				ArrayList<String> lineItems = new ArrayList<String>();
				// Create a list of items:
				for (String token : line.split("\\s+")) {
					if (!lineItems.contains(token)) {
						lineItems.add(token);
					}
				}

				for (int i = 0; i < lineItems.size(); i++) {
					for (int j = i + 1; j < lineItems.size(); j++)
						for (int k = j + 1; k < lineItems.size(); k++) {
							String pairSortedKey = GetSortedKey(lineItems
									.get(i) + "," + lineItems.get(j));
							
							//Need to skip cases like: A,B A
							if (pairSortedKey.contains(lineItems.get(k))) {
								continue;
							}
							if ((prunedTuplesOfSizeTwo.contains(pairSortedKey))
									&& (PrunedItems.contains(lineItems.get(k)))) {
								// Create the key:
								String sortedKey = GetSortedKey(pairSortedKey
										+ "," + lineItems.get(k));
								int newCount = 1;
								if (frequentTuplesOfSizeThree
										.containsKey(sortedKey)) {
									newCount += frequentTuplesOfSizeThree
											.get(sortedKey);
								}

								frequentTuplesOfSizeThree.put(sortedKey,
										newCount);
							}
						}
				}

				for (Map.Entry<String, Integer> entry : frequentTuplesOfSizeThree
						.entrySet()) {
					if ((entry.getValue() >= supportThreshold)
							&& (!prunedTuplesOfSizeThree.contains(entry
									.getKey()))) {
						prunedTuplesOfSizeThree.add(entry.getKey());
					}
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(GetSortedKey("Hello,world,helllo"));
	}

	public static String GetSortedKey(String commaSeparatedStrInput) {
		String outputStr = "";
		TreeSet<String> strArray = new TreeSet<String>();
		for (String item : commaSeparatedStrInput.toUpperCase().split(",")) {
			strArray.add(item);
		}

		for (String item : strArray) {
			outputStr += item + ",";
		}

		return outputStr.substring(0, outputStr.length() - 1);
	}
}
