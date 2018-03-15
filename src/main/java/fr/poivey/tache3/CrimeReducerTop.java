package fr.poivey.tache3;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CrimeReducerTop extends Reducer<Text, IntWritable, Text, IntWritable> {

  private Map<String, Integer> unsortedMap;

  @Override
  public void setup(Context context) {
    this.unsortedMap = new HashMap<>();
  }

  @Override
  public void reduce(Text key, Iterable<IntWritable> values, Context context) {
    values.forEach(value -> unsortedMap.put(key.toString(), value.get()));
  }

  @Override
  public void cleanup(Context context) throws IOException, InterruptedException {
    Map<String, Integer> result = unsortedMap.entrySet().stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                    (oldValue, newValue) -> oldValue, LinkedHashMap::new));

    Set<Map.Entry<String, Integer>> resultSet = result.entrySet();
    int i = 0;
    for (Map.Entry<String, Integer> entry : resultSet) {
      /* top 3 */
      if (i < 3) {
        context.write(new Text(entry.getKey()), new IntWritable(entry.getValue()));
      }
      /* worst 3 */
      if (i >= resultSet.size() - 3){
        context.write(new Text(entry.getKey()), new IntWritable(entry.getValue()));
      }
      i += 1;
    }
  }
}
