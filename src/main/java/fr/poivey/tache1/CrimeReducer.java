package fr.poivey.tache1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
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
    List<Entry> sortedList = new ArrayList(crimeMap.entrySet());
    sortedList.sort((Entry e1, Entry e2) -> ((Integer)e2.getValue()) - ((Integer)e1.getValue()));
    sortedList.forEach(entry -> {
      try {
        context.write((Text)entry.getKey(), new IntWritable((Integer)entry.getValue()));
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }
    });
  }

}
