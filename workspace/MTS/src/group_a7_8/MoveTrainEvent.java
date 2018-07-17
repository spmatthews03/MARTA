package group_a7_8;

//import edu.gatech.Bus;
//import edu.gatech.BusRoute;
import edu.gatech.RailCar;
import edu.gatech.RailRoute;
import edu.gatech.SimEvent;
//import edu.gatech.Stop;
import edu.gatech.RailStation;
import edu.gatech.TransitSystem;

public class MoveTrainEvent extends SimEvent{

    private RailCar train;

	public MoveTrainEvent(TransitSystem system, int eventID, int timeRank, RailCar train) {
    	super(system,timeRank,"move_bus",eventID);
    	this.train = train;

    }

    public RailCar getTrain() { return this.train; }
    
	public String getDescription() {
    	StringBuilder sb = new StringBuilder();
    	sb.append(" moving");
    	sb.append(train.getType());
    	sb.append(" ");
    	sb.append(train.getID());
    	return sb.toString();	
	}	

	@Override
	public void execute() {
        displayEvent();

        // identify the train that will move
        RailCar activeTrain = getTrain();
        System.out.println(" the train being observed is: " + Integer.toString(activeTrain.getID()));

        // identify the current station
        RailRoute activeRoute = system.getRailRoute(activeTrain.getRouteID());
        System.out.println(" the bus is driving on route: " + Integer.toString(activeRoute.getID()));

        int activeLocation = activeTrain.getLocation();
        int activeStationID = activeRoute.getStationID(activeLocation);
        RailStation activeStation = system.getRailStation(activeStationID);
         System.out.println(" the Train is currently at station: " + Integer.toString(activeStation.get_uniqueID()) + " - " + activeStation.getFacilityName());

        // drop off and pickup new passengers at current station
        int currentPassengers = activeTrain.getPassengers();
        int passengerDifferential = activeStation.exchangeRiders(getRank(), currentPassengers, activeTrain.getCapacity());
        System.out.println(" passengers pre-station: " + Integer.toString(currentPassengers) + " post-station: " + (currentPassengers + passengerDifferential));
        activeTrain.adjustPassengers(passengerDifferential);

        // determine next station
        int nextLocation = activeRoute.getNextLocation(activeLocation);
        int nextStationID = activeRoute.getStationID(nextLocation);
        RailStation nextStation = system.getRailStation(nextStationID);
        System.out.println(" the bus is heading to station: " + Integer.toString(nextStationID) + " - " + nextStation.getFacilityName() + "\n");

        // find distance to station to determine next event time
        Double travelDistance = activeStation.findDistance(nextStation);
        // conversion is used to translate time calculation from hours to minutes
        int travelTime = 1 + (travelDistance.intValue() * 60 / activeTrain.getSpeed());
        activeTrain.setLocation(nextLocation);

        // generate next event for this bus
        eventQueue.add(new MoveTrainEvent(system, eventID, getRank() + travelTime,train));               
		
	}

}
