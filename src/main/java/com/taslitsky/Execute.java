package com.taslitsky;

import com.taslitsky.component.Building;
import com.taslitsky.component.Lift;

public class Execute {
  public static void main(String[] args) {
    Building building = new Building();
    Lift lift = new Lift();
    LiftManager liftManager = new LiftManager(lift, building, new Menu(building.getFloorCount(), lift.getMaxCapacity()));
    liftManager.run();
  }
}
