package com.taslitsky.reader;

public class FloorPropertiesReader extends PropertiesReader {
  private final int minQueue;
  private final int maxQueue;

  public FloorPropertiesReader() {
    minQueue = Integer.parseInt(properties.getProperty("building.floor.queue_min"));
    maxQueue = Integer.parseInt(properties.getProperty("building.floor.queue_max"));
  }

  public int getMinQueue() {
    return minQueue;
  }

  public int getMaxQueue() {
    return maxQueue;
  }
}
