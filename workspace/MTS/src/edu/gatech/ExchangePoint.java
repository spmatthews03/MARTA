package edu.gatech;

import java.util.Random;

public class ExchangePoint extends Facility {
	protected Random randGenerator;
	protected Integer waiting;
	protected boolean exchangePointDown;

	public ExchangePoint(){
		//default constructor
	}

	public ExchangePoint(int uniqueID, String name, double x, double y, String type) {
		super(uniqueID, name, x, y,type);
		waiting = 0;
		exchangePointDown = false;
		randGenerator = new Random();
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
	
	public int getWaiting() {return waiting;}
}
