package fr.poivey.tache1;

import fr.poivey.tache1.utility.CrimeAndCount;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

public class CrimeReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

  private List<CrimeAndCount> crimeList;

  @Override
  public void setup(Context context) {
    crimeList = new ArrayList();
  }

  @Override
  public void reduce(Text key, Iterable<IntWritable> values, Context context) {
    int count = StreamSupport.stream(values.spliterator(), false)
            .mapToInt(IntWritable::get)
            .sum();
    crimeList.add(new CrimeAndCount(new Text(key), new IntWritable(count)));
  }

  @Override
  public void cleanup(Context context) {
    crimeList.sort((CrimeAndCount c1, CrimeAndCount c2) -> c2.getCount().get() - c1.getCount().get());
    crimeList.forEach(cAndC -> {
      try {
        context.write(cAndC.getCrime(), cAndC.getCount());
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }
    });
  }

}
