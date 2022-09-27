package com.taslitsky.reader;

public class BuildingPropertiesReader extends PropertiesReader {
  private final int maxFloor;
  private final int minFloor;

  public BuildingPropertiesReader() {
    minFloor = Integer.parseInt(properties.getProperty("building.floor_min"));
    maxFloor = Integer.parseInt(properties.getProperty("building.floor_max"));
  }

  public int getMaxFloor() {
    return maxFloor;
  }

  public int getMinFloor() {
    return minFloor;
  }
}
