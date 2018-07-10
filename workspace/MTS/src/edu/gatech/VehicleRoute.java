package edu.gatech;

import java.util.Hashtable;

public class VehicleRoute {
    public Integer ID;
    public Integer routeNumber;
    public String routeName;
    public Hashtable<Integer, Integer> exchangePointsOnRoute;

    public void setNumber(int inputNumber) { this.routeNumber = inputNumber; }

    public void setName(String inputName) { this.routeName = inputName; }

    public void addNewStop(int stopID) { this.exchangePointsOnRoute.put(exchangePointsOnRoute.size(), stopID); }

    public Integer getID() { return this.ID; }

    public Integer getNumber() { return this.routeNumber; }

    public String getName() { return this.routeName; }

    public Integer getNextLocation(int routeLocation) {
        int routeSize = this.exchangePointsOnRoute.size();
        if (routeSize > 0) { return (routeLocation + 1) % routeSize; }
        return -1;
    }

    public Integer getStopID(int routeLocation) { return this.exchangePointsOnRoute.get(routeLocation); }

    public Integer getLength() { return this.exchangePointsOnRoute.size(); }
    
    //Override the equals method to compare the object
    @Override
    public boolean equals(Object object) {
        boolean result = false;
        if (object == null || object.getClass() != getClass()) {
            result = false;
        } else {
            BusRoute me = (BusRoute) object;
            if (this.ID == me.getID()) {
                result = true;
            }
        }
        return result;
    }

}
