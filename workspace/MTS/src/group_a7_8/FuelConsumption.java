package group_a7_8;

import edu.gatech.Bus;

public class FuelConsumption {

    private Bus bus;
    private PathKey pathKey;
    private int timeRank;
    private double amount;


    public FuelConsumption(Bus bus, PathKey pathKey){
        this.bus = bus;
        this.pathKey = pathKey;
    }

    public double getFuelConsumed(){ return amount; }

    public Bus getBus(){ return bus; }

    public PathKey getPathKey(){ return pathKey; }

    public int getTimeRank(){ return timeRank; }

    public void setBus(Bus bus) { this.bus = bus; }

    public void setPathKey(PathKey pathKey) { this.pathKey = pathKey; }

    public void setTimeRank(int timeRank) { this.timeRank = timeRank; }

    public void setAmount(double amount) { this.amount = amount; }

    // Helper method to update the amount of fuel consumed
    private void updateFuelConsumption(){
        // TODO: populate method
    }

    public String toJSON() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append("\"bus\":");
        sb.append(bus.getID());
        sb.append(',');
        //sb.append("\"pathKey\":");
        //sb.append(pathKey);
        sb.append("\"timeRank\":");
        sb.append(this.timeRank);
        sb.append("\"amount\":");
        sb.append(this.amount);
        sb.append('}');
        return sb.toString();
    }
}
