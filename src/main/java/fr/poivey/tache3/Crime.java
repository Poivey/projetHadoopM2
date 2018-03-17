package fr.poivey.tache3;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Crime {

  public static void main(String[] args) throws Exception {
    if (args.length != 2) {
      System.err.println("Usage : Crime <input path> <centroids>");
      System.exit(-1);
    }

    String centroidFileTemp = args[1] + ".temp";
    Files.write(Paths.get(centroidFileTemp), "".getBytes());

    Utility.replaceFileContent(centroidFileTemp, args[1]);

    boolean hasConverged = false;
    int iteration = 0;
    while (!hasConverged && iteration < 25){

      // executes hadoop job
      if (!startJobCreationCentroid(args[0], centroidFileTemp)) {
        // if an error has occurred stops iteration and terminates
        System.exit(-1);
      }

      // if the output of the reducer equals the old one
      if (Utility.checkContentEquality(centroidFileTemp, "./output/crime3-centroids/part-r-00000")) {
        // it means that the iteration is finished
        hasConverged = true;
      }
      else {
        // writes the reducers output to distributed cache
        Utility.replaceFileContent(centroidFileTemp, "./output/crime3-centroids/part-r-00000");
      }

      iteration +=1;
      System.out.println("iteration : " + iteration);
    }

    Files.delete(Paths.get(centroidFileTemp));
    startJobTop3Worst3("./output/crime3-centroids/part-r-00000");
  }

  private static boolean startJobCreationCentroid(String data, String centroids) throws IOException, ClassNotFoundException, InterruptedException {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "top 3 of most and less dangerous zones");
    job.setJarByClass(Crime.class);

    FileInputFormat.addInputPath(job, new Path(data));
    Path outputPath = new Path("./output/crime3-centroids");
    FileOutputFormat.setOutputPath(job, outputPath);
    outputPath.getFileSystem(conf).delete(outputPath,true);

    job.addCacheFile(new Path(centroids).toUri());

    job.setMapperClass(CrimeMapperCentro.class);
    job.setReducerClass(CrimeReducerCentro.class);

    job.setOutputKeyClass(IntWritable.class);
    job.setOutputValueClass(Text.class);

    return job.waitForCompletion(true);
  }

  private static boolean startJobTop3Worst3(String data) throws IOException, ClassNotFoundException, InterruptedException {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "top 3 of most and less dangerous zones");
    job.setJarByClass(Crime.class);

    FileInputFormat.addInputPath(job, new Path(data));
    Path outputPath = new Path("./output/crime3-top");
    FileOutputFormat.setOutputPath(job, outputPath);
    outputPath.getFileSystem(conf).delete(outputPath,true);

    job.setMapperClass(CrimeMapperTop.class);
    job.setReducerClass(CrimeReducerTop.class);

    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);

    return job.waitForCompletion(true);
  }
}
