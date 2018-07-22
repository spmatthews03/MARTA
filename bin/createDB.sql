CREATE TABLE STOP(
  id              	SERIAL PRIMARY KEY,
  type				      char(20) NOT NULL,
  stopLogicalID		  int NOT NULL,
  NAME              CHAR(50) NOT NULL,
  waiting           int NOT NULL,
  x          		    numeric NOT NULL,
  y          		    numeric NOT NULL

);

CREATE TABLE ROUTE(
  id              	SERIAL PRIMARY KEY,
  type				      char(20) NOT NULL,
  routeLogicalID	  int NOT NULL,
  routenumber		    int NOT NULL,
  NAME              CHAR(50) NOT NULL
);

CREATE TABLE VEHICLE (
  id              	SERIAL PRIMARY KEY,
  type            	char(20),
  vehicleLogicalID	int NOT NULL,
  routeLogicalID  	int NOT NULL,
  location  	  	  int NOT NULL,
  passengers	  	  int NOT NULL,
  capacity		  	  int NOT NULL,
  fuellevel		  	  numeric,
  fuelcapacity	  	numeric,
  speed			  	    int NOT NULL
);

CREATE TABLE HAZARD (
  id              	SERIAL PRIMARY KEY,
  delayFactor   	  numeric NOT NULL
);

CREATE TABLE FUELCONSUMPTION (
  id              	SERIAL PRIMARY KEY,
  timeRank		  	  int NOT NULL,
  amount      	  	numeric NOT NULL,
  passengers  	    int NOT NULL
);

