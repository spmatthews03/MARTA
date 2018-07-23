package group_a7_8;

import edu.gatech.Facility;
import edu.gatech.VehicleRoute;

public class RouteDefinition {
	private VehicleRoute route;
	private int order;
	private Facility facility;
	public RouteDefinition(VehicleRoute route, int order, Facility facility) {
		super();
		this.route = route;
		this.order = order;
		this.facility = facility;
	}
	public VehicleRoute getRoute() {
		return route;
	}
	public void setRoute(VehicleRoute route) {
		this.route = route;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public Facility getFacility() {
		return facility;
	}
	public void setFacility(Facility facility) {
		this.facility = facility;
	}

}
