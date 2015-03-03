package edu.stanford.cs246.q2;

import java.util.Comparator;

public class valueIndexComparator implements Comparator<valueIndex> {

	public int compare(valueIndex entry1, valueIndex entry2) {
		if(entry1.value > entry2.value)
		{
			return -1;
		}
		else if(entry1.value < entry2.value)
		{
			return +1;
		}
		else
		{
			return entry1.index < entry2.index ? 1 : -1 ;
		}
	}
}