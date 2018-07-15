package group_a7_8;

import edu.gatech.SimEvent;
import edu.gatech.TransitSystem;
import edu.gatech.Facility;

public class FacilityOutOfServiceEvent extends SimEvent{
	private Facility facility;
	private boolean outOfService;
	public FacilityOutOfServiceEvent(TransitSystem system, Integer eventID, Integer timeRank, Facility facility) {
    	super(system,timeRank,"set_vehicle_outOfService",eventID);
		this.facility = facility;
		this.outOfService = true;
	}
	public Facility getFacility() {
		return facility;
	}
	public boolean getOutOfService() {
		return outOfService;
	}
	@Override
	public void execute() {
		displayEvent();
		System.out.printf(" %s:\n\t%s\n", eventType,toJSON());
		
		//Need setter in facility class
		/*
		facility.setOutOfService(outOfService);
		System.out.printf(" %s%d is out of service\n\n",facility.type,facility.get_uniqueID());
		*/
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
	    	sb.append("\",\"facilityType\":");
	    	sb.append(facility.type);
	    	sb.append(",\"facilityID\":");
	    	sb.append(facility.get_uniqueID());
	    	sb.append(",\"outOfService\":");
	    	sb.append(outOfService);
	    	sb.append('}');
	    	return sb.toString();
	}	
}