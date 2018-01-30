package fr.poivey.tache5;

import fr.poivey.tache5.utility.TopNPriorityQueue;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.StreamSupport;

public class CrimeReducer extends Reducer<Text, Text, Text, IntWritable> {

  @Override
  public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

    TopNPriorityQueue top3 = new TopNPriorityQueue();

    for (Text value : values) {
      top3.insert(value.toString());
    }

    while (top3.size() > 0){
      String[] monthAndCount = top3.pop().split(":");
      context.write(new Text(key.toString() + ":" + monthAndCount[0]), new IntWritable(Integer.valueOf(monthAndCount[1])));
    }
  }


}
