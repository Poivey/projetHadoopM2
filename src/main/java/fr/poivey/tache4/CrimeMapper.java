package fr.poivey.tache4;

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

      String arrest = ligne[ligne.length - 15].toLowerCase();
      String latitude = ligne[ligne.length - 4].toLowerCase();
      String longitude = ligne[ligne.length - 3].toLowerCase();
      String location = latitude + " " + longitude;

      if (!location.trim().equals("") && (arrest.equals("true") || arrest.equals("false"))){
        context.write(new Text(location + " " + arrest), new IntWritable(1));
      }
    }
  }
}
