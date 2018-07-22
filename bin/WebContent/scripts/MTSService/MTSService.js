(function(){

var service = function ($log, $timeout, $interval, $http, $rootScope){
   //$log.info("MTS Service");
    
   var state = {
			time:0,
	    	vehicles:[],
	        routes:[],
	        stops:[],
	        paths:[],
	        events:[],
	        commands:[],
	        commandsQueue:[],
	        reports:{vehicles:[]},
	  	  	editMode:false,
	  	  	priorSim:false,
	  	  	holdCommands:false,
	  	  	fuelByBusData:{},
	  		commandOption:""
   };
	
   var ws;
   var post = function(message){
       if(ws && ws.readyState === ws.OPEN) ws.send(message);
       //else $log.info('ws not valid');
   };
   var heartbeat=function(){
    	//$log.info('heartbeat' + Date.now());
    	post('{\"messageType\":\"heartbeat\"}')
   };
   commandBlocked = false;

    
   var sendCommands = function(){
	   if(!state.holdCommands && !commandBlocked && state.commandsQueue.length>0){
		   //$log.info(state.commandsQueue.length+" commands to send");
		   var command = state.commandsQueue.shift();
		   $log.info('sending :'+command);
		   //$log.info('url: '+'/api/MTS/command?line=' + command);
	    	var promise = $http.get('/api/MTS/command?line=' + command);
	    	promise.then(
	    	          function(payload) { 
	    	        	  //$log.info('service call returned:', payload);
	    	          },
	    	          function(errorPayload) {
	    	              $log.error('failure error:', errorPayload);
	    	          });
		   commandBlocked = true;
	   }
   }
   var process = function(update){
      //$log.info(update);
	  if(update.hasOwnProperty('time')){
		  state.time = update.time;
	  }
	  if(update.hasOwnProperty('system')){
		  if(update.system.hasOwnProperty('vehicles') && update.system.vehicles.length>0){
		     state.vehicles.splice(0, state.vehicles.length);
		     update.system.vehicles.forEach(function(vehicle){
				    state.vehicles.push(vehicle);
		     });
		  }
		  if(update.system.hasOwnProperty('routes') && update.system.routes.length>0){
		     state.routes.splice(0, state.routes.length);
		     update.system.routes.forEach(function(route){
				    state.routes.push(route);
		     });
		  }
		  if(update.system.hasOwnProperty('stops') && update.system.stops.length>0){
		   	 state.stops.splice(0, state.stops.length);
		     update.system.stops.forEach(function(stop){
				    state.stops.push(stop);
		     });
		   }
	  	   if(update.system.hasOwnProperty('paths') && update.system.paths.length>0){
		     state.paths.splice(0, state.paths.length);
		     update.system.paths.forEach(function(path){
		    	    path.origin = getStop(path.pathKey.originType,path.pathKey.origin);
		    	    path.destination = getStop(path.pathKey.destinationType,path.pathKey.destination);
				    state.paths.push(path);
		     });
		   }
	   }
  	   if(update.hasOwnProperty('events') && update.events.length>0){
  		  state.events.splice(0, state.events.length);
 	      update.events.forEach(function(event){
			    state.events.push(event);
 	      });
  	   }
  	   if(update.hasOwnProperty('reports') && update.reports.length>0){
  		   //$log.info(update);
  		   state.reports.vehicles.splice(0,state.reports.vehicles.length);
    		 state.reports.minAmount=0;
      		 state.reports.maxAmount = 0;
      		 state.reports.totalAmount = 0;
      		 state.reports.minPassengers=0;
      		 state.reports.maxPassengers = 0;
      		 state.reports.totalPassengers = 0;
  		   var maxPassengers = 0;
  		   var maxAmount = 0;
  		   var totalPeople = 0;
  		   var totalAmount = 0;
  		   for(var r=0;r<update.reports.length;r++){
  			   var report = update.reports[r];
  			   var vehicleAmountTotal = 0;
  			   
  			   for(var i=0;i<report.reports.length;i++){
  				 vehicleAmountTotal = vehicleAmountTotal + report.reports[i].amount;
  			   }
  			   report.amount = vehicleAmountTotal;
  			   if(report.amount>maxAmount) maxAmount = report.amount;
  			   totalAmount = totalAmount + report.amount;
  			   
  			   var vehiclePeopleTotal = 0;
  			   for(var i=0;i<report.reports.length;i++){
  				   vehiclePeopleTotal = vehiclePeopleTotal + report.reports[i].passengers;
  			   }
  			   report.passengers = vehiclePeopleTotal;
  			   if(report.maxPassengers>totalPeople) maxPassengers = report.passengers;
  			   totalPeople = totalPeople + report.passengers;

  			   
  			   report.name = report.vehicle.type+" #"+report.vehicle.ID;
  			   state.reports.vehicles.push(report);
//  			   $log.info('report:');
//  			   $log.info(report);
  		   }
  		 state.reports.minAmount=0;
  		 state.reports.maxAmount = maxAmount;
  		 state.reports.totalAmount = totalAmount;
  		 state.reports.minPassengers=0;
  		 state.reports.maxPassengers = maxPassengers;
  		 state.reports.totalPassengers = totalPeople;
  		 
  		 state.fuelByBusData.min=state.reports.minAmount;
  		 state.fuelByBusData.max=state.reports.maxAmount;
  		 state.fuelByBusData.total=state.reports.totalAmount;
  		 state.fuelByBusData.items=state.reports.vehicles;
  	   }
  	   commandBlocked = false;
   };
   // ws = new WebSocket('ws://127.0.0.1:5808');
   var onopen = function(){
	  //$log.info('socket opened!');
	  $interval(heartbeat,1000);
	  $interval(sendCommands,10);
   };
		    
   var onmessage = function(evt){
  	  //console.log('received socket message: '+evt.data);
        $timeout(function(){
            //$log.info('processing: ');
            //$log.info(evt.data);
        	process(JSON.parse(evt.data));
        });
    };
    var onclose = function(){
  	  //console.log('socket closed.');
    };
    var onerror = function(err){
  	  //console.log('ERROR: '+err)
    };
    var connect = function(){
	    //open a socket to the server
	    //console.log('initializing web socket client...');
	    ws = new WebSocket('ws://localhost:6310');
	    ws.onopen = onopen;
	    ws.onmessage = onmessage;
	    ws.onclose = onclose;
	    ws.onerror = onerror;  
    };
		    
    var executeCommand = function(command){
    	//$log.info('executing command: '+command);
    	if(command.startsWith('step')){
    		//$log.info('need to switch to sim execution mode');
    		$rootScope.setExecMode(true);
    	}
    	state.commandsQueue.push(command);
    	
    };
    var getStop = function(stopType,stopID){
    	var result = state.stops.find(function(stop){
    		return (stopType==stop.type && stopID==stop.ID);
    	});
    	return result;
    };
    var getPath = function(origin,destination){

    	var result = state.paths.find(function(path){
    		return (path.pathKey.origin==origin.ID && path.pathKey.originType==origin.type &&
    				path.pathKey.destination==destination.ID && path.pathKey.destinationType==destination.type);
    	});
    	return result;
    };
    var getPathVehicle=function(stopType,originStopID,destinationStopID,route){
    	$log.info(route);
    	$log.info("stopType: "+stopType +", originStopID: "+originStopID+", destinationStopID: "+destinationStopID);
    	var vehicleType = ((stopType=='busStop')?'Bus':'Train');
    	var result;
    	result = state.vehicles.find(function(vehicle){
    		if(vehicle.routeID!=route.ID) return false;
    		//$log.info(vehicle);
    		var locationID = vehicle.prevLocation;
    		var nextLocationID = vehicle.nextLocation;
    		var vehicleStopID = route.stops[locationID];
    		var vehicleNextStopID = route.stops[nextLocationID];
    		//var vehicleStop = getStop(stopType,vehicleStopID);
    		$log.info(vehicleStopID);
    		$log.info(vehicleNextStopID);
    		return (vehicle.type==vehicleType && vehicleStopID == originStopID && vehicleNextStopID == destinationStopID );
    	});
    	return result;
    };
    var getVehicleEvent=function(vehicleType,vehicleID){
    	//$log.info("vehicleType: "+vehicleType +", vehicleID: "+vehicleID);
    	var result;
    	result = state.events.find(function(event){
    		if ( !event.hasOwnProperty('vehicle') ) {
    		    return false;
    		}
    		if(event.vehicle.type!=vehicleType) return false;
    		//$log.info(event);
    		return event.vehicle.ID == vehicleID;
    	});
    	return result;
    };

    var reset = function(){
    	//$log.info('resetting system');
    	/*
    	 * 			time:0,
	    	vehicles:[],
	        routes:[],
	        stops:[],
	        paths:[],
	        events:[],
	        commands:[],
	        commandsQueue:[],
	        reports:[],
	  	  	editMode:false,
	  	  	priorSim:false,
	  		commandOption:""
    	 */
    	state.time=0;
    	state.vehicles.splice(0,state.vehicles.length);
    	state.routes.splice(0,state.routes.length);
    	state.stops.splice(0,state.stops.length);
    	state.paths.splice(0,state.paths.length);
    	state.events.splice(0,state.events.length);
    	state.commands.splice(0,state.commands.length);
    	state.commandsQueue.splice(0,state.commandsQueue.length);
    	state.reports.vehicles.splice(0,state.reports.length);
		 state.reports.minAmount=0;
  		 state.reports.maxAmount = 0;
  		 state.reports.totalAmount = 0;
  		 state.reports.minPassengers=0;
  		 state.reports.maxPassengers = 0;
  		 state.reports.totalPassengers = 0;
  		 editMode:false;
  	  	priorSim:false;
  		commandOption:"";
    	//$log.info('reset completed')
    	//$log.info(state);
    };

    connect();
    
    var countBus = function(){
    	var c=0;
       for(var i=0;i<state.vehicles.length;i++){
    	   if(state.vehicles[i].type=='Bus')  c++;
       }	
       return c;
    };
    var countBusStop = function(){
    	var c=0;
       for(var i=0;i<state.stops.length;i++){
    	   if(state.stops[i].type=='busStop')  c++;
       }	
       return c;
    };
    var countBusRoute = function(){
    	var c=0;
       for(var i=0;i<state.routes.length;i++){
    	   if(state.routes[i].type=='busRoute')  c++;
       }	
       return c;
    };
    var countTrain = function(){
    	var c=0;
       for(var i=0;i<state.vehicles.length;i++){
    	   if(state.vehicles[i].type=='Train')  c++;
       }	
       return c;
    };
    var countTrainStop = function(){
    	var c=0;
       for(var i=0;i<state.stops.length;i++){
    	   if(state.stops[i].type=='railStop')  c++;
       }	
       return c;
    };
    var countTrainRoute = function(){
    	var c=0;
       for(var i=0;i<state.routes.length;i++){
    	   if(state.routes[i].type=='railRoute')  c++;
       }	
       return c;
    };
    var countDepot = function(){
    	var c=0;
       for(var i=0;i<state.stops.length;i++){
    	   if(state.stops[i].type=='Depot')  c++;
       }	
       return c;
    };
    var countMoveEvent = function(){
    	var c=0;
       for(var i=0;i<state.events.length;i++){
    	   if(state.events[i].type=='move_bus' || state.events[i].type=='move_train')  c++;
       }	
       return c;
    };
    
   
    return {
    	state: state,
    	executeCommand: executeCommand,
    	getStop:getStop,
    	getPath:getPath,
    	getPathVehicle:getPathVehicle,
    	getVehicleEvent:getVehicleEvent,
    	reset:reset,
    	countBus:countBus,
    	countBusStop:countBusStop,
    	countBusRoute:countBusRoute,
    	countTrain:countTrain,
    	countTrainStop:countTrainStop,
    	countTrainRoute:countTrainRoute,
    	countDepot:countDepot,
    	countMoveEvent:countMoveEvent
    };
    
  };
  

  

angular.module('MTSService',[])
  .factory ("MTSService", ['$log', '$timeout', '$interval', '$http', '$rootScope',service]);  
}());
