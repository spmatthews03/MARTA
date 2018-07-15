package edu.gatech;

import java.util.HashMap;
import java.util.Random;

public class ExchangePoint extends Facility {
	protected Random randGenerator;
	protected Integer waiting;
	protected boolean out_of_service;
    private HashMap<Integer, int[]> rateCatching;
    private HashMap<Integer, int[]> rateLeaving;

	public ExchangePoint(){
		//default constructor
	}

	public ExchangePoint(int uniqueID, String name, double x, double y, String type) {
		super(uniqueID, name, x, y,type);
		waiting = 0;
		out_of_service = false;
		randGenerator = new Random();
		this.setRateCatching(new HashMap<Integer, int[]>());
        this.setRateLeaving(new HashMap<Integer, int[]>());
	}

	public Integer exchangeRiders(Integer rank, Integer initialPassangerCount, Integer capacity) {
		return 0;
	}

	public Integer randomBiasedValue(Integer lower, Integer middle, Integer upper) {
		return 0;
	}

	public void addNewRiders(Integer moreRiders) {
	}

	public void addArrivalInfo(Integer timeSlot, Integer minOn, Integer avgOn, Integer maxOn,
							   Integer minOff, Integer avgOff, Integer maxOff) {
	}
	
	public Integer getWaiting() {return waiting;}

    public boolean get_out_of_service() {
    	return out_of_service;
    }
    public void set_out_of_service(boolean value) {
    	out_of_service = value;
    }

	public HashMap<Integer, int[]> getRateCatching() {
		return rateCatching;
	}

	public void setRateCatching(HashMap<Integer, int[]> rateCatching) {
		this.rateCatching = rateCatching;
	}

	public HashMap<Integer, int[]> getRateLeaving() {
		return rateLeaving;
	}

	public void setRateLeaving(HashMap<Integer, int[]> rateLeaving) {
		this.rateLeaving = rateLeaving;
	}
}
