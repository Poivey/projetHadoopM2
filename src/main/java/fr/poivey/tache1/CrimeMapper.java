package fr.poivey.tache1;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class CrimeMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

  @Override
  public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
    if (key.get() != 0L) {
      String[] ligne = value.toString().split(",");

      String crime = ligne[5].toLowerCase();

      context.write(new Text(crime), new IntWritable(1));
    }
  }
}
