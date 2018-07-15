package edu.gatech;

public class BusRoute extends VehicleRoute {
	   private VehicleRoute route;
		
	   public BusRoute() {
		   super();
	   }

	   public BusRoute(int uniqueValue) {
		   super(uniqueValue);
	   }

	   public BusRoute(int uniqueValue, int inputNumber, String inputName) {
		   super(uniqueValue, inputNumber, inputName,"busRoute");
	   }
	   
	   public VehicleRoute getBusRoute() { return this.route; }
	   
	   public void addNewStop(int stopID) { this.addNewExchangePoint(stopID); }

	   public Integer getStopID(int routeLocation) { return this.getExchangePointID(routeLocation); }
	      
	   public void displayEvent() { this.displayEvent("bus"); }

	   public void takeTurn() { this.takeTurn("stop"); }

	   public void displayInternalStatus() { this.displayInternalStatus("stops"); }
	   
	   public String toJSON() { return this.toJSON("stops"); }
	   
	   public BusStop getBusStop(TransitSystem system, int routeLocation) {
		   return system.getBusStop(this.getExchangePointID(routeLocation));
	   }
	}
