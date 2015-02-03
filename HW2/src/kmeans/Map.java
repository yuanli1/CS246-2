package kmeans;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Map extends Mapper<LongWritable, Text, Text, Text> {

  @Override
  public void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {
	  Configuration conf = context.getConfiguration();
	  String centroidsFile = conf.get("CentroidsPath");
	  ArrayList<ArrayList<Double>> centroids = new ArrayList<ArrayList<Double>>(10);
	  for (String lines : Files.readAllLines(Paths.get(centroidsFile), Charset.defaultCharset()))
	  {
		  ArrayList<Double> centroid = new ArrayList<Double>();
		  
		  if(lines.contains("Cost"))
			  continue;
		  
		  for(String token : lines.split("\\s+"))
		  {
			  if(token.isEmpty())
				  continue;
			  centroid.add(Double.parseDouble(token));
		  }
		  if (!centroid.isEmpty())
		  {
			  centroids.add(centroid);
		  }
	  }
	  
	  //Find the centroid with mean distance to the current value:
	  String pointStr = value.toString();
	  ArrayList<Double> point = new ArrayList<Double>();
	  
	  for (String token : pointStr.split("\\s+"))
	  {
		  point.add(Double.parseDouble(token));
	  }
	  
	  ValueIndex minValueIndex = KmeansUtils.minCentroidDistanceValueIndex(point, centroids);
	  
	  context.write(new Text(Integer.toString(minValueIndex.index)), value);
	  context.write(new Text("Cost"),new Text(Double.toString(minValueIndex.value)));
  }
}
