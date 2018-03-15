package fr.poivey.tache3;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.List;

public class CrimeMapperCentro extends Mapper<LongWritable, Text, IntWritable, Text> {

  public static List<Double[]> centroids;

  @Override
  protected void setup(Context context) throws IOException {
    centroids = Utility.readCentroids(context.getCacheFiles()[0].toString());
  }

  @Override
  public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
    if (key.get() != 0L) {
      String[] ligne = value.toString().split(",");
      String latitude = ligne[ligne.length - 4];
      String longitude = ligne[ligne.length - 3];

      if (!latitude.trim().equals("") && !longitude.trim().equals("")){

        double latValue = Double.parseDouble(ligne[ligne.length - 4]);
        double longValue = Double.parseDouble(ligne[ligne.length - 3]);
        double minDistance = Double.MAX_VALUE;
        int indexCluster = 0;
        for (int i = 0; i < centroids.size(); i++) {
          double distance = Math.sqrt(Math.pow(centroids.get(i)[0] - latValue, 2) + Math.pow(centroids.get(i)[1] - longValue, 2));
          if (distance < minDistance) {
            indexCluster = i;
            minDistance = distance;
          }
        }

        context.write(new IntWritable(indexCluster), new Text(latitude + " " + longitude));
      }
    }
  }
}
