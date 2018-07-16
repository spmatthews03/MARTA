package edu.gatech;

import java.awt.Point;
import java.util.Objects;

public abstract class Facility {
	protected Integer uniqueID;
	protected Point coord;
	protected String facilityName;
	private String type;

	public Facility(){
		//default
	}

	public Facility(int uniqueID, String name, double x, double y, String type) {
		this.uniqueID = uniqueID;
		this.facilityName = name;
		this.coord = new Point();
		//System.out.println(x + " " + y);
		this.coord.setLocation(x, y);
		this.type = type;
	}
	public Point getLocation() {
		return coord.getLocation();
	}
	public void setLocation(double x, double y) {
		coord.setLocation(x, y);
	}
	public Integer get_uniqueID() {
		return uniqueID;
	}
	public String getFacilityName() {
		return facilityName;
	}
	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

	public String getType() {
		return type;
	}

	public void displayEvent() {
	}
	public void takeTurn() {
	}

	public void displayInternalStatus() {
      
        System.out.print("> " + this.type + " - ID: " + Integer.toString(uniqueID) + " name: " + facilityName);
        System.out.println(" xCoord: " + Integer.toString(coord.x) + " yCoord: " + Integer.toString(coord.y));
        
	}

	public double findDistance(Facility destination) {
		/*
		Double x_coord_sqrt;
		Double y_coord_sqrt;

		x_coord_sqrt = (destination.getLocation().getX() - this.getLocation().getX());
		x_coord_sqrt = x_coord_sqrt * x_coord_sqrt;

		y_coord_sqrt = (destination.getLocation().getY() - this.getLocation().getY());
		y_coord_sqrt = y_coord_sqrt * y_coord_sqrt;
		
		return Math.sqrt(x_coord_sqrt + y_coord_sqrt);
		*/
		
        final double distanceConversion = 70.0;
        
        return distanceConversion * Math.sqrt(Math.pow((this.getLocation().getX() - destination.getLocation().getX()), 2) + Math.pow((this.getLocation().getY() - destination.getLocation().getY()), 2));
	}

	public boolean equals(Object object) {
	    if(object instanceof Facility && ((Facility)object).get_uniqueID() == this.get_uniqueID()) {
	        return true;
	    } else {
	        return false;
	    }
	}

    @Override
    public int hashCode() {
    	return Objects.hash(this.get_uniqueID(), this.getFacilityName(),
    						this.getLocation().getX(), this.getLocation().getY());
    }
}