package edu.gatech;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public abstract class VehicleRoute {
    protected Integer ID;
    protected Integer routeNumber;
    protected String routeName;
    protected Hashtable<Integer, Integer> exchangePointsOnRoute;
	private String type;
    
	
    public String getType() {
		return type;
	}

    protected VehicleRoute() {
    	this.ID = -1;
    }

	protected VehicleRoute(int uniqueValue) {
    	this.ID = uniqueValue;
    	this.routeNumber = -1;
    	this.routeName = "";
    	this.exchangePointsOnRoute = new Hashtable<Integer, Integer>();
    }
    
    protected VehicleRoute(int uniqueValue, int inputNumber, String inputName,String type) {
    	this.ID = uniqueValue;
    	this.routeNumber = inputNumber;
    	this.routeName = inputName;
    	this.exchangePointsOnRoute = new Hashtable<Integer, Integer>();
    	this.type = type;
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
   
    public Integer get_next_next_location(int routeLocation) {
        int routeSize = this.exchangePointsOnRoute.size();
        if (routeSize > 0) { return (routeLocation + 2) % routeSize; }
        return -1;
    }

    public Integer getLength() { return this.exchangePointsOnRoute.size(); }

    protected void addNewExchangePoint(int exchangePointID) { this.exchangePointsOnRoute.put(exchangePointsOnRoute.size(), exchangePointID); }
    
    protected Integer getExchangePointID(int routeLocation) {
    	System.out.print("getExchangePointID: " + routeLocation);
    	System.out.print("getExchangePointID pointer: " + this.exchangePointsOnRoute);
    	return this.exchangePointsOnRoute.get(routeLocation);
	}
    
    protected Integer get_route_location_index(Integer exchange_point) {
    	Set<Integer> keys = this.exchangePointsOnRoute.keySet();
    	for (Integer key: keys) {
    		Integer value = this.exchangePointsOnRoute.get(key);
    		if (exchange_point.equals(value)) {
    			return key;
    		}
    	}
    	return null;
    }
    
    protected void displayEvent(String vehicleType) {
 	   //System.out.println(" " + vehicleType + " route: " + Integer.toString(this.ID));
    }

    protected void takeTurn(String exchangePointType) {
        System.out.println("provide next " + exchangePointType + " on route along with the distance");
    }

    protected void displayInternalStatus(String exchangePointType) {
        System.out.print("> " + this.type + " - ID: " + Integer.toString(this.ID));
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
 	   sb.append("\"type\":\"");
 	   sb.append(this.type);
 	   sb.append("\",");
 	   sb.append("\"ID\":");
 	   sb.append(this.ID);
 	   sb.append(',');
 	   sb.append("\"number\":");
 	   sb.append(this.routeNumber);
 	   sb.append(',');
 	   sb.append("\"name\":\"");
 	   sb.append(this.getName());
 	   sb.append('\"');
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
