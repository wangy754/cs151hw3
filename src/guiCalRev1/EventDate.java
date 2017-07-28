package guiCalRev1;

import java.util.Scanner;

/**
 * @author Ying Wang
 * An event date.
 */
public class EventDate implements java.io.Serializable, Comparable<EventDate> {
	/**
	 * Constructs an eventDate object.
	 * 
	 * @param d
	 *            the date
	 */
	private int year;
	private int month;
	private int day;

	public EventDate(String d) {
		Scanner s = new Scanner(d).useDelimiter("/");
		year = s.nextInt();
		month = s.nextInt();
		day = s.nextInt();

	}

	/**
	 * Determines if dates are equal.
	 * 
	 * @param the
	 *            other date
	 * @return true if the dates are equal, false otherwise
	 */
	public boolean equals(EventDate other) {
		if (other == null)
			return false;
		EventDate b = other;
		return year == b.year && month == b.month && day == b.day;
	}

	/**
	 * Determines if dates are equal.
	 * 
	 * @param the
	 *            other date
	 * @return an integer result
	 */

	public int compareTo(EventDate other) {
		int resultYear = Integer.compare(year, other.year);
		if (resultYear == 0) {
			int resultMonth = Integer.compare(month, other.month);
			if (resultMonth == 0) {
				return Integer.compare(day, other.day);
			} else {
				return resultMonth;
			}
		} else {
			return resultYear;
		}
	}

	/**
	 * Prints a string representation of the date.
	 * 
	 * @return the date
	 */

	public String toString() {
		return year + "/" + month + "/" + day;
	}

}
