package fr.poivey.tache1;

import java.util.HashMap;
import java.util.Map;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.stream.StreamSupport;

public class CrimeReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

  private Map<Text, Integer> crimeMap;

  @Override
  public void setup(Context context) {
    crimeMap = new HashMap<>();
  }

  @Override
  public void reduce(Text key, Iterable<IntWritable> values, Context context) {
    int count = StreamSupport.stream(values.spliterator(), false)
            .mapToInt(IntWritable::get)
            .sum();
    crimeMap.put(new Text(key), count);
  }

  @Override
  public void cleanup(Context context) {
    crimeMap.entrySet().stream()
        .sorted(Map.Entry.<Text, Integer>comparingByValue().reversed())
        .forEach(entry -> {
          try {
            context.write(entry.getKey(), new IntWritable(entry.getValue()));
          } catch (IOException | InterruptedException e) {
            e.printStackTrace();
          }
        });
  }

}
