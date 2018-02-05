package fr.poivey.tache2;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class CrimeMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

  private Text[] hourFilter;

  @Override
  public void setup(Context context) {
    hourFilter = new Text[]{new Text("0-4"), new Text("4-8"), new Text("8-12"), new Text("12-16"),
        new Text("16-20"), new Text("20-24")};
  }

  @Override
  public void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {

    if (key.get() != 0L) {

      String[] ligne = value.toString().split(",");
      int hour =
          Integer.valueOf(ligne[2].split(" ")[1].split(":")[0]) % 12; //Midnight and noon become 0
      if (ligne[2].split(" ")[2].toLowerCase().equals("pm")) {
        hour += 12;
      }

      context.write(hourFilter[hour / 4], new IntWritable(1));
    }

  }

}
