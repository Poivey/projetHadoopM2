package fr.poivey.tache5;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class CrimeMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

  Map<String, Integer> monthCountMap;
  private Text[] monthList;

  @Override
  protected void setup(Context context) {
    monthCountMap = new HashMap<>();
    monthList = new Text[]{new Text("january"), new Text("febuary"), new Text("march"),
        new Text("april"), new Text("may"), new Text("june"), new Text("july"),
        new Text("august"), new Text("september"), new Text("october"),
        new Text("november"), new Text("december")};
  }

  @Override
  public void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {
    if (key.get() != 0L) {
      String[] ligne = value.toString().split(",");
      String month = ligne[2].substring(0, 2);

      int count = monthCountMap.getOrDefault(month, 0) + 1;
      monthCountMap.put(month, count);
    }
  }

  @Override
  protected void cleanup(Context context) throws IOException, InterruptedException {
    for (Map.Entry<String, Integer> entry : monthCountMap.entrySet()) {
      context.write(new Text(monthList[Integer.parseInt(entry.getKey())-1]), new IntWritable(entry.getValue()));
    }
  }
}
