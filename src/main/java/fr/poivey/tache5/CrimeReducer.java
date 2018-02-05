package fr.poivey.tache5;

import fr.poivey.utility.TopNPriorityQueue;
import java.io.IOException;
import java.util.PriorityQueue;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class CrimeReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

  private PriorityQueue top3;

  @Override
  protected void setup(Context context) throws IOException, InterruptedException {
    top3 = new PriorityQueue(3);
  }

  @Override
  public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
    System.out.println(key.toString());
    for (IntWritable value : values) {
      System.out.println(value.toString());
      top3.insert(key.toString() + ":" + value.toString());
    }
  }

  @Override
  protected void cleanup(Context context) throws IOException, InterruptedException {
    while (top3.size() > 0){
      String[] monthAndCount = top3.pop().split(":");
      context.write(new Text(monthAndCount[0]), new IntWritable(Integer.valueOf(monthAndCount[1])));
    }
  }
}
