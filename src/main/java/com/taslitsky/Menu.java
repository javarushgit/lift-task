package com.taslitsky;

import com.taslitsky.component.Building;
import com.taslitsky.component.Direction;
import com.taslitsky.component.Floor;
import com.taslitsky.component.Lift;
import com.taslitsky.component.Person;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Optional;

public class Menu {
  private int stepId;
  private List<Integer> gone;
  private StringBuilder data;
  public Menu(int floorCount, int liftCapacity) {
    stepId = 0;
    gone = new ArrayList<>(Collections.nCopies(floorCount, 0));
    data =  new StringBuilder();
    data.append("Building: ").append(floorCount).append(" floors. Lift capacity: ")
        .append(liftCapacity).append('\n');
  }

  public void doStep(Building building, Lift lift, Direction direction) {
    data.append("******** STEP ").append(stepId++).append(" ********\n");
    for(int i = building.getFloorCount(); i > 0; i--) {
      data.append("[").append(i).append("]\t\t");
      data.append(gone.get(i - 1)).append(" |");
      if(i == lift.getCurrentFloor()) {
        data.append(getDirectionCar(direction)).append("\t");
        if(!lift.getPeople().isEmpty()) {
          lift.getPeople().forEach(person -> data.append(person.getNeededFloor()).append(" "));
        }
        data.append("\t").append(getDirectionCar(direction));
      }
      else {
        for(int j = 0; j < lift.getMaxCapacity()+3; j++) {
          data.append("\t");
        }
      }
      data.append("| ");

      Optional<Floor> optionalFloor = building.getFloorById(i);
      if(optionalFloor.isPresent()) {
        for (Person person : optionalFloor.get().getQueue()) {
          data.append(person.getNeededFloor()).append(" ");
        }
      }
      data.append("\n");
    }
    data.append("\n\n");
  }

  public void printToConsole() {
    System.out.println(data.toString());
  }

  public void printToFile() {
    DataWriter dataWriter = new DataWriter();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = new Date();
    String fileName = "output-" + formatter.format(date) + ".txt";
    dataWriter.writeData(data, fileName);
  }

  private char getDirectionCar(Direction direction) {
    return direction == Direction.UP ? '^' : 'v';
  }

  public List<Integer> getGone() {
    return gone;
  }
}
