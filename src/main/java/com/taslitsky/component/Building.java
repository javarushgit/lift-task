package com.taslitsky.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.taslitsky.reader.BuildingPropertiesReader;

public class Building {
  private final int floorCount;
  private List<Floor> floorList;

  public Building(){
    BuildingPropertiesReader propertiesReader = new BuildingPropertiesReader();
    int minFloor = propertiesReader.getMinFloor();
    int maxFloor = propertiesReader.getMaxFloor();
    Random random = new Random();
    floorCount = minFloor + random.nextInt(maxFloor-minFloor+1);
    floorList = new ArrayList<>(floorCount);
    for (int i = 0; i < floorCount; i++) {
      System.out.println("hello");
      floorList.add(new Floor(this,i));
    }
  }

  public int getFloorCount() {
    return floorCount;
  }

  public List<Floor> getFloorList() {
    return floorList;
  }

  public Optional<Floor> getFloorById(int id) {
    for (Floor floor : floorList) {
      if(floor.getFloorId() == id) {
        return Optional.of(floor);
      }
    }
    return Optional.empty();
  }

}
