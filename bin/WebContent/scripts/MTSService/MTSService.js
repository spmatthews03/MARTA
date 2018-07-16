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
	  	  	editMode:false,
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
	   if(!commandBlocked && state.commandsQueue.length>0){
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
      $log.info(update);
	  state.time = update.time;
	  if(update.system.vehicles && update.system.vehicles.length>0){
	     state.vehicles.splice(0, state.vehicles.length);
	     update.system.vehicles.forEach(function(vehicle){
			    state.vehicles.push(vehicle);
	     });
	  }
	  if(update.system.routes && update.system.routes.length>0){
	     state.routes.splice(0, state.routes.length);
	     update.system.routes.forEach(function(route){
			    state.routes.push(route);
	     });
	  }
	  if(update.system.stops && update.system.stops.length>0){
	   	 state.stops.splice(0, state.stops.length);
	     update.system.stops.forEach(function(stop){
			    state.stops.push(stop);
	     });
	   }
  	   if(update.system.paths && update.system.paths.length>0){
	     state.paths.splice(0, state.paths.length);
	     update.system.paths.forEach(function(path){
	    	    path.origin = getStop(path.pathKey.originType,path.pathKey.origin);
	    	    path.destination = getStop(path.pathKey.destinationType,path.pathKey.destination);
			    state.paths.push(path);
	     });
	   }
  	   if(update.events && update.events.length>0){
  		  state.events.splice(0, state.events.length);
 	      update.events.forEach(function(event){
			    state.events.push(event);
 	      });
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
    var getStopVehicle=function(stopType,stopID){
    	$log.info("stopType: "+stopType, +", stopID: "+stopID);
    	var result;
    	result = state.vehicles.find(function(vehicle){
    		$log.event(vehicle);
    		return false;
    	});
    	return result;
    };
    var getVehicleEvent=function(vehicleType,vehicleID){
    	$log.info("vehicleType: "+vehicleType, +", vehicleID: "+vehicleID);
    	var result;
    	result = state.events.find(function(event){
    		$log.event(event);
    		return false;
    	});
    	return result;
    };
    
    
    connect();
   
    return {
    	state: state,
    	executeCommand: executeCommand,
    	getStop:getStop,
    	getPath:getPath,
    	getStopVehicle:getStopVehicle,
    	getVehicleEvent:getVehicleEvent
    };
    
  };
  

  

angular.module('MTSService',[])
  .factory ("MTSService", ['$log', '$timeout', '$interval', '$http', '$rootScope',service]);  
}());
