package elevatorApp.button;

public class Button {
	private String title = "";
	private boolean lit = false;
	private Integer floor;
	
	public Button(Integer floor) {
		this.setFloor(floor);
		this.title = Integer.toString(floor + 1);
	}
	
	public void buttonPress() {
		this.lit = true;
	}
	
	public void floorReached() {
		this.lit = false;
	}
	
	public boolean isButtonLit() {
		return lit;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getFloor() {
		return floor;
	}

	public void setFloor(Integer floor) {
		this.floor = floor;
	}
}
