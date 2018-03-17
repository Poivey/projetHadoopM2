package fr.poivey.tache5alt;

import fr.poivey.utility.TopNPriorityQueue;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CrimeReducer extends Reducer<Text, Text, Text, IntWritable> {

  @Override
  public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
    Map<String, Integer> mapMonthCount = new HashMap<>();
    TopNPriorityQueue top3 = new TopNPriorityQueue(3);

    for (Text value : values) {
      String month = value.toString().split(":")[0];
      String localCount = value.toString().split(":")[1];
      int count = mapMonthCount.getOrDefault(month, 0) + Integer.valueOf(localCount);
      mapMonthCount.put(month, count);
    }
    for (Map.Entry<String, Integer> monthTotalCount : mapMonthCount.entrySet()) {
      top3.insert(monthTotalCount.getKey() + ":"+monthTotalCount.getValue());
    }

    while (top3.size() > 0){

      String[] monthAndCount = top3.pop().split(":");
      context.write(new Text(key.toString() + ":" + monthAndCount[0]), new IntWritable(Integer.valueOf(monthAndCount[1])));
    }
  }

}
