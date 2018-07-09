package edu.gatech;

public abstract class SimEvent {
    protected Integer timeRank;
    protected String eventType;
    protected Integer eventID;
    protected TransitSystem system;
    protected SimQueue eventQueue;

    public SimEvent(TransitSystem system,int inputRank, String inputType, int inputID) {
    	this.system = system;
        this.timeRank= inputRank;
        this.eventType = inputType;
        this.eventID = inputID;
    }

    public Integer getRank() { return this.timeRank; }

    public String getType() { return this.eventType; }

    public Integer getID() { return this.eventID; }
    
    public SimQueue getEventQueue() { return this.eventQueue; }

    public void setEventQueue(SimQueue eventQueue) { this.eventQueue = eventQueue; }

    public abstract void execute();

    public void displayEvent() {
        // System.out.println();
        System.out.println("# event rank: " + Integer.toString(timeRank) + " type: " + eventType + " ID: " + Integer.toString(eventID));
    }

    //Override the equals method to compare the object
    @Override
    public boolean equals(Object object) {
        boolean result = false;
        if (object == null || object.getClass() != getClass()) {
            result = false;
        } else {
            SimEvent me = (SimEvent) object;
            if ((int)this.timeRank == (int)me.getRank() && this.eventType.equals(me.getType()) && (int)this.eventID == (int)me.getID() ) {
                result = true;
            }
        }
        return result;
    }
    
    public String toJSON() {
    	StringBuilder sb = new StringBuilder();
    	sb.append('{');
    	sb.append("\"ID\":");
    	sb.append(eventID);
    	sb.append(",\"time\":");
    	sb.append(timeRank);
    	sb.append(",\"type\":\"");
    	sb.append(eventType);
    	sb.append('\"');
    	sb.append('}');
    	return sb.toString();
    }

}
