package fr.poivey.utility;


import org.apache.hadoop.util.PriorityQueue;

public class TopNPriorityQueue extends PriorityQueue<String> {

  public TopNPriorityQueue(int n) {
    initialize(n);
  }

  @Override
  protected boolean lessThan(Object o1, Object o2) {
    return lessThan((String)o1, (String)o2);
  }

  protected boolean lessThan(String s1, String s2) {
    int first = Integer.valueOf(s1.split(":")[1]);
    int sec = Integer.valueOf(s2.split(":")[1]);
    return first < sec;
  }
}