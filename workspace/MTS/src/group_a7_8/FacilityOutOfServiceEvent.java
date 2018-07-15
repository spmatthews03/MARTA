package group_a7_8;

import edu.gatech.SimEvent;
import edu.gatech.TransitSystem;
import edu.gatech.ExchangePoint;

public class FacilityOutOfServiceEvent extends SimEvent{
	private ExchangePoint exchangePoint;
	private boolean outOfService;
	public FacilityOutOfServiceEvent(TransitSystem system, Integer eventID, Integer timeRank, ExchangePoint exchangePoint) {
    	super(system,timeRank,"set_vehicle_outOfService",eventID);
		this.exchangePoint = exchangePoint;
		this.outOfService = true;
	}
	public ExchangePoint getExchangePoint() {
		return exchangePoint;
	}
	public boolean getOutOfService() {
		return outOfService;
	}
	@Override
	public void execute() {
		displayEvent();
		System.out.printf(" %s:\n\t%s\n", eventType,toJSON());

		exchangePoint.set_out_of_service(outOfService);
		System.out.printf(" %s%d is out of service\n\n",exchangePoint.getType(),exchangePoint.get_uniqueID());
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
	    	sb.append(exchangePoint.getType());
	    	sb.append(",\"facilityID\":");
	    	sb.append(exchangePoint.get_uniqueID());
	    	sb.append(",\"outOfService\":");
	    	sb.append(outOfService);
	    	sb.append('}');
	    	return sb.toString();
	}	
}