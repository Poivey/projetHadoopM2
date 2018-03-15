package fr.poivey.tache3;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CrimeMapperTop extends Mapper<LongWritable, Text, Text, IntWritable> {

  @Override
  public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
    if (key.get() != 0L) {
      String[] ligne = value.toString().split("\t")[1].split(" ");
      String latitude = ligne[0];
      String longitude = ligne[1];
      String count = ligne[2];

      context.write(new Text(latitude + " " + longitude), new IntWritable(Integer.parseInt(count)));
    }
  }
}
