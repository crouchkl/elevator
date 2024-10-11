package elevatorApp.button;

public class FloorButton extends Button {
	private Integer direction = 0;

	public FloorButton(Integer floor) {
		super(floor);
	}

	public Integer getDirection() {
		return direction;
	}

	public void setDirection(Integer direction) {
		this.direction = direction;
	}
	
	public String getTitle() {
		return direction == 0 ? "UP" : "DOWN";
	}
}
