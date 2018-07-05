(function(){

	 	
  var service = function ($log, $timeout, $http){
    //$log.info("MTS Service");
    
	var state = {
			time:0,
	    	vehicles:[],
	        routes:[],
	        stops:[],
	        events:[]
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
		  	  if(update.events && update.events.length>0){
		  		 //$log.info('updating events');
		  		state.events.splice(0, state.events.length);
		  		for(event in update.events){
		  			state.events.push(event);
		  		}
		  	  }
		  	  //$log.info(state);
		    };
		    // ws = new WebSocket('ws://127.0.0.1:5808');
		    var onopen = function(){
		  	  //console.log('socket opened!');
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
		    connect = function(){
			    //open a socket to the server
			    //console.log('initializing web socket client...');
			    var ws = new WebSocket('ws://localhost:6310');
			    ws.onopen = onopen;
			    ws.onmessage = onmessage;
			    ws.onclose = onclose;
			    ws.onerror = onerror;  
		    };
		    
    var executeCommand = function(command){
    	$log.info('executing command: '+command);
    	//$log.info('url: '+'/api/MTS/command?line=' + command);
    	var promise = $http.get('/api/MTS/command?line=' + command);
    	promise.then(
    	          function(payload) { 
    	        	  //$log.info('service call returned:', payload);
    	          },
    	          function(errorPayload) {
    	              $log.error('failure error:', errorPayload);
    	          });
    	}
    connect();
    
    
    return {
    	state: state,
    	executeCommand: executeCommand
    };
    
  };
  

  

angular.module('MTSService',[])
  .factory ("MTSService", ['$log', '$timeout', '$http', service]);  
}());
