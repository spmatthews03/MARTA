package edu.gatech;

import java.util.HashMap;

public class BusStop extends ExchangePoint {
    private HashMap<Integer, int[]> rateCatchingBus;
    private HashMap<Integer, int[]> rateLeavingBus;

	public BusStop(int uniqueValue, String inputName, int inputRiders, double inputXCoord, double inputYCoord) {
			super(uniqueValue, inputName, inputXCoord, inputYCoord);
			this.rateCatchingBus = new HashMap<Integer, int[]>();
	        this.rateLeavingBus = new HashMap<Integer, int[]>();
	        this.waiting = inputRiders;
	}

    public void displayEvent() {
        System.out.println(" bus stop: " + Integer.toString(this.get_uniqueID()));
    }

    public void takeTurn() {
        System.out.println("get new people - exchange with bus when it passes by");
    }
}