package kmeans;

import java.util.ArrayList;

public class KmeansUtils {
	public static double distance(ArrayList<Double> point, ArrayList<Double> centroid)
	{
		double distanceValue = 0;
		for(int i=0; i < point.size();i++)
		{
			distanceValue += Math.pow((point.get(i) - centroid.get(i)),2);
		}
		
		return distanceValue;
	}
	
	public static ValueIndex minCentroidDistanceValueIndex(ArrayList<Double> point, ArrayList<ArrayList<Double>> centroids)
	{
		int index = 0;
		double minDistance = -1;
		double currentDistance = 0;
		for(int i=0;i < centroids.size();i++)
		{
			currentDistance = distance(point, centroids.get(i));
			if ((minDistance > currentDistance) || (minDistance == -1))
			{
				minDistance = currentDistance;
				index = i;
			}
		}
		
		ValueIndex valueIndex = new ValueIndex(index, minDistance);
		
		return valueIndex;
	}
	
	public static ArrayList<Double> convertPointFromStringToInt(String pointStr)
	{
		ArrayList<Double> point = new ArrayList<Double>();
		
		for(String token : pointStr.split("\\s+"))
		{
			point.add(Double.parseDouble(token));
		}
		
		return point;
	}
	
	public static String convertPointFromIntToString(ArrayList<Double> point)
	{
		StringBuilder output = new StringBuilder();
		for(int i=0;i < point.size();i++)
		{
			output.append(point.get(i).toString());
			output.append(" ");
		}
		
		return output.toString();
	}

}
