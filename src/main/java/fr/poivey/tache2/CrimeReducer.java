package fr.poivey.tache2;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

public class CrimeReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
  private Map<Text, Integer> hourCrimeMap;

  @Override
  public void setup(Context context) {
    this.hourCrimeMap = new HashMap<>();
  }

  @Override
  public void reduce(Text key, Iterable<IntWritable> values, Context context) {

    int count = StreamSupport.stream(values.spliterator(), false)
            .mapToInt(IntWritable::get)
            .sum();

    hourCrimeMap.put(new Text(key), count);
  }

  @Override
  public void cleanup(Context context){
    List<Text> keyList = new ArrayList(hourCrimeMap.keySet());
    keyList.sort((Text t1, Text t2) -> Integer.valueOf(t1.toString().split("-")[0]) - Integer.valueOf(t2.toString().split("-")[0]));
//    keyList.sort(Comparator.comparingInt(Text::toString)); TODO faire le comparator comme ceci
    keyList.forEach(key -> {
      try {
        context.write(key, new IntWritable(hourCrimeMap.get(key)));
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }
    });
  }

}
