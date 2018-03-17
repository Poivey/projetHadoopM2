package fr.poivey.tache5;

import java.io.IOException;

import fr.poivey.utility.TopNPriorityQueue;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class CrimeReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

  private TopNPriorityQueue top3;

  @Override
  protected void setup(Context context) {
    top3 = new TopNPriorityQueue(3);
  }

  @Override
  public void reduce(Text key, Iterable<IntWritable> values, Context context) {
    Integer count = 0;
    for (IntWritable value : values){
      count += value.get();
    }
    top3.insert(key.toString() + ":" + count.toString());
  }

  @Override
  protected void cleanup(Context context) throws IOException, InterruptedException {
    while (top3.size() > 0){
      String[] monthAndCount = top3.pop().split(":");
      context.write(new Text(monthAndCount[0]), new IntWritable(Integer.valueOf(monthAndCount[1])));
    }
  }
}
