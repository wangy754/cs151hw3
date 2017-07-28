package guiCalRev1;

/**
 * @author Ying Wang
 * A class for accessing a calendar via the GUI
 */

import java.util.ArrayList;
import java.util.Calendar;

import java.util.GregorianCalendar;
import java.util.Locale;
import java.io.PrintStream;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.io.FileInputStream;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class Controller extends JPanel {
	private Scanner in;
	private PrintStream out;
	private Model book;
	private ChangeListener lis;
	public Calendar cal = new GregorianCalendar();
	public Event event;
	public EventTime start;
	public EventTime end;

	final int width = 600;
	final int height = 30;
	JLabel label;
	String month;
	String weekday;
	int day;
	int year;

	/**
	 * constructs a controller to navigate the calendar and create event
	 * 
	 * @param model
	 *            of the events
	 */
	public Controller(Model m) {

		this.book = m;

		this.setPreferredSize(new Dimension(width, height));
		label = new JLabel();
		label.setHorizontalAlignment(SwingConstants.CENTER);
		updateLabel();

		// navigate the day
		JButton b1 = new JButton("<<");
		b1.addActionListener(factory(-1));
		JButton b2 = new JButton(">>");
		b2.addActionListener(factory(1));

		JButton b3 = new JButton("Create");
		b3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					CreateEventView();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					errorMessage();
				}
			}
		});

		JButton b4 = new JButton("Quit");
		b4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveEventBook();
				System.exit(0);
			}
		});

		this.setLayout(new GridLayout(1, 5));
		this.add(b1);
		this.add(label);
		this.add(b2);
		this.add(b3);
		this.add(b4);
	}

	/**
	 * pop out a window to enter and create new events
	 * 
	 * @param
	 */

	protected void CreateEventView() {
		// TODO Auto-generated method stub
		final JFrame frame = new JFrame();
		final JTextField descField = new JTextField("Description");
		final JTextField dateField = new JTextField(
				(cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR));
		dateField.setEditable(false);
		final JTextField startField = new JTextField("00:00");
		final JTextField endField = new JTextField("23:59");

		JButton saveButton = new JButton("Save");
		JButton cancelButton = new JButton("Quit");

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});

		// CONTROLLER gets the from the fields and updates the model with a new Event.
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = descField.getText();
				String date = dateField.getText();
				String start = startField.getText();
				String end = endField.getText();
				String eLine = name + " " + date + " " + start + " " + end;
				Event et = new Event("Description  00/00/0000 00:00 00:00 ");
				try {
					et = new Event(eLine);
					if (book.checkConflick(et))
						warningView();
					else {
						try {
							book.add(et);
							book.save();
							update();
						} catch (Exception e1) {
							System.out.println("error");
						}
						;
					}

				} catch (Exception e2) {
					errorMessage();
				}

			}
		});

		JPanel panel = new JPanel();
		panel.add(descField);
		panel.add(dateField);
		panel.add(startField);
		panel.add(endField);
		panel.add(saveButton);
		panel.add(cancelButton);
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * load events from events.txt
	 * 
	 */

	public void loadEventBook() {
		File f = new File("./events.txt");
		if (f.exists()) {
			try {
				FileInputStream fileIn = new FileInputStream("events.txt");
				ObjectInputStream in = new ObjectInputStream(fileIn);
				book = (Model) in.readObject();
				in.close();
				fileIn.close();
				book.load();
				System.out.println("Events loaded.\n");
			} catch (IOException i) {
				i.printStackTrace();
				return;
			} catch (ClassNotFoundException c) {
				System.out.println("EventBook class not found");
				c.printStackTrace();
				return;
			}
		} else
			System.out.println("First run, no file to load.\n");
	}

	/**
	 * save events to events.txt
	 * 
	 * @param
	 */

	public void saveEventBook() {
		if (book.isEmpty()) {
			// First time
			System.out.println("The event book is empty");
			return;
		}
		try {
			book.save();
			FileOutputStream fileOut = new FileOutputStream("events.txt");
			ObjectOutputStream oOut = new ObjectOutputStream(fileOut);
			oOut.writeObject(book);
			oOut.close();
			fileOut.close();
			System.out.printf("Serialized data is saved in events.txt\n");
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	/**
	 * attach a changeListener to the controller
	 * 
	 * @param changeListener
	 */

	public void attach(ChangeListener c) {
		lis = c;
	}

	/**
	 * when the data in model is changed update the view and the label in the
	 * controller panel
	 * 
	 * @param
	 */
	public void update() {
		lis.stateChanged(new ChangeEvent(this));
		updateLabel();
	}

	/**
	 * return a string of all the events on the date that the GregorianCalendar is
	 * holding
	 * 
	 * @param void
	 */
	public String getAgenda() {
		int dd = cal.get(GregorianCalendar.DATE);
		int mm = cal.get(GregorianCalendar.MONTH) + 1;
		int yy = cal.get(GregorianCalendar.YEAR);
		weekday = cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US);
		String str = Integer.toString(mm) + "/" + Integer.toString(dd) + "/" + Integer.toString(yy);
		String str2 = Integer.toString(mm) + "/" + Integer.toString(dd) ;
		EventDate d = new EventDate(str);
		ArrayList<Event> l = book.getDayList(d);
		String returningString = new String();
		returningString = weekday +" "+ str2 + '\n';
		if (l.size() == 0)
			returningString += "no events for that day" + '\n';

		for (Event ev : l) {
			returningString += ev.toString() + '\n';
		}
		return returningString;
	}

	/**
	 * factory method return an actionListener that will update the date of
	 * GregorianCalendar
	 * 
	 * @param int
	 *            which can be 1 or -1 , call the update method
	 */
	private ActionListener factory(int i) {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				cal.add(Calendar.DATE, i);
				update();
			}
		};
	}

	/**
	 * update the label in controller
	 * 
	 * @param
	 */

	public void updateLabel() {
		day = cal.get(Calendar.DATE);
		year = cal.get(Calendar.YEAR);
		month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.US);
		label.setText( month + " " + day + ", " + year);
	}

	/**
	 * pop out a warning message of " time conflict or incorrect entry"
	 * 
	 * @param
	 */

	public void warningView() {
		// TODO Auto-generated method stub
		final JFrame frame = new JFrame();
		final JLabel lab = new JLabel("time conflict or incorrect entry, please enter again");
		JButton okButton = new JButton("ok");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		JPanel warning = new JPanel();
		warning.add(lab);
		warning.add(okButton);
		frame.add(warning);
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * pop out a error message of " incorrect time entry"
	 * 
	 * @param
	 */

	public void errorMessage() {
		// TODO Auto-generated method stub
		final JFrame frame = new JFrame();
		final JLabel lab = new JLabel("incorrent time entry, please enter again");
		JButton okButton = new JButton("ok");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		JPanel warning = new JPanel();
		warning.add(lab);
		warning.add(okButton);
		frame.add(warning);
		frame.pack();
		frame.setVisible(true);
	}
}
