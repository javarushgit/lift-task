package com.taslitsky.component;

import java.util.Random;

public class Person {
  private Floor floor;
  private int currentFloor;
  private int neededFloor;
  private Direction direction;

  public int getCurrentFloor() {
    return currentFloor;
  }

  public Person(Floor floor) {
    this.floor = floor;
    this.currentFloor = floor.getFloorId();
    generateNeededFloor();
    direction = currentFloor < neededFloor ? Direction.UP : Direction.DOWN;
  }

  private void generateNeededFloor() {
    Random random = new Random();
    // чтобы не создавался этаж тот, на котором мы уже находимся
    do {
      // радомайзер этажа куда нужно человеку
      neededFloor = 1 + random.nextInt(floor.getBuilding().getFloorCount());
    } while (neededFloor == currentFloor);
  }

  public int getNeededFloor() {
    return neededFloor;
  }

  public Direction getDirection() {
    return direction;
  }

}
