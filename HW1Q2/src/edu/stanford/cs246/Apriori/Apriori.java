package edu.stanford.cs246.Apriori;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

public class Apriori {

	public static void main(String[] args) {
		int supportThreshold = 100;
		// Pass I:
		// Create the ItemsCount:
		HashMap<String, Integer> ItemsCount = new HashMap<String, Integer>();
		ArrayList<String> prunedItems = new ArrayList<String>();
		int lineCounter = 0;
		try {
			System.out.println("Single Items:");
			for (String line : Files.readAllLines(Paths.get(args[0]))) {
				lineCounter++;
				if (lineCounter % 10000 == 0) {
					System.out.println("Processing line:" + lineCounter);
					//break;
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
						&& (!prunedItems.contains(entry.getKey()))) {
					prunedItems.add(entry.getKey());
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Pass II:
		// Create the frequent Tuples of size 2:
		System.out.println("Two Items:");

		HashMap<String, Integer> frequentTuplesOfSizeTwo = new HashMap<String, Integer>(
				10000);
		ArrayList<String> prunedTuplesOfSizeTwo = new ArrayList<String>(10000);
		try {
			lineCounter = 0;
			for (String line : Files.readAllLines(Paths.get(args[0]))) {
				lineCounter++;
				if (lineCounter % 10000 == 0) {
					System.out.println("Processing line:" + lineCounter);
					//break;
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
						if ((prunedItems.contains(lineItems.get(i)))
								&& (prunedItems.contains(lineItems.get(j)))) {
							// Create the key:
							String sortedKey = GetSortedKey(lineItems.get(i)
									+ "," + lineItems.get(j));
							int newCount = 1;
							if (frequentTuplesOfSizeTwo.containsKey(sortedKey)) {
								newCount += frequentTuplesOfSizeTwo
										.get(sortedKey);
							}

							frequentTuplesOfSizeTwo.put(sortedKey, newCount);
						}
					}
				}

				for (Map.Entry<String, Integer> entry : frequentTuplesOfSizeTwo
						.entrySet()) {
					if ((entry.getValue() >= supportThreshold)
							&& (!prunedTuplesOfSizeTwo.contains(entry.getKey()))) {
						prunedTuplesOfSizeTwo.add(entry.getKey());
					}
				}				
			}
			// Need to update the prune items here:
			prunedItems.clear();
			for (String item : prunedTuplesOfSizeTwo) {
				for (String token : item.split(",")) {
					if (!prunedItems.contains(token)) {
						prunedItems.add(token);
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
					//break;
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
									&& (prunedItems.contains(lineItems.get(k)))) {
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
		
		// Compute 2 item rules:
		HashMap<String, Double> twoItemRules = new HashMap<String, Double>();
		String[] items = null;
		for(String entry : prunedTuplesOfSizeTwo)
		{
			items = entry.split(",");
			double confidence = (double)frequentTuplesOfSizeTwo.get(entry) / (double)ItemsCount.get(items[0]) ;
			twoItemRules.put(items[0] + "->" + items[1], confidence);
		    confidence = (double)frequentTuplesOfSizeTwo.get(entry) / (double)ItemsCount.get(items[1]) ;
			twoItemRules.put(items[1] + "->" + items[0], confidence);
		}
		
		Map sortedTwoItemRules = sortHashMapByValue(twoItemRules);
		//Output Top Five:
		int ruleCounter = 0;
		for(Object entry : sortedTwoItemRules.entrySet())
		{
			if(ruleCounter >= 5)
			{
				break;
			}
			System.out.print(((Entry<String, Integer>) entry).getKey() +":" + ((Entry<String, Integer>) entry).getValue() + "\t");
			ruleCounter++;
		}
		System.out.println("");
		
		// Compute 3 item rules:
		HashMap<String, Double> threeItemRules = new HashMap<String, Double>();

		for(String entry : prunedTuplesOfSizeThree)
		{
			items = entry.split(",");
			double confidence = (double)frequentTuplesOfSizeThree.get(entry) / (double)frequentTuplesOfSizeTwo.get(items[0] + "," + items[1]) ;
			threeItemRules.put(items[0] + "," + items[1] + "->" + items[2], confidence);
			
			confidence = (double)frequentTuplesOfSizeThree.get(entry) / (double)frequentTuplesOfSizeTwo.get(items[0] + "," + items[2]) ;
			threeItemRules.put(items[0] + "," + items[2] + "->" + items[1], confidence);
			
			confidence = (double)frequentTuplesOfSizeThree.get(entry) / (double)frequentTuplesOfSizeTwo.get(items[1] + "," + items[2]) ;
			threeItemRules.put(items[1] + "," + items[2] + "->" + items[0], confidence);
		}
		
		Map sortedThreeItemRules = sortHashMapByValue(threeItemRules);
		//Output Top Five:
	   ruleCounter = 0;
		for(Object entry : sortedThreeItemRules.entrySet())
		{
			if(ruleCounter >= 5)
			{
				break;
			}
			System.out.print(((Entry<String, Integer>) entry).getKey() +":" + ((Entry<String, Integer>) entry).getValue() + "\t");
			ruleCounter++;
		}
		System.out.println("");
	}

	public static String GetSortedKey(String commaSeparatedStrInput) {
		String outputStr = "";
		TreeSet<String> strArray = new TreeSet<String>();
		for (String item : commaSeparatedStrInput.split(",")) {
			strArray.add(item);
		}

		for (String item : strArray) {
			outputStr += item + ",";
		}

		return outputStr.substring(0, outputStr.length() - 1);
	}
	
	public static <K extends Comparable<? super K>, V extends Comparable<? super V>> Map<K, V> sortHashMapByValue(
			Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(
				map.entrySet());
		
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				int comparedVal = o1.getValue().compareTo(o2.getValue());
				
				if(0 != comparedVal)
				{
					return -1 * comparedVal;
				}
				
				// For breaking ties, sort lexicographically:
				return (o1.getKey().compareTo(o2.getKey()));
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
}
