package guiCalRev1;

import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 * @author Ying Wang
 * A class for testing the model, CalView and Controller
 */
public class MyCalendarTester {
	/**
	 * creates a model of serializable data to save all the events, a controller to
	 * update the model, and a CalView to show the calendar and agenda.
	 * 
	 */
	public static void main(String[] args) {

		JFrame frame = new JFrame();
		frame.setTitle("CS151 My Calendar");
		Model model = new Model();
		Controller c = new Controller(model);
		CalView view = new CalView(c);
		c.attach(view);

		frame.add(view, BorderLayout.SOUTH);
		frame.add(c, BorderLayout.NORTH);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
}