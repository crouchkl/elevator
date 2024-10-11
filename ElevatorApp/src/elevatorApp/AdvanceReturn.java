package elevatorApp;

/**
 * Used as the return value for the advanceElevator() method
 * in the Elevator object
 */

public class AdvanceReturn {
	// set to true if the elevator will pause on the floor due to a button press
	private boolean pause = false;
	
	private Integer floor = 0;
	
	// 0 - up; 1 - down 
	private Integer direction = 0;

	public boolean isPause() {
		return pause;
	}
	
	public void setPause(boolean pause) {
		this.pause = pause;
	}
	
	public Integer getFloor() {
		return floor;
	}
	
	public void setFloor(Integer floor) {
		this.floor = floor;
	}
	
	public Integer getDirection() {
		return direction;
	}
	
	public void setDirection(Integer direction) {
		this.direction = direction;
	}
}
