package com.taslitsky.reader;

public class LiftPropertiesReader extends PropertiesReader {
  private final int maxCapacity;
  private final int startFloor;

  public LiftPropertiesReader() {
    maxCapacity = Integer.parseInt(properties.getProperty("lift.max_capacity"));
    startFloor = Integer.parseInt(properties.getProperty("lift.start_floor"));
  }

  public int getMaxCapacity() {
    return maxCapacity;
  }

  public int getStartFloor() {
    return startFloor;
  }
}
