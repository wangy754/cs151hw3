package guiCalRev1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * a class to show the calendar and agenda.
 * @author Ying Wang
 */

public class CalView extends JPanel implements ChangeListener {

	DefaultTableModel CalTable;
	final int width = 700;
	final int height = 200;
	int currentDay, highr, highc;

	JTextArea events; // to show all the agenda
	JTable table; // to the the calendar view
	public Controller control;
	public Calendar calV = new GregorianCalendar();

	// constructor of a CalView
	CalView(Controller c) {
		this.control = c;
		c.loadEventBook();

		// create the calendar panel and the agenda panel
		this.setPreferredSize(new Dimension(width, height));
		String[] columns = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
		CalTable = new DefaultTableModel(null, columns);
		table = new JTable(CalTable);

		table.setCellSelectionEnabled(true);
		events = new JTextArea();
		JScrollPane pane = new JScrollPane(table);

		this.setLayout(new GridLayout(1, 2));
		this.add(pane);
		this.add(events);
		this.updateMonth();
		this.updateEvents();

		// add MouseListener to the calendar table to make each day selectable

		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) throws NullPointerException {
				// System.out.println("test");
				try {
					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow();
					int column = target.getSelectedColumn();
					int j = (int) target.getValueAt(row, column);
					c.cal.set(Calendar.DATE, j);
					c.update();
				} catch (Exception e1) {
					// System.out.println("An error occurred. Try again.");
				}
				;
			}
		});

	} // end constructor

	/**
	 * update the textArea events
	 * 
	 * @param
	 */

	private void updateEvents() {
		// TODO Auto-generated method stub
		events.setText(control.getAgenda());
	}

	/**
	 * update the month view
	 * 
	 * @param
	 */
	private void updateMonth() {
		currentDay = control.cal.get(Calendar.DATE);
		int month = control.cal.get(Calendar.MONTH);
		calV.set(Calendar.MONTH, month);

		calV.set(Calendar.DAY_OF_MONTH, 1);
		int startDay = calV.get(Calendar.DAY_OF_WEEK);
		int numberOfDays = calV.getActualMaximum(Calendar.DAY_OF_MONTH);
		int weeks = calV.getActualMaximum(Calendar.WEEK_OF_MONTH);

		CalTable.setRowCount(0);
		CalTable.setRowCount(weeks);
		int i = startDay - 1;
		for (int day = 1; day <= numberOfDays; day++) {
			CalTable.setValueAt(day, i / 7, i % 7);

			if (day == currentDay) {
				highr = i / 7;
				highc = i % 7;
			}
			i = i + 1;
		}

		// use a tableRenderer to highlight the current date and the weekend
		table.setDefaultRenderer(table.getColumnClass(0), new tblCalendarRenderer());

	} // end updateMonth

	/**
	 * call updateMonth and updateEvents to update the CalView
	 * 
	 * @param
	 */

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		updateMonth();
		updateEvents();
	}

	// create a tableRenderer class to highlight the current date and the weekend

	public class tblCalendarRenderer extends DefaultTableCellRenderer {
		public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused,
				int row, int column) {
			super.getTableCellRendererComponent(table, value, selected, focused, row, column);

			if (row == highr && column == highc) { // current day
				setBackground(new Color(220, 220, 220));
			} else if (column == 0 || column == 6) { // week end
				setBackground(new Color(255, 220, 220));
			} else { // Week
				setBackground(new Color(255, 255, 255));
			}

			setBorder(null);
			setForeground(Color.black);
			return this;
		}
	}
}
