package kmeans;

import java.util.Arrays;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class kmeans extends Configured implements Tool {

	public static void main(String[] args) throws Exception {
		System.out.println(Arrays.toString(args));
		Configuration conf = new Configuration();
		//conf.set("CentroidsPath", "/home/cloudera/git/CS246/HW2/cSanity.txt");
		int res = ToolRunner.run(conf, new kmeans(), args);
		System.exit(res);
	}
	
	@Override
	public int run(String[] args) throws Exception {

		String baseOutputPath = "output";
		String centroidsPath = "/home/cloudera/git/CS246/HW2/c2.txt";
		System.out.println(Arrays.toString(args));
		
		for (int i = 0; i < 20; i++) {
			Configuration conf = new Configuration();
			if(i > 0)
			{
				centroidsPath = args[1] + Integer.toString(i-1)+"/part-r-00000";
			}
			conf.set("CentroidsPath", centroidsPath);
			@SuppressWarnings("deprecation")
			Job job = new Job(conf, "kmeans");
			job.setJarByClass(kmeans.class);
			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(IntWritable.class);
			job.setMapOutputValueClass(Text.class);
			job.setMapOutputKeyClass(Text.class);
			job.setMapperClass(Map.class);
			job.setReducerClass(Reduce.class);
			job.setInputFormatClass(TextInputFormat.class);
			job.setOutputFormatClass(TextOutputFormat.class);
			FileInputFormat.addInputPath(job, new Path(args[0]));
			FileOutputFormat.setOutputPath(job, new Path(args[1] + Integer.toString(i)));
			job.waitForCompletion(true);
		}
		
		return 0;
	}
}
