package group_a7_8;

import edu.gatech.Stop;

public class PathKey {
	private Stop origin;
	private Stop destination;


	public PathKey(Stop origin, Stop destination) {
		super();
		this.origin = origin;
		this.destination = destination;
	}


	public Stop getOrigin() {
		return origin;
	}


	public Stop getDestination() {
		return destination;
	}


}
