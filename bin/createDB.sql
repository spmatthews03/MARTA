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

CREATE TABLE PATH (
  id              	SERIAL PRIMARY KEY,
  originID          int,
  destinationID     int,
  originType        char(50),
  destinationType   char(50),
  speedLimit        numeric,
  isBlocked         char(20),
  type              char(50)
);

CREATE TABLE HAZARD (
  id              	SERIAL PRIMARY KEY,
  originID          int,
  destinationID     int,
  originType        char(50),
  destinationType   char(50),
  delayFactor   	  numeric NOT NULL,
  type              char(50) NOT NULL
);

CREATE TABLE FUELCONSUMPTION (
  id              	SERIAL PRIMARY KEY,
  timeRank		  	  int NOT NULL,
  amount      	  	numeric NOT NULL,
  passengers  	    int NOT NULL,
  vehicleID         int,
  vehicleType       char(50),
  originID          int,
  destinationID     int,
  originType        char(50),
  destinationType   char(50),
  type              char(50) NOT NULL
);

CREATE TABLE EVENT (
 id                  SERIAL PRIMARY KEY,
 eventID             int NOT NULL,
 type                char(50),
 timeRank            int NOT NULL,
 speedLimit          numeric,
 delayFactor         numeric,
 deltaStallDuration  int,
 repairDuration      int,
 originID            int,
 destinationID       int,
 originType          char(50),
 destinationType     char(50),
 vehicleID           int,
 vehicleType         char(50),
 exchangePointID     int,
 exchangePointType   char(50)
);

CREATE TABLE ROUTEDEFINITION (
 id                  SERIAL PRIMARY KEY,
 routeType           char(50) NOT NULL,
 routeID             int NOT NULL,
 seqno               int NOT NULL,
 stopType            char(50) NOT NULL,
 stopID              int NOT NULL,
 type                char(50) NOT NULL
 );
