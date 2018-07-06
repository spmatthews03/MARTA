package group_a7_8;

public class Hazard {
	private double delayFactor;
	private PathKey pathKey;	
	
	public Hazard(PathKey pathKey, double delayFactor) {
		super();
		this.delayFactor = delayFactor;
		this.pathKey = pathKey;
	}

	public double getDelayFactor() {
		return delayFactor;
	}
	public PathKey getPathKey() {
		return pathKey;
	}
	
}
