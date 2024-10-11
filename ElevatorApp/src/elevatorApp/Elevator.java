package elevatorApp;

import java.util.ArrayList;
import java.util.Iterator;

import elevatorApp.button.ElevatorButton;
import elevatorApp.button.FloorButton;

/**
 * Class that defines the Elevator object 
 */

public class Elevator {
	private Integer elevatorNumber = 0;
	private Integer numFloors = 0;
	
	// Buttons that are found in the elevator object
	private ArrayList<ElevatorButton> elevatorButtons;
	// Floors that this elevator will stop at
	private ArrayList<Floor> floors;
	
	// Current floor that the elevator is on
	private Integer currentFloor = 0;
	
	// 0 - up; 1 - down 
	private Integer direction = 0;
	
	public Elevator(Integer elevatorNumber, Integer numFloors) {
		this.elevatorNumber = elevatorNumber;
		this.numFloors = numFloors;
		createButtons(numFloors);
	}

	// The elevator number is the index for an array, thus this is incremented for display
	public Integer getPrintableElevatorNumber() {
		return elevatorNumber + 1;
	}

	public Integer getElevatorNumber() {
		return elevatorNumber;
	}

	public void setElevatorNumber(Integer elevatorNumber) {
		this.elevatorNumber = elevatorNumber;
	}
	
	public Integer getNumFloors() {
		return numFloors;
	}

	public void setNumFloors(Integer numFloors) {
		this.numFloors = numFloors;
	}

	public ArrayList<ElevatorButton> getElevatorButtons() {
		return elevatorButtons;
	}

	public void setElevatorButtons(ArrayList<ElevatorButton> elevatorButtons) {
		this.elevatorButtons = elevatorButtons;
	}
	
	public ArrayList<Floor> getFloors() {
		return floors;
	}

	public Integer getCurrentFloor() {
		return currentFloor;
	}

	public void setCurrentFloor(Integer currentFloor) {
		this.currentFloor = currentFloor;
	}
	
	public Integer getDirection() {
		return direction;
	}
	
	public void setDirection(Integer direction) {
		this.direction = direction;
	}
	
	// On creation of the elevator, buttons will be created
	// for both the elevator.
	// The floors that the elevator visits will also be created.
	private void createButtons(Integer numFloors) {
		this.elevatorButtons = new ArrayList<ElevatorButton>();
		this.floors = new ArrayList<Floor>();
		
		for (int i = 0; i < numFloors; i++) {
			ElevatorButton button = new ElevatorButton(i);
			elevatorButtons.add(button);
			
			Floor floor = new Floor((i == numFloors - 1), (i == 0), i);
			floors.add(floor);
		}
	}
	
	// When a button within the elevator is pressed, it will be set to 'lit'
	// and the button on the floor that corresponds to the current direction
	// will be 'lit'.
	// If a floor only has one button, then it will be 'lit'.
	public void elevatorButtonPressed(ElevatorButton button) {
		int index = elevatorButtons.indexOf(button);
		elevatorButtons.get(index).buttonPress();
		
		Floor floor = floors.get(button.getFloor());
		if (floor.getButtons().size() > 1) {
			for (int i = 0; i < floor.getButtons().size(); i++) {
				if (this.direction == 0 && floor.getButtons().get(i).getDirection() == 0) {
					floor.getButtons().get(i).buttonPress();
				}
				if (this.direction == 1 && floor.getButtons().get(i).getDirection() == 1) {
					floor.getButtons().get(i).buttonPress();
				}
			}
		}
		else {
			floor.getButtons().get(0).buttonPress();
		}
	}

	// When a floor button is pressed, it will be set to 'lit' and the corresponding
	// button in the elevator will be 'lit'.
	public void floorButtonPressed(Floor floor, FloorButton button) {
		int floorIndex = floors.indexOf(floor);
		Floor flr = floors.get(floorIndex);
		
		int index = flr.getButtons().indexOf(button);
		flr.getButtons().get(index).buttonPress();
		
		ElevatorButton elevatorButton = elevatorButtons.get(button.getFloor());
		elevatorButton.buttonPress();
	}
	
	// This method is called at intervals to move the elevator to the next floor.
	public AdvanceReturn advanceElevator() {
		// Determine if any buttons have been pressed. If not, the elevator will
		// stay at the current floor.
		boolean advanceFloor = false;
		for (Iterator<ElevatorButton> iter = elevatorButtons.iterator(); iter.hasNext();) {
			ElevatorButton eButton = iter.next();
			if (eButton.isButtonLit()) {
				advanceFloor = true;
				break;
			}
		}
		
		AdvanceReturn ret = new AdvanceReturn();

		// If the elevator will advance to another floor, base the movement on
		// the current direction of the elevator.
		if (advanceFloor) {
			if (direction == 0) {
				// If there are no floors above the current floor that have
				// buttons 'lit', the elevator will reverse direction.
				boolean changeDirection = true;
				for (int i = currentFloor + 1; i < elevatorButtons.size(); i++) {
					if (elevatorButtons.get(i).isButtonLit()) {
						changeDirection = false;
						break;
					}
				}
				
				// Advance to the next floor if there are still floors above
				// the current floor that have buttons 'lit'. Otherwise set the
				// direction to DOWN = 1
				if (currentFloor < floors.size() - 1 && !changeDirection) {
					currentFloor = currentFloor + 1;
				}
				else {
					setDirection(1);
				}
			}
			else {
				// If there are no floors below the current floor that have
				// buttons 'lit', the elevator will reverse direction.
				boolean changeDirection = true;
				for (int i = currentFloor - 1; i >= 0; i--) {
					if (elevatorButtons.get(i).isButtonLit()) {
						changeDirection = false;
						break;
					}
				}
				
				// Advance to the next floor if there are still floors below
				// the current floor that have buttons 'lit'. Otherwise set the
				// direction to UP = 0
				if (currentFloor > 0 && !changeDirection) {
					currentFloor = currentFloor - 1;
				}
				else {
					setDirection(0);
				}
			}
			
			// Set the return values for the direction and current floor
			ret.setDirection(direction);
			ret.setFloor(currentFloor);

			// Grab the current floor for updates
			Floor floor = floors.get(currentFloor);
			// Loop through the buttons on the floors and set the 'lit' to false
			// if the elevator is stopping at this floor. The elevator will stop
			// if the direction of the elevator movement is the same as the
			// direction button on the floor and the button is 'lit'.
			for (int i = 0; i < floor.getButtons().size(); i++) {
				if (this.direction == 0
						&& floor.getButtons().get(i).getDirection() == 0
						&& floor.getButtons().get(i).isButtonLit()) {
					floor.getButtons().get(i).floorReached();
					getElevatorButtons().get(currentFloor).floorReached();
					ret.setPause(true);
				}
				if (this.direction == 1
						&& floor.getButtons().get(i).getDirection() == 1
						&& floor.getButtons().get(i).isButtonLit()) {
					floor.getButtons().get(i).floorReached();
					getElevatorButtons().get(currentFloor).floorReached();
					ret.setPause(true);
				}
			}
		}
		return ret;
	}
}
