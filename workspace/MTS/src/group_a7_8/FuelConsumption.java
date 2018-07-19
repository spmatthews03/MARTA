package group_a7_8;

import edu.gatech.Bus;

public class FuelConsumption {

    private Bus bus;
    private PathKey pathKey;
    private int timeRank;
    private double amount;
    private int passengers;

    public FuelConsumption(Bus bus, PathKey pathKey, int timeRank, double distance, int passengers) {
		super();
		this.bus = bus;
		this.pathKey = pathKey;
		this.timeRank = timeRank;
		this.amount = distance;
		this.passengers = passengers;
	}

    public double getFuelConsumed(){ return amount; }

    public Bus getBus(){ return bus; }

    public PathKey getPathKey(){ return pathKey; }

    public int getTimeRank(){ return timeRank; }

    public int getPassengers() { return passengers; }

    public void setPassengers(int passengers){
        this.passengers = passengers;
    }

    public void setBus(Bus bus) { this.bus = bus; }

    public void setPathKey(PathKey pathKey) { this.pathKey = pathKey; }

    public void setTimeRank(int timeRank) { this.timeRank = timeRank; }

    public void setAmount(double amount) { this.amount = amount; }

    public String toJSON() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append("\"bus\":");
        sb.append(bus.getID());
        sb.append(',');
        sb.append("\"pathKey\":");
        sb.append(pathKey.toJSON());
        sb.append(',');
        sb.append("\"timeRank\":");
        sb.append(this.timeRank);
        sb.append(',');
        sb.append("\"amount\":");
        sb.append(this.amount);
        sb.append(',');
        sb.append("\"passengers\":");
        sb.append(this.passengers);
        sb.append('}');
        return sb.toString();
    }
}
