package fr.poivey.tache1.utility;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

public class CrimeAndCount {
  private Text crime;
  private IntWritable count;

  public CrimeAndCount(Text crime, IntWritable count) {
    this.crime = crime;
    this.count = count;
  }

  public Text getCrime() {
    return crime;
  }

  public IntWritable getCount() {
    return count;
  }
}
