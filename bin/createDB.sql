CREATE TABLE STOP(
  id              	SERIAL PRIMARY KEY,
  type				      varchar(50) NOT NULL,
  stopLogicalID		  int NOT NULL,
  NAME              varCHAR(50) NOT NULL,
  waiting           int,
  x          		    numeric NOT NULL,
  y          		    numeric NOT NULL
);

CREATE TABLE ROUTE(
  id              	SERIAL PRIMARY KEY,
  type				      varchar(50) NOT NULL,
  routeLogicalID	  int NOT NULL,
  routenumber		    int NOT NULL,
  NAME              varCHAR(50) NOT NULL
);

CREATE TABLE VEHICLE (
  id              	SERIAL PRIMARY KEY,
  type            	varchar(20),
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
  id              	 SERIAL PRIMARY KEY,
  originID           int,
  destinationID      int,
  originType         varchar(50),
  destinationType    varchar(50),
  speedLimit         numeric,
  deltaStallDuration numeric,
  isBlocked          varchar(20),
  type               varchar(50)
);

CREATE TABLE HAZARD (
  id              	SERIAL PRIMARY KEY,
  originID          int,
  destinationID     int,
  originType        varchar(50),
  destinationType   varchar(50),
  delayFactor   	  numeric NOT NULL,
  type              varchar(50) NOT NULL
);

CREATE TABLE FUELCONSUMPTION (
  id              	SERIAL PRIMARY KEY,
  timeRank		  	  int NOT NULL,
  amount      	  	numeric NOT NULL,
  passengers  	    int NOT NULL,
  vehicleID         int,
  vehicleType       varchar(50),
  originID          int,
  destinationID     int,
  originType        varchar(50),
  destinationType   varchar(50),
  type              varchar(50) NOT NULL
);

CREATE TABLE EVENT (
 id                  SERIAL PRIMARY KEY,
 eventID             int NOT NULL,
 type                varchar(50),
 timeRank            int NOT NULL,
 speedLimit          numeric,
 delayFactor         numeric,
 deltaStallDuration  int,
 repairDuration      int,
 originID            int,
 destinationID       int,
 originType          varchar(50),
 destinationType     varchar(50),
 vehicleID           int,
 vehicleType         varchar(50),
 exchangePointID     int,
 exchangePointType   varchar(50)
);

CREATE TABLE ROUTEDEFINITION (
 id                  SERIAL PRIMARY KEY,
 routeType           varchar(50) NOT NULL,
 routeID             int NOT NULL,
 seqno               int NOT NULL,
 stopType            varchar(50) NOT NULL,
 stopID              int NOT NULL,
 type                varchar(50) NOT NULL
 );
