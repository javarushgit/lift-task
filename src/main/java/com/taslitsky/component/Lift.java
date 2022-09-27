package com.taslitsky.component;

import com.taslitsky.reader.LiftPropertiesReader;

import java.util.ArrayList;
import java.util.List;

public class Lift {
  private final int maxCapacity;
  private int currentFloor;
  private List<Person> people;

  public Lift() {
    LiftPropertiesReader propertiesReader = new LiftPropertiesReader();
    maxCapacity = propertiesReader.getMaxCapacity();
    currentFloor = propertiesReader.getStartFloor();
    people =  new ArrayList<>(maxCapacity);
  }

  public int getCurrentFloor() {
    return currentFloor;
  }

  public void setCurrentFloor(int currentFloor) {
    this.currentFloor = currentFloor;
  }

  public int getMaxCapacity() {
    return maxCapacity;
  }

  public List<Person> getPeople() {
    return people;
  }

  public boolean isFull() {
    return people.size() == maxCapacity;
  }

  public boolean isEmpty() {
    return people.isEmpty();
  }
}
