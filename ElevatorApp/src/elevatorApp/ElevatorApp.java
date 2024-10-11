package elevatorApp;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import elevatorApp.button.ElevatorButton;
import elevatorApp.button.FloorButton;

public class ElevatorApp {

	private JFrame frame;

	private ArrayList<ButtonGroup> eButtonGroup = new ArrayList<ButtonGroup>();
	private ArrayList<ArrayList<ButtonGroup>> fButtonGroup = new ArrayList<ArrayList<ButtonGroup>>();
	
	private Object[] columns = {"Elevator Number", "Floors"};
	
	private List<Elevator> elevators = Arrays.asList(
			new Elevator(0, 3),
			new Elevator(2, 12),
			new Elevator(3, 7),
			new Elevator(4, 9));
	
	private Elevator elevator;
	
	private Timer timer = new Timer();


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ElevatorApp window = new ElevatorApp();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ElevatorApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 346);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
		JTable elevatorTable = new JTable(tableModel);
		
		for (Iterator<Elevator> iter = elevators.iterator(); iter.hasNext();) {
			Elevator elevator = iter.next();
			
			Object[] data = {elevator.getPrintableElevatorNumber(), elevator.getNumFloors()};
			tableModel.addRow(data);
		}
		
		elevatorTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1 && elevatorTable.getSelectedRow() != -1) {
					Elevator elevator = elevators.get(elevatorTable.getSelectedRow());
					
					openElevator(elevator);
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(elevatorTable);
		scrollPane.setBounds(6, 6, 438, 260);
		frame.getContentPane().add(scrollPane);
	}
	
	private void openElevator(Elevator elevator) {
		this.elevator = elevator;
		
		frame = new JFrame("Elevator #" + elevator.getPrintableElevatorNumber());
		frame.setBounds(100, 100, 600, 500);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		
		JPanel elevatorPanel = new JPanel();

		elevatorPanel.setBounds(6, 6, 150, 400);
		elevatorPanel.setLayout(null);

		int row = 50;
		int col = 40;
		
		ArrayList<ElevatorButton> elevatorButtons = elevator.getElevatorButtons();
		for (ListIterator<ElevatorButton> iter = elevatorButtons.listIterator(elevatorButtons.size()); iter.hasPrevious();) {
			ElevatorButton button = iter.previous();
			
			JRadioButton elevatorButton = new JRadioButton(button.getTitle());
			elevatorButton.setBounds(col, row, 71, 23);
			elevatorButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					elevatorButtonClicked(button);
				}
			});
			
			if (col > 40) {
				col = 40;
				row += 28;
			}
			else {
				col += 100;
			}
			
			ButtonGroup bg = new ButtonGroup();
			bg.add(elevatorButton);
			eButtonGroup.add(bg);
			
			elevatorPanel.add(elevatorButton);
		}

		JScrollPane elevatorScrollPane = new JScrollPane(elevatorPanel);
		elevatorScrollPane.setBounds(6, 6, 250, 460);

		frame.getContentPane().add(elevatorScrollPane);
		
		JPanel floorPanel = new JPanel();
		floorPanel.setBounds(6, 6, 150, 400);
		floorPanel.setLayout(null);
		
		row = 50;
		
		ArrayList<Floor> floors = elevator.getFloors();
		for (ListIterator<Floor> iter = floors.listIterator(floors.size()); iter.hasPrevious();) {
			Floor floor = iter.previous();
			
			JLabel lblFloor = new JLabel(Integer.toString(floor.getPrintableFloorNumber()));
			lblFloor.setBounds(6, row + 5, 61, 16);

			col = 40;
			
			ArrayList<ButtonGroup> floorButtonGroup = new ArrayList<ButtonGroup>();
			
			for (Iterator<FloorButton> buttonIter = floor.getButtons().iterator(); buttonIter.hasNext();) {
				FloorButton button = buttonIter.next();
				
				JRadioButton floorButton = new JRadioButton(button.getTitle());
				floorButton.setBounds(col, row, 80, 23);
				floorButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						floorButtonClicked(floor, button);
					}
				});
				
				col += 100;
				
				ButtonGroup bg = new ButtonGroup();
				bg.add(floorButton);
				floorButtonGroup.add(bg);
				
				floorPanel.add(floorButton);
			}
			
			fButtonGroup.add(floorButtonGroup);
			
			row += 28;

			floorPanel.add(lblFloor);
		}

		JScrollPane floorScrollPane = new JScrollPane(floorPanel);
		
		floorScrollPane.setBounds(344, 6, 250, 460);
		floorScrollPane.getVerticalScrollBar().setUnitIncrement(10);
		floorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		floorScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		frame.getContentPane().add(floorScrollPane);
		
		restartTimer();
	}
	
	private void restartTimer() {
		TimerTask task = new TimerTask() {
			public void run() {
				AdvanceReturn ret = ElevatorApp.this.elevator.advanceElevator();
				
				if (ret.isPause()) {
					timer.cancel();
					System.out.println("Elevator Paused on Floor " + (ret.getFloor() + 1) +"\n");
					int index = elevator.getNumFloors() - ret.getFloor() - 1;
					eButtonGroup.get(index).clearSelection();
					String direction = ret.getDirection() == 0 ? "UP" : "DOWN";
					ArrayList<ButtonGroup> bgs = fButtonGroup.get(index);
					for (int i = 0; i < bgs.size(); i++) {
						for (Enumeration<AbstractButton> buttons = bgs.get(i).getElements(); buttons.hasMoreElements();) {
							AbstractButton button = buttons.nextElement();
							if (direction == button.getText()) {
								bgs.get(i).clearSelection();
							}
						}
					}
					restartTimer();
				}
				System.out.println("Elevator Advanced");
				printElevator();
			}
		};
		timer = new Timer();
		timer.schedule(task, 10000, 6000);
	}
	
	public void elevatorButtonClicked(ElevatorButton button) {
		this.elevator.elevatorButtonPressed(button);
		int index = elevator.getNumFloors() - button.getFloor() - 1;
		ArrayList<ButtonGroup> bgs = fButtonGroup.get(index);
		for (int i = 0; i < bgs.size(); i++) {
			for (Enumeration<AbstractButton> buttons = bgs.get(i).getElements(); buttons.hasMoreElements();) {
				AbstractButton b = buttons.nextElement();
				String direction = elevator.getDirection() == 0 ? "UP" : "DOWN";
				if (bgs.size() < 2) {
					b.setSelected(true);
				}
				else if (direction == b.getText()) {
					b.setSelected(true);
				}
			}
		}
		System.out.println("Elevator Button #" + button.getTitle() + " Pressed");
		printElevator();
	}
	
	public void floorButtonClicked(Floor floor, FloorButton button) {
		this.elevator.floorButtonPressed(floor, button);
		int index = elevator.getNumFloors() - floor.getFloorNumber() - 1;
		for (Enumeration<AbstractButton> buttons = eButtonGroup.get(index).getElements(); buttons.hasMoreElements();) {
			AbstractButton b = buttons.nextElement();
			b.setSelected(true);
		}

		System.out.println("Floor #" + floor.getPrintableFloorNumber() + " Button #" + button.getTitle() + " Pressed");
		printElevator();
	}
	
	private void printElevator() {
		System.out.println("Current Elevator Floor #" + Integer.toString(this.elevator.getCurrentFloor() + 1) + " : Moving " + (this.elevator.getDirection() == 0 ? "UP" : "DOWN"));
		System.out.println("Elevator Buttons\t\tFloor Buttons");
		for (int i = 0; i < this.elevator.getElevatorButtons().size(); i++) {
			ElevatorButton eButton = this.elevator.getElevatorButtons().get(i);
			System.out.print("Floor #" + eButton.getTitle() + (eButton.isButtonLit() ? " Lit" : " unLit") + "\t\t");
			
			Floor floor = this.elevator.getFloors().get(i);
			for (int j = 0; j < floor.getButtons().size(); j++ ) {
				FloorButton fButton = floor.getButtons().get(j);
				System.out.print(fButton.getTitle() + (fButton.isButtonLit() ? " Lit\t" : " unLit") + "\t");
			}
			System.out.println();
		}
		System.out.println("");
	}
}
