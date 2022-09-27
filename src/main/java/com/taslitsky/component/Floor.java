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
    System.out.println("hi");
    this.building = building;
    this.floorId = ++floorId;
    System.out.println("1");
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
    System.out.println("2");
    int floorMinQueue = propertiesReader.getMinQueue();
    int floorMaxQueue = propertiesReader.getMaxQueue();
    System.out.println("3");
    int queueCount = floorMinQueue + random.nextInt(floorMaxQueue-floorMinQueue + 1);
    System.out.println("4");
    queue = new ArrayList<>(queueCount);

    for (int i = 0; i < queueCount; i++) {
      System.out.println("4,5");
      queue.add(new Person(this));
    }
    System.out.println("5");
  }
}
