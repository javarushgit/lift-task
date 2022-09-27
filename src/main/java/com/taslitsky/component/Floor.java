package com.taslitsky.component;


import com.taslitsky.reader.FloorPropertiesReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Floor {
  private Building building;
  private List<Person> queue;
  private final int floorId;

  public Floor(Building building, int floorId) {
    this.building = building;
    this.floorId = ++floorId;
    generateQueue();
  }

  public Building getBuilding() {
    return building;
  }

  public List<Person> getQueue() {
    return queue;
  }

  public int getFloorId() {
    return floorId;
  }
  private void generateQueue() {
    Random random = new Random();
    FloorPropertiesReader propertiesReader = new FloorPropertiesReader();
    int floorMinQueue = propertiesReader.getMinQueue();
    int floorMaxQueue = propertiesReader.getMaxQueue();
    int queueCount = floorMinQueue + random.nextInt(floorMaxQueue-floorMinQueue + 1);
    queue = new ArrayList<>(queueCount);

    for (int i = 0; i < queueCount; i++) {
      queue.add(new Person(this));
    }
  }
}
