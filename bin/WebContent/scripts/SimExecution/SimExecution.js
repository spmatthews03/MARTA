// MTS SimExec Module
(function(){
  //Directives
  var simRouteViewerDirective = function(){
      return{
		 restrict:'E',
		 scope:false,
		 replace: true,
         templateUrl: 'scripts/SimExecution/SimRouteViewer.html'
	  }
  };
  var simEntityViewerDirective = function(){
      return{
		 restrict:'E',
		 scope:false,
		 replace: true,
         templateUrl: 'scripts/SimExecution/SimEntityViewer.html'
	  }
  };
  var stopDirective = function(){
      return{
		 restrict:'E',
		 scope:{stop:"="},
		 replace: true,
		 controller: 'stopController',
         templateUrl: 'scripts/SimExecution/stop.html'
	  }
   };
  var routeDirective = function(){
	      return{
			 restrict:'E',
			 scope:{route:"="},
			 replace: true,
			 controller: 'routeController',
	         templateUrl: 'scripts/SimExecution/route.html'
		  }
  };   
  var pathDirective = function(){
      return{
		 restrict:'E',
		 scope:{path:"="},
		 replace: true,
		 controller: 'pathController',
         templateUrl: 'scripts/SimExecution/path.html'
	  }
  };
  var vehicleDirective = function(){
      return{
		 restrict:'E',
		 scope:{vehicle:"="},
		 replace: true,
		 controller: 'vehicleController',
         templateUrl: 'scripts/SimExecution/vehicle.html' 
	  }
  };

  //route viewer directives
  var routeStopDirective = function(){
      return{
		 restrict:'E',
		 scope:{stop:"=",order:"="},
		 replace: true,
		 controller: 'stopController',
         templateUrl: 'scripts/SimExecution/route_stop.html'
	  }
   };
  var routeRouteDirective = function(){
	      return{
			 restrict:'E',
			 scope:{route:"="},
			 replace: true,
			 controller: 'routeController',
	         templateUrl: 'scripts/SimExecution/route_route.html'
		  }
  };   
  var routePathDirective = function(){
      return{
		 restrict:'E',
		 scope:{path:"="},
		 replace: true,
		 controller: 'pathController',
         templateUrl: 'scripts/SimExecution/route_path.html'
	  }
  };
  var routeVehicleDirective = function(){
      return{
		 restrict:'E',
		 scope:{vehicle:"="},
		 replace: true,
		 controller: 'vehicleController',
         templateUrl: 'scripts/SimExecution/route_vehicle.html' 
	  }
  };  
  var eventDirective = function(){
      return{
		 restrict:'E',
		 scope:{event:"="},
		 replace: true,
		 controller: 'eventController',
         templateUrl: 'scripts/SimExecution/event.html' 
	  }
};   
  
  
   var simDirective = function(){
	  	return{
	  		restrict:'E',
			scope:{},
			controller: 'simController',
	        replace: true,
            templateUrl: 'scripts/SimExecution/SimMain.html' 

        }
  };
  //controllers
  var simController = function($scope, $log, mtsService){
		//$log.info('simController');
		$scope.mts = mtsService.state;
		var x=0;
		$scope.depotFill = function(){
			//$log.info('depoFill');
			$log.info("x="+x);
			switch(x){
			case 0:
				if(mtsService.state.vehicles.length>0){
					mtsService.state.vehicles[0].outOfService = true;
				}
				if(mtsService.state.stops.length>0){
					mtsService.state.stops[0].outOfService = true;
				}
				if(mtsService.state.paths.length>0){
					mtsService.state.paths[0].blocked=true;
				}
				x++;
				if(x>4) x=0;
				break;
			case 1:
				if(mtsService.state.vehicles.length>0){
					mtsService.state.vehicles[0].refueling = true;
				}
				if(mtsService.state.stops.length>0){
					mtsService.state.stops[0].outOfService = false;
				}
				if(mtsService.state.paths.length>0){
					mtsService.state.paths[0].blocked=false;
					mtsService.state.paths[0].speedLimit=20;
				}
				x++;
				if(x>4) x=0;
				break;
			case 2:
				if(mtsService.state.vehicles.length>0){
					mtsService.state.vehicles[0].outOfService = false;
					mtsService.state.vehicles[0].refueling = false;
				}
				if(mtsService.state.paths.length>0){
					mtsService.state.paths[0].blocked=false;
					mtsService.state.paths[0].speedLimit=-1;
					mtsService.state.paths[0].delayfactor=1.3;
				}
				x++;
				if(x>4) x=0;
				break;
			case 3:
				if(mtsService.state.paths.length>0){
					mtsService.state.paths[0].delayfactor=1.3;
					mtsService.state.paths[0].speedLimit=20;
					mtsService.state.paths[0].blocked=true;
				}
				x++;
				if(x>4) x=0;
				break;
			case 4:
				if(mtsService.state.paths.length>0){
					mtsService.state.paths[0].delayfactor=1;
					mtsService.state.paths[0].speedLimit=-1;
					mtsService.state.paths[0].blocked=false;
				}
				x++;
				if(x>4) x=0;
				break;
			}
			if(mtsService.state.paths.length>0){
				$log.info(mtsService.state.paths[0]);
			}
		};
		
		$scope.stepOnce=function(){
			var command = 'step_once';
			  //$log.info('command: '+command);
			  mtsService.state.commands.push({index:mtsService.state.commands.length,line:command,processed:false});
			  mtsService.executeCommand(command);
		};
		$scope.stepMulti = function(stepSize){
			var command = 'step_multi,'+stepSize;
			  //$log.info('command: '+command);
			  mtsService.state.commands.push({index:mtsService.state.commands.length,line:command,processed:false});
			  mtsService.executeCommand(command);
		};		
		$scope.stop = function(){
			var command = 'quit';
			  //$log.info('command: '+command);
			  mtsService.state.commands.push({index:mtsService.state.commands.length,line:command,processed:false});
			  mtsService.executeCommand(command);
		};	
		  $scope.time = mtsService.state.time;

       $scope.getDepotCount = function(){
    	   var c = 0;
    	   for(var i=0;i<mtsService.state.vehicles.length;i++){
    		   if(mtsService.state.vehicles[i].outOfService) c++;
    	   }
    	   return c;
       };
  };
  
  var stopController = function($scope, $log,mtsService){
		//$log.info('stopController');
	  $scope.debug = false;
	  $scope.time = mtsService.state.time;

	  $scope.$watch('time',function(){
		  //$log.info('stop '+$scope.stop.name+' noticed time changed');
		  //$log.info('time: ',$scope.time);
		  //$log.info($scope.stop);

		  if(!(typeof $scope.stop === "undefined") && !(typeof $scope.stop.stop === "undefined") ){
			  $scope.stop.vehicleAtStop.splice(0,$scope.stop.vehicleAtStop);
			  var vehicle = mtsService.getStopVehicle($scope.stop.stop.type,$scope.stop.stop.ID,$scope.stop.route);
			  if(!(typeof vehicle === "undefined")){
				  //$log.info('stop vehicle');
				  //$log.info(vehicle);
				  var vehicleEvent = mtsService.getVehicleEvent(vehicle.type,vehicle.ID);
				  if(!(typeof vehicleEvent === "undefined") ){
					  //$log.info('vehicleEvent');
					  //$log.info(vehicleEvent);
					  if(vehicle.prevLocation == $scope.stop.location && vehicleEvent.time == $scope.time ){
						  //$log.info($scope.stop);
						  //$log.info(vehicle);
						  //$log.info(vehicleEvent);
						  $scope.stop.vehicleAtStop.push(vehicle);
						  //$log.info($scope.stop);
					  }
					  else{
						  $scope.stop.vehicleAtStop.splice(0,$scope.stop.vehicleAtStop);
					  }
				  }
			  }
		  }
	  });
  };
  var routeController = function($scope, $log,mtsService){
		//$log.info('routeController');
	  $scope.routeStops = [];
	  $scope.routePaths = [];
	  $scope.routeID = $scope.route.id;
	  $scope.getStopCount=function(){
		  return -1;
	  };
	  $scope.$watch('stops',function(){
		  if(!(typeof $scope.route.stops === "undefined")){
			  $scope.routeStops.splice(0,$scope.routeStops.length);
			  var route = $scope.route;
			  //$log.info('route:'+route);
			  $scope.route.stops.forEach(function(stopID,index,arr){
				 var stop = mtsService.getStop(($scope.route.type==='busRoute'?'busStop':'railStop'), stopID);
				 if(stop){
					 $scope.routeStops.push({location:index,stop:stop,route:route,vehicleAtStop:[]});
					 //need to interleave the correct paths
					 //the paths need to have the right origin and destination locations
					 var origin=stop;
					 var destinationLocation = ((index+1<arr.length)?index+1:0);
					 var destinationStopId = $scope.route.stops[destinationLocation];
					 var destination = mtsService.getStop(origin.type, destinationStopId);
					 var path = mtsService.getPath(origin,destination);
					 if(path){
						 $log.info('route path:');
						 $log.info(path);
						 $scope.routePaths.push({path:path,route:route,startLocation:index,endLocation:destinationLocation,vehicleAtPath:[]});
					 }
				 }
				 else{
					 $log.info('could not find stop with ID='+stopID);
				 }				 
			  });
		  }
	  });
  };
  var pathController = function($scope, $log,mtsService){
	  $scope.time = mtsService.state.time;
	  $scope.debug=false;
	  
	  $scope.pathBlocked=function(){
		  if($scope.path) return $scope.path.blocked;
		  return false;
	  };
	  
	  $scope.pathHasHazard=function(){
		  return true;
	  };
	  
	  $scope.$watch('time',function(){
		  if(!(typeof $scope.path === "undefined") && !(typeof $scope.path.path === "undefined")){
			  //$log.info('path noticed time changed');
			  //$log.info('time: ',$scope.time);
			  //$log.info($scope.path);
			  $scope.path.vehicleAtPath.splice(0,$scope.path.vehicleAtPath);
			  var vehicle = mtsService.getStopVehicle($scope.path.path.origin.type,$scope.path.path.origin.ID,$scope.path.route);
			  if(!(typeof vehicle === "undefined")){
				  //$log.info('path vehicle');
				  //$log.info(vehicle);
				  if(vehicle.prevLocation == $scope.path.startLocation){
					  //$log.info('here! prevLocation: '+vehicle.prevLocation+', startLocation: '+$scope.path.startLocation);
					  var vehicleEvent = mtsService.getVehicleEvent(vehicle.type,vehicle.ID);
					  if(!(typeof vehicleEvent === "undefined") ){
						  //$log.info('vehicleEvent');
						  //$log.info(vehicleEvent);
						  if(vehicleEvent.time >= $scope.time){
							  //$log.info($scope.path);
							  //$log.info(vehicle);
							  //$log.info(vehicleEvent);
							  $scope.path.vehicleAtPath.push(vehicle);
							  //$log.info($scope.path);
						  }
						  else{
							  $scope.path.vehicleAtPath.splice(0,$scope.path.vehicleAtPath);
						  }
					  }
				  }
				  else{
					  $scope.path.vehicleAtPath.splice(0,$scope.path.vehicleAtPath);
				  }
			  }
		  }
	  });
	  //$scope.path.speedLimit=-1;
	  //$scope.path.delayFactor=-1;
	 
  };
  var vehicleController = function($scope, $log){
		//$log.info('vehicleController');
	  $scope.inService = function(){
		  return true;
	  };
	  $scope.getStop=function(){
		  return "stop";
	  };
	  $scope.getRoute=function(){
		  return "route";
	  };
  };
  var eventController = function($scope, $log){
		//$log.info('eventController');
  };  
 
  angular.module('SimExec',['ngMaterial','MTSService'])
  .directive('stop',[stopDirective])
  .directive('route',[routeDirective])
  .directive('path',[pathDirective])
  .directive('vehicle',[vehicleDirective])
  .directive('routeStop',[routeStopDirective])
  .directive('routeRoute',[routeRouteDirective])
  .directive('routePath',[routePathDirective])
  .directive('routeVehicle',[routeVehicleDirective])
  .directive('event',[eventDirective])
  .directive('simRouteViewer',[simRouteViewerDirective])
  .directive('simEntityViewer',[simEntityViewerDirective])
  .directive('sim',[simDirective])
  .controller('stopController',['$scope', '$log',  'MTSService', stopController])  
  .controller('routeController',['$scope', '$log', 'MTSService', routeController])  
  .controller('pathController',['$scope', '$log', 'MTSService', pathController])  
  .controller('vehicleController',['$scope', '$log', vehicleController])  
  .controller('eventController',['$scope', '$log', eventController])  
  .controller('simController',['$scope', '$log','MTSService', simController]);  
}());