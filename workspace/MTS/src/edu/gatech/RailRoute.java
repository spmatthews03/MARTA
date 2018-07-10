package edu.gatech;

import java.util.Hashtable;

public class RailRoute {
	
	private VehicleRoute route;
	
    public RailRoute() {
    	route.ID = -1;
    }

    public RailRoute(int uniqueValue) {
    	route.ID = uniqueValue;
    	route.routeNumber = -1;
    	route.routeName = "";
    	route.exchangePointsOnRoute = new Hashtable<Integer, Integer>();
    }

    public RailRoute(int uniqueValue, int inputNumber, String inputName) {
    	route.ID = uniqueValue;
    	route.routeNumber = inputNumber;
    	route.routeName = inputName;
    	route.exchangePointsOnRoute = new Hashtable<Integer, Integer>();
   }
   
   public VehicleRoute getVehicleRoute() { return route; }
   
   public void displayEvent() {
       System.out.println(" rail route: " + Integer.toString(route.ID));
   }

   public void takeTurn() {
       System.out.println("provide next station on route along with the distance");
   }

   public void displayInternalStatus() {
       System.out.print("> route - ID: " + Integer.toString(route.ID));
       System.out.print(" number: " + Integer.toString(route.routeNumber) + " name: " + route.routeName);
       System.out.print(" station: [ ");
       for (int i = 0; i < route.exchangePointsOnRoute.size(); i++) {
           System.out.print(Integer.toString(i) + ":" + Integer.toString(route.exchangePointsOnRoute.get(i)) + " ");
       }
       System.out.println("]");
   }
   
   public String toJSON() {
   	StringBuilder sb = new StringBuilder();
   	sb.append('{');
   	sb.append("\"ID\":");
   	sb.append(route.ID);
   	sb.append(',');
   	sb.append("\"routeNumber\":");
   	sb.append(route.routeNumber);
   	if(route.exchangePointsOnRoute!=null && route.exchangePointsOnRoute.size()>0) {
       	sb.append(',');
       	sb.append("\"stations\":[");    
       	for(int key = 0; key<route.exchangePointsOnRoute.size();key++) {
       		if(key>0) {
               	sb.append(',');        			
       		}
       		if(route.exchangePointsOnRoute.containsKey(key)) {
       	    	sb.append(route.exchangePointsOnRoute.get(key));
       		}	
       	}
       	sb.append(']');
   	}
   	sb.append('}');
   	return sb.toString();
   }
   
}
