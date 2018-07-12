(function(){

var service = function ($log, $timeout, $interval, $http){
   //$log.info("MTS Service");
    
   var state = {
			time:0,
	    	vehicles:[],
	        routes:[],
	        stops:[],
	        paths:[],
	        events:[],
	        commands:[],
	        commandsQueue:[]
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
		   $log.info(state.commandsQueue.length+" commands to send");
		   var command = state.commandsQueue.shift();
		   $log.info('sending :'+command);
		   $log.info('url: '+'/api/MTS/command?line=' + command);
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
	  state.time = update.time;
	  if(update.system.buses && update.system.buses.length>0){
	     //$log.info('updating buses');
	     state.vehicles.splice(0, state.vehicles.length);
	     for(vehicle in update.system.buses){
		    state.vehicles.push(vehicle);
		 }
	  }
	  if(update.system.routes && update.system.routes.length>0){
	     //$log.info('updating routes');
		 state.routes.splice(0, state.routes.length);
		 for(route in update.system.routes){
		    state.routes.push(route);
		 }
	  }
	  if(update.system.stops && update.system.stops.length>0){
	     //$log.info('updating stops');
	     //$log.info(update.system.stops.length+' stops to add');
	   	 state.stops.splice(0, state.stops.length);
		 for(stop in update.system.stops){
		 	state.stops.push(stop);
		 }
	   }
  	   if(update.system.paths && update.system.paths.length>0){
	      $log.info('updating paths');
	  	  state.paths.splice(0, state.paths.length);
	  	  for(path in update.system.paths){
	  	     state.paths.push(path);
	  	  }
	   }
  	   if(update.events && update.events.length>0){
  	      //$log.info('updating events');
  		  state.events.splice(0, state.events.length);
  		  for(event in update.events){
  		     state.events.push(event);
  		  }
  	   }
	   //$log.info(state);
  	   commandBlocked = false;
   };
   // ws = new WebSocket('ws://127.0.0.1:5808');
   var onopen = function(){
	  $log.info('socket opened!');
	  $interval(heartbeat,1000);
	  $interval(sendCommands,100);
   };
		    
   var onmessage = function(evt){
  	  //console.log('received socket message: '+evt.data);
        $timeout(function(){
            //$log.info('processing '+evt.data);
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
    	$log.info('executing command: '+command);
    	state.commandsQueue.push(command);
    	
    };
    connect();
   
    return {
    	state: state,
    	executeCommand: executeCommand
    };
    
  };
  

  

angular.module('MTSService',[])
  .factory ("MTSService", ['$log', '$timeout', '$interval', '$http', service]);  
}());
