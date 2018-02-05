package fr.poivey.tache5alt;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CrimeMapper extends Mapper<LongWritable, Text, Text, Text> {
  Map<String, Integer> mapCrimeMonth;

  @Override
  protected void setup(Context context){
    mapCrimeMonth = new HashMap<>();
  }

  @Override
  public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
    if (key.get() != 0L) {
      String[] ligne = value.toString().split(",");

      String crime = ligne[5].toLowerCase();
      String month = ligne[2].substring(0,2);

      String mapKey = crime + ":" + month;

      int count = mapCrimeMonth.getOrDefault(mapKey, 0) + 1;
      mapCrimeMonth.put(mapKey, count);
    }
  }

  @Override
  protected void cleanup(Context context) throws IOException, InterruptedException {
    for (Map.Entry<String, Integer> entry: mapCrimeMonth.entrySet()) {
      String[] splitMapKey = entry.getKey().split(":");
      Text crime = new Text(splitMapKey[0]);
      Text monthAndCount = new Text(splitMapKey[1] + ":" + entry.getValue());

      context.write(crime, monthAndCount);
    }
  }
}
