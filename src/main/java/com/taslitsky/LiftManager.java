package com.taslitsky;


import com.taslitsky.component.Building;
import com.taslitsky.component.Direction;
import com.taslitsky.component.Floor;
import com.taslitsky.component.Lift;
import com.taslitsky.component.Person;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

import java.util.Comparator;
import java.util.stream.Collectors;

public class LiftManager {
  private Lift lift;
  private Building building;
  private Menu menu;
  private Direction currentDirection;
  // Use Set, because I will just need to remove elements from container, so it's faster than same at List
  private Set<Person> upPeople;
  private Set<Person> downPeople;
  private List<Boolean> activeFloor;

  public LiftManager(Lift lift, Building building, Menu menu) {
    this.lift = lift;
    this.building = building;
    this.menu = menu;
    upPeople = new HashSet<>();
    downPeople = new HashSet<>();
    activeFloor = new ArrayList<>();
    currentDirection = Direction.DOWN;

    for (Floor floor : building.getFloorList()) {
      List<Person> floorQueue = floor.getQueue();

      // Add people to lists who go up and down
      for(int i = 0; i < floor.getQueue().size(); i++) {
        if(floorQueue.get(i).getDirection() == Direction.UP) {
          upPeople.add(floorQueue.get(i));
        }
        else {
          downPeople.add(floorQueue.get(i));
        }
      }

      // Set floors where people wait lift
      if(floor.getQueue().isEmpty()) {
        activeFloor.add(false);
      }
      else {
        activeFloor.add(true);
      }
    }
  }

  public void run() {
    menu.doStep(building, lift, currentDirection);
    while (activeFloor.stream().anyMatch(floor -> floor) || !lift.isEmpty()) {
      action();
    }
    menu.printToConsole();
    menu.printToFile();
  }

  private void action() {
    // Whether to change the direction of the elevator
    if(shouldChangeDirection()) {
      changeDirection();
    }
    loadPeopleIfShould(); //Load people if there are places
    goToNeededFloor();   // Go further
    unLoadPeople();   // Unload people to floor
  }

  private void loadPeopleIfShould() {
    if(!lift.isFull()) {  // Whether there free space in lift
      loadPeople(); // Load the quantity that fits
      // If there are no more people on the floor -> unpress the elevator wait button
      if(building.getFloorById(lift.getCurrentFloor()).get().getQueue().isEmpty()) {
        activeFloor.set(lift.getCurrentFloor()-1, false);
      }
    }
  }

  private void loadPeople() {
    // Get companions who are going up
    List<Person> companionsUp = building.getFloorList().get(lift.getCurrentFloor()-1).getQueue().stream()
        .filter(person -> person.getNeededFloor() > lift.getCurrentFloor())
        .sorted((o1, o2) -> o2.getNeededFloor()-o1.getNeededFloor())
        .collect(Collectors.toList());

    // Get companions who are going down
    List<Person> companionsDown = building.getFloorList().get(lift.getCurrentFloor()-1).getQueue().stream()
        .filter(person -> person.getNeededFloor() < lift.getCurrentFloor())
        .sorted(Comparator.comparingInt(Person::getNeededFloor))
        .collect(Collectors.toList());

    List<Person> companions;

    // Decide where to go
    if(lift.isEmpty()) {
      companions = companionsUp.size() > companionsDown.size() ? companionsUp : companionsDown;
    } else {
      companions = currentDirection == Direction.UP ? companionsUp : companionsDown;
    }

    if(companions.isEmpty()) {
      return;
    }

    // Get only the number of companions that fit in the lift
    companions = companions.stream().limit(lift.getMaxCapacity() - lift.getPeople().size())
        .collect(Collectors.toList());

    // Remove people who entered the lift from the floor
    building.getFloorList().get(lift.getCurrentFloor()-1).getQueue().removeAll(companions);

    // Get the number of passengers in the lift, before the arrival of new companions
    int liftSize = lift.getPeople().size();

    // Add them to our lift in sorted order.
    lift.getPeople().addAll(companions);

    // Sort if the lift was empty
    if(liftSize == 0) {
      sortPeople(lift.getPeople());
    }
    menu.doStep(building,lift, currentDirection);
  }

  private void unLoadPeople() {
    // Get a list of people who need to go out on this floor
    List<Person> people = lift.getPeople().stream()
        .filter(person -> person.getNeededFloor() == lift.getCurrentFloor())
        .collect(Collectors.toList());

    // Remove them from the lift
    if(!lift.getPeople().removeAll(people)) {
      return;
    }

    removePeople(people);

    menu.getGone().set(lift.getCurrentFloor()-1, menu.getGone().get(lift.getCurrentFloor()-1) + people.size());
    menu.doStep(building,lift, currentDirection);
  }

  private void removePeople(List<Person> peopleToRemove) {
    if(upPeople.removeAll(peopleToRemove)) {}
    else {
      downPeople.removeAll(peopleToRemove);
    }
  }

  private int getNeededFloor() {
    return  lift.getPeople().get(0).getNeededFloor();
  }

  private void changeDirection() {
    currentDirection = (currentDirection == Direction.UP) ? Direction.DOWN : Direction.UP;
  }

  private void goToNeededFloor() {
    int neededFloorId = lift.isEmpty() ? getClosestFloor() : getNeededFloor();
    if(shouldChangeDirection(neededFloorId)) {
      changeDirection();
    }
    while (lift.getCurrentFloor() != neededFloorId) {
      goByDirection();
      unLoadPeople();
      loadPeopleIfShould();
    }
  }

  private boolean shouldChangeDirection() {
    if(lift.getCurrentFloor() == building.getFloorCount() || lift.getCurrentFloor() == 1) {
      return true;
    } else if(upPeople.size() == 0 && downPeople.size() != 0) {
      return downPeople.stream().max(Comparator.comparingInt(Person::getCurrentFloor)).get()
          .getCurrentFloor() < lift.getCurrentFloor();
    } else if (upPeople.size() !=0 && downPeople.size() == 0 ) {
      return upPeople.stream().min(Comparator.comparingInt(Person::getCurrentFloor)).get()
          .getCurrentFloor() > lift.getCurrentFloor();
    } else {
      return false;
    }
  }

  private boolean shouldChangeDirection(int floor) {
    if(currentDirection == Direction.UP && floor < lift.getCurrentFloor()) {
      return true;
    } else {
      return currentDirection == Direction.DOWN && floor > lift.getCurrentFloor();
    }
  }

  private void sortPeople(List<Person> people) {
    if(currentDirection == Direction.DOWN) {
      people.sort(Comparator.comparingInt(Person::getCurrentFloor));
    } else {
      people.sort((o1, o2) -> o2.getCurrentFloor()-o1.getCurrentFloor());
    }
  }

  private int getClosestFloor() {
    int closestFloor = building.getFloorCount();
    for (int i = 0; i < activeFloor.size(); i++) {
      if(activeFloor.get(i)) {
        if(Math.abs(lift.getCurrentFloor() - (i + 1)) < closestFloor) {
          closestFloor = i+1;
        }
      }
    }
    return closestFloor;
  }

  private void goByDirection() {
    int directionCounter = currentDirection == Direction.UP ? 1 : -1;
    lift.setCurrentFloor(lift.getCurrentFloor() + directionCounter);
  }
}
