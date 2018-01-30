package fr.poivey.tache5.utility;

import org.apache.hadoop.util.PriorityQueue;

public class TopNPriorityQueue extends PriorityQueue<String> {

  public TopNPriorityQueue() {
    initialize(3);
  }

  @Override
  protected boolean lessThan(Object o, Object o1) {
    int first = Integer.valueOf(((String) o).split(":")[1]);
    int sec = Integer.valueOf(((String) o1).split(":")[1]);
    return first > sec;
  }
}