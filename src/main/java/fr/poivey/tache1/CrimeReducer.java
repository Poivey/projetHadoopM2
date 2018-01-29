package fr.poivey.tache1;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.stream.StreamSupport;

public class CrimeReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

  @Override
  public void reduce(Text key, Iterable<IntWritable> values, Context context) throws InterruptedException, IOException {
    int count = StreamSupport.stream(values.spliterator(), false)
        .mapToInt(IntWritable::get)
        .sum();
    context.write(key, new IntWritable(count));
  }

}
