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
		 scope:{stop:"=",order:"=",route:"="},
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
		
		$scope.stepOnce=function(){
			var command = 'step_once';
			  $log.info('command: '+command);
			  mtsService.state.commands.push({index:mtsService.state.commands.length,line:command,processed:false});
			  mtsService.executeCommand(command);
		};
		$scope.stepMulti = function(stepSize){
			var command = 'step_multi,'+stepSize;
			  $log.info('command: '+command);
			  mtsService.state.commands.push({index:mtsService.state.commands.length,line:command,processed:false});
			  mtsService.executeCommand(command);
		};		
  };
  
  var stopController = function($scope, $log,mtsService){
		//$log.info('stopController');
	  $scope.inService = function(){
		  return true;
	  };
	  $scope.time = mtsService.state.time;
	  $scope.vehicle;
	  $scope.$watch('time',function(){
		  $log.info('stop noticed time changed');
		  $log.info($scope.stop);
		  var vehicle = mtsService.getStopVehicle($scope.stop.type,$scope.stop.ID,$scope.route);
		  //var vehicleEvent = mtsService.getVehicleEvent(vehicle.type,vehicle.ID);
		  $log.info('vehicle');
		  $log.info(vehicle);
		  //$log.info('vehicleEvent');
		  //$log.info(vehicleEvent);
		  
	  });
  };
  var routeController = function($scope, $log,mtsService){
		//$log.info('routeController');
	  $scope.routeStops = [];
	  $scope.routePaths = [];
	  $scope.getStopCount=function(){
		  return -1;
	  };
	  $scope.$watch('stops',function(){
		  if($scope.route.stops){
			  $scope.routeStops.splice(0,$scope.routeStops.length);
			  $scope.route.stops.forEach(function(stopID,index,arr){
				 var stop = mtsService.getStop(($scope.route.type=='busRoute'?'busStop':'trainStop'), stopID);
				 if(stop){
					 $scope.routeStops.push(stop);
					 //need to interleave the correct paths
					 //the paths need to have the right origin and destination locations
					 var origin=stop;
					 var destinationLocation = ((index+1<arr.length)?index+1:0);
					 var destinationStopId = $scope.route.stops[destinationLocation];
					 var destination = mtsService.getStop(origin.type, destinationStopId);
					 $scope.routePaths.push(mtsService.getPath(origin,destination));
				 }
				 else{
					 $log.info('could not find stop with ID='+stopID);
				 }				 
			  });
		  }
	  });
  };
  var pathController = function($scope, $log){
	  //$log.info('pathController');
	  //$log.info($scope.path);
	  $scope.bus;
	  $scope.$watch('time',function(){$log.info('path noticed time changed');});
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
  .controller('pathController',['$scope', '$log', pathController])  
  .controller('vehicleController',['$scope', '$log', vehicleController])  
  .controller('eventController',['$scope', '$log', eventController])  
  .controller('simController',['$scope', '$log','MTSService', simController]);  
}());