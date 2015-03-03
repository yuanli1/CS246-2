package edu.stanford.cs246.q2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

public class Utilities {
	
	public static ArrayList<Integer> topElements(ArrayList<Double> inputList, int numTopValues)
	{
		ArrayList<Integer> topIndicies = new ArrayList<Integer>();
		TreeSet<valueIndex> treeSet = new TreeSet<valueIndex>(new valueIndexComparator());
		for (int i = 0; i < inputList.size(); i++) {
			boolean result = treeSet.add(new valueIndex(i, inputList.get(i)));
			if(!result)
			{
				break;
			}
		}
		
		Iterator<valueIndex> iterator = treeSet.iterator();
		int i = 0;
		while(i < numTopValues)
		{
			topIndicies.add(iterator.next().index);
			i++;
		}
		
		return topIndicies;
	}
}
