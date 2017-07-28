package guiCalRev1;

import java.util.Scanner;

/**
 * @author Ying Wang
 * An event time.
 */
public class EventTime implements java.io.Serializable, Comparable<EventTime> {
	// define variables
	public int hours;
	public int minutes;

	/**
	 * Constructs an eventTime object
	 * 
	 * @param t
	 *            the time
	 */

	public EventTime(String t) {
		Scanner s = new Scanner(t).useDelimiter(":");
		hours = s.nextInt();
		minutes = s.nextInt();
	}

	/**
	 * Determines if the event times are equal.
	 * 
	 * @param other
	 *            the other time
	 * @return true if the event times are equal, false otherwise
	 */

	public boolean equals(EventTime other) {
		if (other == null)
			return false;
		EventTime b = other;
		return hours == b.hours && minutes == b.minutes;
	}

	/**
	 * Determines if the event times are equal.
	 * 
	 * @param other
	 *            the other time
	 * @return an integer result
	 */

	public int compareTo(EventTime other) {
		int result = Integer.compare(hours, other.hours);
		if (result == 0) {
			return Integer.compare(minutes, other.minutes);
		} else {
			return result;
		}
	}

	/**
	 * Prints a string representation of the time.
	 * 
	 * @return the time
	 */
	public String toString() {
		  int h = hours;
		  String r = "" ;
		  
		  if (hours == 12) {
			  r = hours + ":";
		      if (minutes < 10) r = r + "0";
		      r = r + minutes + "PM";
		      if (hours < 10) r = "0" + r ;		
		      return r ;	  
		  }
		  
		  if (hours> 12 & hours < 24) {
			  h = h - 12 ;
			  r = h + ":";
		      if (minutes < 10) r = r + "0";
		      r = r + minutes + "PM";
		      if (h < 10) r = "0" + r ;		
		      return r ;
		  }
		  
		  if (hours>=0 && hours <12 ) {
		  r = hours + ":";
	      if (minutes < 10) r = r + "0";
	      r = r + minutes;
	      if (hours < 10) r = "0" + r  ;
	      r = r + "AM" ;
	      return r;
		  }
		  
		  return r;
	}


}
