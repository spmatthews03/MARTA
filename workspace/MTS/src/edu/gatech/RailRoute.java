package edu.gatech;

public class RailRoute extends VehicleRoute {
	
   private VehicleRoute route;
	
   public RailRoute() {
	   super();
   }

   public RailRoute(int uniqueValue) {
	   super(uniqueValue);
   }

   public RailRoute(int uniqueValue, int routeNumber, String inputName) {
	   super(uniqueValue, routeNumber, inputName,"railRoute");
   }
   
   public VehicleRoute getRailRoute() { return this.route; }
   
   public void addNewStation(int stationID) { this.addNewExchangePoint(stationID); }

   public Integer getStationID(int routeLocation) { return this.getExchangePointID(routeLocation); }
      
   public void displayEvent() { this.displayEvent("rail"); }

   public void takeTurn() { this.takeTurn("station"); }

   public void displayInternalStatus() { this.displayInternalStatus("stations"); }
   
   public String toJSON() { return this.toJSON("stops"); }
   
   public RailStation getRailStation (TransitSystem system, int routeLocation) {
	   return system.getRailStation(this.getStationID(routeLocation));
   }
}
