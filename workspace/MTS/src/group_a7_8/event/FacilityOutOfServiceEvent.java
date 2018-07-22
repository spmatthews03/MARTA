package group_a7_8.event;

import edu.gatech.TransitSystem;
import edu.gatech.ExchangePoint;

public class FacilityOutOfServiceEvent extends SimEvent{
	private ExchangePoint exchangePoint;
	private boolean outOfService;
	public FacilityOutOfServiceEvent(TransitSystem system, Integer eventID, Integer timeRank, ExchangePoint exchangePoint) {
    	super(system,timeRank,"exchangePoint_out_of_service",eventID);
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
	

	public String getDescription() {
    	StringBuilder sb = new StringBuilder();
    	sb.append(exchangePoint.getType());
    	sb.append(" ");
    	sb.append(exchangePoint.get_uniqueID());
    	sb.append(" is out of service");
    	return sb.toString();	
	}	
}