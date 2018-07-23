package edu.gatech;

import java.util.Comparator;
import java.util.PriorityQueue;

import group_a7_8.event.SimEvent;
import group_a7_8.event.SimEventComparator;
import group_a7_8.server.StateChangeListener;

public class SimQueue {
    private static PriorityQueue<SimEvent> eventQueue;
    private Comparator<SimEvent> simComparator;
    final static Integer passengerFrequency = 3;
    private StateChangeListener listener;
    private Integer time;
    private int nextEventId=0;

    public SimQueue() {
        simComparator = new SimEventComparator();
        eventQueue = new PriorityQueue<SimEvent>(100, simComparator);
        time=0;
    }

    public int getNextEventID() {
    	//helper method to auto-increment event ID's
    	int eventID = nextEventId;
    	nextEventId++;
    	return eventID;
    	
    }
    public boolean hasEvents() {
    	return eventQueue.size()>0;
    }
    public int getSize() {
    	return eventQueue.size();
    }
    public SimEvent[] getEvents() {
    	SimEvent[] events = new SimEvent[1];
    	return eventQueue.toArray(events);
    }
    
    public void triggerNextEvent(TransitSystem system) {
        if (eventQueue.size() > 0) {
            SimEvent activeEvent = eventQueue.poll();
            time = activeEvent.getRank();
            activeEvent.execute();
            listener.updateState();
        } else {
            System.out.println(" event queue empty");
        }
    }

    public void add(SimEvent event) {
    	event.setEventQueue(this);
        eventQueue.add(event);
        listener.updateState();
    }

	public void setStateChangeListener(StateChangeListener listener) {
		this.listener = listener;
		
	}

	public Integer getTime() {
		return time;
	}

	public void reset() {
		System.out.println("clearing event queue");

		eventQueue.clear();
		
		time=0;
		nextEventId=0;
	}


}
