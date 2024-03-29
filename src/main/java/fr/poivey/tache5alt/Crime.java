package fr.poivey.tache5alt;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Crime {

  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      System.err.println("Usage : Crime <input path>");
      System.exit(-1);
    }

    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "Top 3 month for each crime");
    job.setJarByClass(Crime.class);

    FileInputFormat.addInputPath(job, new Path(args[0]));
    Path outputPath = new Path("./output/crime5alt");
    FileOutputFormat.setOutputPath(job, outputPath);
    outputPath.getFileSystem(conf).delete(outputPath,true);

    job.setMapperClass(CrimeMapper.class);
    job.setReducerClass(CrimeReducer.class);

    job.setMapOutputValueClass(Text.class);

    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);

    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
