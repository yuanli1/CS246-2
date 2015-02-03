package kmeans;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reduce extends Reducer<Text, Text, Text, DoubleWritable> {

  @Override
  public void reduce(Text key, Iterable<Text> values, Context context)
      throws IOException, InterruptedException {

	  ArrayList<Double> coordinatesSum = new ArrayList<Double>();
	  int numValues = 0;
		if (key.toString().compareTo("Cost") == 0) {
			double cost = 0;
			for (Text item : values)
			{
				cost += Double.parseDouble(item.toString());
			}
			
			context.write(new Text("Cost"), new DoubleWritable(cost));

		} else {
			for (Text item : values) {
				numValues++;
				String pointStr = item.toString();
				ArrayList<Double> point = KmeansUtils
						.convertPointFromStringToInt(pointStr);

				for (int i = 0; i < point.size(); i++) {
					double currentVal = coordinatesSum.size() > i ? coordinatesSum
							.get(i) : 0;
					if (coordinatesSum.size() <= i)
						coordinatesSum.add(currentVal + point.get(i));
					else
						coordinatesSum.set(i, currentVal + point.get(i));
				}
			}

			for (int i = 0; i < coordinatesSum.size(); i++) {
				double currentVal = coordinatesSum.get(i);
				coordinatesSum.set(i, currentVal / numValues);
			}
			context.write(
					new Text(KmeansUtils
							.convertPointFromIntToString(coordinatesSum)), null);
		}
  }
}