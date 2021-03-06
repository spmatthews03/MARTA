package edu.gatech;

import java.util.Hashtable;
import java.util.Random;

public abstract class ExchangePoint extends Facility {
	protected Random randGenerator;
	protected Integer waiting;
	protected boolean out_of_service;
    private Hashtable<Integer, int[]> rateCatching;
    private Hashtable<Integer, int[]> rateLeaving;

	public ExchangePoint(){
		//default constructor
	}

	public ExchangePoint(int uniqueID, String name, double x, double y, String type) {
		super(uniqueID, name, x, y,type);
		waiting = 0;
		out_of_service = false;
		randGenerator = new Random();
		this.setRateCatching(new Hashtable<Integer, int[]>());
        this.setRateLeaving(new Hashtable<Integer, int[]>());
	}

	public Integer randomBiasedValue(Integer lower, Integer middle, Integer upper) {
        int lowerRange = randGenerator.nextInt(middle - lower + 1) + lower;
        int upperRange = randGenerator.nextInt(upper - middle + 1) + middle;
        return (lowerRange + upperRange) / 2;
	}

    public void set_riders(int num_riders) {
    	waiting = num_riders;
    }
    public void add_riders(int moreRiders) {
    	waiting = waiting + moreRiders;
    }
	public Integer get_riders() {
		return waiting;
	}

	public void addArrivalInfo(Integer timeSlot, Integer minOn, Integer avgOn, Integer maxOn,
							   Integer minOff, Integer avgOff, Integer maxOff) {
	}

    public boolean get_out_of_service() {
    	return out_of_service;
    }
    public void set_out_of_service(boolean value) {
    	out_of_service = value;
    }

	public Hashtable<Integer, int[]> getRateCatching() {
		return rateCatching;
	}

	public void setRateCatching(Hashtable<Integer, int[]> rateCatching) {
		this.rateCatching = rateCatching;
	}

	public Hashtable<Integer, int[]> getRateLeaving() {
		return rateLeaving;
	}

	public void setRateLeaving(Hashtable<Integer, int[]> rateLeaving) {
		this.rateLeaving = rateLeaving;
	}
	
    @Override
	public void displayInternalStatus() {
	      
        System.out.print("> " + this.getType() + " - ID: " + Integer.toString(this.uniqueID) + " name: " + this.facilityName);
        System.out.print(" waiting: " + Integer.toString(waiting));
        System.out.println(" xCoord: " + Double.toString(this.coord.getX()) + " yCoord: " + Double.toString(this.coord.getY()));
        
	}

    public Integer exchangeRiders(int rank, int initialPassengerCount, int capacity) {
        int hourOfTheDay = (rank / 60) % 24;
        int ableToBoard;
        int[] leavingBusRates, catchingBusRates;
        int[] filler = new int[]{0, 1, 1};

        // calculate expected number riders leaving the bus
        if (rateLeaving.containsKey(hourOfTheDay)) { leavingBusRates = rateLeaving.get(hourOfTheDay); }
        else { leavingBusRates = filler; }
        int leavingBus = randomBiasedValue(leavingBusRates[0], leavingBusRates[1], leavingBusRates[2]);

        // update the number of riders actually leaving the bus versus the current number of passengers
        int updatedPassengerCount = Math.max(0, initialPassengerCount - leavingBus);

        // calculate expected number riders leaving the bus
        if (rateCatching.containsKey(hourOfTheDay)) { catchingBusRates = rateCatching.get(hourOfTheDay); }
        else { catchingBusRates = filler; }
        int catchingBus = randomBiasedValue(catchingBusRates[0], catchingBusRates[1], catchingBusRates[2]);

        // determine how many of the currently waiting and new passengers will fit on the bus
        int tryingToBoard = waiting + catchingBus;
        int availableSeats = capacity - updatedPassengerCount;

        // update the number of passengers left waiting for the next bus
        if (tryingToBoard > availableSeats) {
            ableToBoard = availableSeats;
            waiting = tryingToBoard - availableSeats;
        } else {
            ableToBoard = tryingToBoard;
            waiting = 0;
        }

        // update the number of riders actually catching the bus and return the difference from the original riders
        int finalPassengerCount = updatedPassengerCount + ableToBoard;
        return finalPassengerCount - initialPassengerCount;
    }
}
