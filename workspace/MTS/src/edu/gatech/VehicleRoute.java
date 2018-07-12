package edu.gatech;

import java.util.Hashtable;

public abstract class VehicleRoute {
    protected Integer ID;
    protected Integer routeNumber;
    protected String routeName;
    protected Hashtable<Integer, Integer> exchangePointsOnRoute;
    
    public VehicleRoute() {
    	this.ID = -1;
    }

    public VehicleRoute(int uniqueValue) {
    	this.ID = uniqueValue;
    	this.routeNumber = -1;
    	this.routeName = "";
    	this.exchangePointsOnRoute = new Hashtable<Integer, Integer>();
    }
    
    public VehicleRoute(int uniqueValue, int inputNumber, String inputName) {
    	this.ID = uniqueValue;
    	this.routeNumber = inputNumber;
    	this.routeName = inputName;
    	this.exchangePointsOnRoute = new Hashtable<Integer, Integer>();
   }

    public void setNumber(int inputNumber) { this.routeNumber = inputNumber; }

    public void setName(String inputName) { this.routeName = inputName; }

    public Integer getID() { return this.ID; }

    public Integer getNumber() { return this.routeNumber; }

    public String getName() { return this.routeName; }

    public Integer getNextLocation(int routeLocation) {
        int routeSize = this.exchangePointsOnRoute.size();
        if (routeSize > 0) { return (routeLocation + 1) % routeSize; }
        return -1;
    }
    
    public Integer getLength() { return this.exchangePointsOnRoute.size(); }

    protected void addNewExchangePoint(int exchangePointID) { this.exchangePointsOnRoute.put(exchangePointsOnRoute.size(), exchangePointID); }
    
    protected Integer getExchangePointID(int routeLocation) { return this.exchangePointsOnRoute.get(routeLocation); }
    
    protected void displayEvent(String vehicleType) {
 	   System.out.println(" " + vehicleType + " route: " + Integer.toString(this.ID));
    }

    protected void takeTurn(String exchangePointType) {
        System.out.println("provide next " + exchangePointType + " on route along with the distance");
    }

    protected void displayInternalStatus(String exchangePointType) {
        System.out.print("> route - ID: " + Integer.toString(this.ID));
        System.out.print(" number: " + Integer.toString(this.routeNumber) + " name: " + this.routeName);
        System.out.print(" " + exchangePointType +": [ ");
        for (int i = 0; i < this.exchangePointsOnRoute.size(); i++) {
            System.out.print(Integer.toString(i) + ":" + Integer.toString(this.exchangePointsOnRoute.get(i)) + " ");
        }
        System.out.println("]");
    }
    
    protected String toJSON(String exchangePointType) {
 	   StringBuilder sb = new StringBuilder();
 	   sb.append('{');
 	   sb.append("\"ID\":");
 	   sb.append(this.ID);
 	   sb.append(',');
 	   sb.append("\"routeNumber\":");
 	   sb.append(this.routeNumber);
 	   if(this.exchangePointsOnRoute!=null && this.exchangePointsOnRoute.size()>0) {
 	      	sb.append(',');
 	      	sb.append("\"" + exchangePointType + "\":[");    
 	      	for(int key = 0; key<this.exchangePointsOnRoute.size();key++) {
 	      		if(key>0) {
 	              	sb.append(',');        			
 	      		}
 	      		if(this.exchangePointsOnRoute.containsKey(key)) {
 	      	    	sb.append(this.exchangePointsOnRoute.get(key));
 	      		}	
 	      	}
 	      	sb.append(']');
 	   }
 	   sb.append('}');
 	   return sb.toString();
    }
    
    //Override the equals method to compare the object
    @Override
    public boolean equals(Object object) {
        boolean result = false;
        if (object == null || object.getClass() != getClass()) {
            result = false;
        } else {
        	VehicleRoute me = (VehicleRoute) object;
            if (this.ID == me.getID()) {
                result = true;
            }
        }
        return result;
    }

}
