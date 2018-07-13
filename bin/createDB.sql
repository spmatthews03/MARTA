CREATE TABLE STOP(
  id              	SERIAL PRIMARY KEY,
  type				char(20) NOT NULL,
  stopLogicalID		int NOT NULL,
  x          		numeric NOT NULL,
  y          		numeric NOT NULL,
  NAME              CHAR(50) NOT NULL
);

CREATE TABLE ROUTE(
  id              	SERIAL PRIMARY KEY,
  type				char(20) NOT NULL,
  routeLogicalID	int NOT NULL,
  routenumber		int NOT NULL,
  NAME              CHAR(50) NOT NULL
);

CREATE TABLE VEHICLE (
  id              	SERIAL PRIMARY KEY,
  type            	char(20),
  vehicleLogicalID	int NOT NULL,
  routeLogicalID  	int NOT NULL,
  nextLocation	  	int NOT NULL,
  prevLocation	  	int NOT NULL,
  passengers	  	int NOT NULL,
  capacity		  	int NOT NULL,
  fuellevel		  	numeric NOT NULL,
  fuelcapacity	  	numeric NOT NULL,
  speed			  	int NOT NULL
);
