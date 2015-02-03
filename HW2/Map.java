package cs246.stanford.edu;
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

public class Map extends Mapper<LongWritable, Text, Text, IntWritable> {

  @Override
  public void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {
	  Configuration conf = context.getConfiguration();
	  String centroidsFile = conf.get("CentroidsFile");
	  ArrayList<ArrayList<Double>> centroids = new ArrayList<ArrayList<Double>>(10);
	  for (String lines : Files.readAllLines(Paths.get(centroidsFile), Charset.defaultCharset()))
	  {
		  ArrayList<Double> centroid = new ArrayList<Double>();
		  for(String tokens : lines.split("\\s+"))
		  {
			  centroid.add(Double.parseDouble(tokens));
		  }
		  centroids.add(centroid);
	  }
	  
    /*
     * TODO implement
     */

  }
}
