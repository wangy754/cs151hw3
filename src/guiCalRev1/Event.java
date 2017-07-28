package guiCalRev1;

import java.util.StringTokenizer;

/**
 * @author Ying Wang
 * A class for appointment objects
 */

public class Event implements java.io.Serializable {
	/**
	 * creates an Appointment object with the specified item and date
	 * 
	 * @param item
	 *            what the appointment is
	 * @param when
	 *            when the appointment is
	 */

	public String item;
	public EventDate date;
	public EventTime startTime;
	public EventTime endTime;

	public Event(String eventLine) {
		StringTokenizer t = new StringTokenizer(eventLine);

		int descriptionCount = t.countTokens() - 3;
		String description = "";
		for (int i = 1; i <= descriptionCount; i++) {
			description += t.nextToken();
			if (i < descriptionCount)
				description += " ";
		}

		String date = t.nextToken();

		String startTime = t.nextToken();

		String endTime = t.nextToken();

		this.item = description;
		this.date = new EventDate(date);
		this.startTime = new EventTime(startTime);
		this.endTime = new EventTime(endTime);
	}

	/**
	 * gets the date of an Appointment
	 * 
	 * @return the date of the Appointment
	 */
	public EventDate getDate() {
		return this.date;
	}

	/**
	 * checks if this Appointment has the same item,date, and start and end times as
	 * another
	 * 
	 * @param other
	 *            the appointment to compare this to
	 */
	public boolean equals(Event other) {
		if (other == null)
			return false;
		else {
			Event a = other;
			return this.item.equals(a.item) && this.date.equals(a.date) && this.startTime.equals(a.startTime)
					&& this.endTime.equals(a.endTime);
		}
	}

	/**
	 * creates a String from this appointment
	 * 
	 * @return a String that represents this Appointment object
	 */
	public String toString() {
		return startTime + " - " + endTime + " " + item;
	}

}
