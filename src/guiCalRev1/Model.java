package guiCalRev1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

/**
 * @author Ying Wang
 * creates a model class of serializable data to save all the events
 */

public class Model implements java.io.Serializable {
	private static final int NOTFOUND = -1;
	private transient TreeMap<EventDate, PriorityQueue<Event>> events = null;
	private ArrayList<Event> eList = new ArrayList<Event>();

	/*
	 * constructor
	 */
	public Model(ArrayList<Event> evenList) {
		for (Event e : evenList) {
			add(e);
		}
		;
	}

	/*
	 * constructor for empty model
	 */
	public Model() {
	}

	/**
	 * load events from eList to the event book
	 */

	public void load() {
		for (Event e : eList) {
			add(e);
		}
	}

	/**
	 * save all events to an event book
	 */

	public void save() {
		// System.out.println("Run save() in model");
		eList.clear();
		ArrayList<Event> lt = getAllEvents();
		eList = (ArrayList<Event>) lt.clone();
	}

	/**
	 * adds an event to an event book
	 * 
	 * @param e
	 *            the event to add
	 */
	public void add(Event a) {
		EventDate date = a.date;
		EventTime stime = a.startTime;

		if (events == null) {
			events = new TreeMap<EventDate, PriorityQueue<Event>>();
			// System.out.println("Events list is null. Created a new event list = " +
			// events);
		}
		if (!events.containsKey(a.date)) {
			Comparator<Event> eventComparator = new Comparator<Event>() {
				@Override
				public int compare(Event left, Event right) {
					return left.startTime.compareTo(right.startTime);
				}
			};
			PriorityQueue pq = new PriorityQueue<Event>(5000, eventComparator);
			pq.add(a);
			events.put(date, pq);
			// System.out.println("A new Event is added " + a);
		} else {
			PriorityQueue<Event> pq = events.get(date);
			if (!pq.contains(stime)) {
				pq.add(a);
				events.put(date, pq);
				// System.out.println("A new Event is added " + a);
			} else {
				pq.add(a);
				// System.out.println("A Event is updated " + a);
			}
		}
		// System.out.println("Event list=" + events);
	}

	/**
	 * determines if an event is in the book
	 * 
	 * @param a
	 *            the event to find
	 */
	public boolean isInBook(Event a) {
		if (events.containsKey(a.date)) {
			PriorityQueue<Event> pq = events.get(a.date);
			for (Event e : pq) {
				if (e.equals(a)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * extracts, from an event book, a list of the events that occur on a particular
	 * day
	 * 
	 * @param date
	 *            the day of the events to list
	 * @return returns an Arraylist of events on a particular day
	 */
	public ArrayList<Event> getDayList(EventDate date) {
		ArrayList<Event> l = new ArrayList<Event>();

		for (Event a : eList) {
			if (a.getDate().equals(date))
				l.add(a);
		}
		return l;

	}

	/**
	 * creates a list of all of the events from an event book
	 * 
	 * @return returns an Arraylist of events
	 */
	public ArrayList<Event> getAllEvents() {
		ArrayList<Event> l = new ArrayList<Event>();
		for (Map.Entry<EventDate, PriorityQueue<Event>> entry : events.entrySet()) {
			EventDate date = entry.getKey();
			PriorityQueue<Event> pq = events.get(date);
			ArrayList<Event> tmp = new ArrayList(pq);
			l.addAll(tmp);
		}
		return l;
	}

	/**
	 * determines if the book is empty
	 * 
	 * @return a boolean
	 */

	public boolean isEmpty() {
		return events.isEmpty();
	}

	/**
	 * check if an event has time conflicts with current event of the same day.
	 * 
	 * @return a boolean
	 */

	public boolean checkConflick(Event event) {
		
		if (event.startTime.toString() == "" || event.endTime.toString() == "") return true;

		if (event.startTime.compareTo(event.endTime) >= 0) return true;

		EventDate date = event.getDate();
		ArrayList<Event> l = new ArrayList<Event>();
		l = getDayList(date);

		for (Event ev : l) {
			if (event.startTime.compareTo(ev.startTime) > 0 && event.startTime.compareTo(ev.endTime) < 0)
				return true;
			if (event.endTime.compareTo(ev.startTime) > 0 && event.endTime.compareTo(ev.endTime) < 0)
				return true;
			if (ev.startTime.compareTo(event.startTime) > 0 && ev.startTime.compareTo(event.endTime) < 0)
				return true;
		}
		for (Event ev : l) {
			if (event.startTime.compareTo(ev.startTime) == 0)
				return true;
		}
		return false;

	}

}