package group_a7_8;

import edu.gatech.ExchangePoint;
import edu.gatech.SimEvent;
import edu.gatech.TransitSystem;

public class FacilityResumeServiceEvent extends SimEvent{
	private ExchangePoint exchangePoint;
	private boolean outOfService;
	public FacilityResumeServiceEvent(TransitSystem system, Integer eventID, Integer timeRank, ExchangePoint exchangePoint) {
    	super(system,timeRank,"exchangePoint_resumed_service",eventID);
		this.exchangePoint = exchangePoint;
		this.outOfService = false;
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
		System.out.printf(" %s%d resumed service\n\n",exchangePoint.getType(),exchangePoint.get_uniqueID());
	}
	

	public String getDescription() {
    	StringBuilder sb = new StringBuilder();
    	sb.append(exchangePoint.getType());
    	sb.append(" ");
    	sb.append(exchangePoint.get_uniqueID());
    	sb.append(" is back in service");
    	return sb.toString();	
	}	
	
}