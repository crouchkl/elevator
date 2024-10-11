package elevatorApp;

import java.util.ArrayList;

import elevatorApp.button.FloorButton;

public class Floor {
	private Integer floorNumber = 0;
	private ArrayList<FloorButton> buttons = new ArrayList<FloorButton>();
	private Integer callDirection = 0;
	
	public Floor(boolean top, boolean bottom, Integer floorNumber) {
		this.buttons = new ArrayList<FloorButton>();
		if (!top) {
			FloorButton button = new FloorButton(floorNumber);
			button.setDirection(0);
			this.buttons.add(button);
		}
		if (!bottom) {
			FloorButton button = new FloorButton(floorNumber);
			button.setDirection(1);
			this.buttons.add(button);
		}
		
		this.floorNumber = floorNumber;
	}

	public ArrayList<FloorButton> getButtons() {
		return buttons;
	}
	
	public Integer getPrintableFloorNumber() {
		return floorNumber + 1;
	}

	public Integer getFloorNumber() {
		return floorNumber;
	}

	public void setFloorNumber(Integer floorNumber) {
		this.floorNumber = floorNumber;
	}

	public Integer getCallDirection() {
		return callDirection;
	}

	public void setCallDirection(Integer callDirection) {
		this.callDirection = callDirection;
	}
}
