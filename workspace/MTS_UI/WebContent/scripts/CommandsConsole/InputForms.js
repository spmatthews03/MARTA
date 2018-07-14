// InputForms

// AddBusStopInputFormDirective
(function(){
  //Directives
  var addBusStopInputFormDirective = function(){
  	return{
  		restrict:'E',
  		scope:{},
  		controller: 'inputFormController',
  		replace: true,
      templateUrl:'scripts/CommandsConsole/AddBusStop.html'
  	}
  };
  var addBusRouteInputFormDirective = function(){
	  	return{
	  		restrict:'E',
	  		scope:{},
	  		controller: 'inputFormController',
	  		replace: true,
	      templateUrl:'scripts/CommandsConsole/AddBusRoute.html'
	  	}
	  };
  var extendBusRouteInputFormDirective = function(){
	  	return{
	  		restrict:'E',
  		scope:{},
  		controller: 'inputFormController',
  		replace: true,
      templateUrl:'scripts/CommandsConsole/ExtendBusRoute.html'
  	}
  };
var addBusFormDirective = function(){
	  	return{
	  		restrict:'E',
		scope:{},
		controller: 'inputFormController',
		replace: true,
    templateUrl:'scripts/CommandsConsole/AddBus.html'
	}
};
var addEventFormDirective = function(){
  	return{
  		restrict:'E',
	scope:{},
	controller: 'inputFormController',
	replace: true,
templateUrl:'scripts/CommandsConsole/AddEvent.html'
}
};
var addBusPathDelayFormDirective = function(){
  	return{
  		restrict:'E',
	scope:{},
	controller: 'inputFormController',
	replace: true,
templateUrl:'scripts/CommandsConsole/BusPathDelay.html'
}
};
var setBusSpeedLimitFormDirective = function(){
	return{
		restrict:'E',
		scope:{},
		controller: 'inputFormController',
		replace: true,
		templateUrl:'scripts/CommandsConsole/BusSpeedLimit.html'
	}
};
var addTrainStationFormDirective = function(){
	return{
		restrict:'E',
		scope:{},
		controller: 'inputFormController',
		replace: true,
		templateUrl:'scripts/CommandsConsole/AddTrainStop.html'
	}
};
var addRailLineFormDirective = function(){
	return{
		restrict:'E',
		scope:{},
		controller: 'inputFormController',
		replace: true,
		templateUrl:'scripts/CommandsConsole/AddTrainRoute.html'
	}
};
var extendRailLineFormDirective = function(){
	return{
		restrict:'E',
		scope:{},
		controller: 'inputFormController',
		replace: true,
		templateUrl:'scripts/CommandsConsole/ExtendTrainRoute.html'
	}
};
var addTrainFormDirective = function(){
	return{
		restrict:'E',
		scope:{},
		controller: 'inputFormController',
		replace: true,
		templateUrl:'scripts/CommandsConsole/AddTrain.html'
	}
};
var addTrainPathDelayFormDirective = function(){
	return{
		restrict:'E',
		scope:{},
		controller: 'inputFormController',
		replace: true,
		templateUrl:'scripts/CommandsConsole/TrainPathDelay.html'
	}
};
var setTrainSpeedLimitFormDirective = function(){
	return{
		restrict:'E',
		scope:{},
		controller: 'inputFormController',
		replace: true,
		templateUrl:'scripts/CommandsConsole/TrainSpeedLimit.html'
	}
};
//controllers
  var inputFormController = function($scope, $log, mtsService){
	  $log.info('inputFormController');
	  $scope.item= {};
	  $scope.cancel = function(){
		$log.info("cancelling form");  
		  mtsService.state.editMode=false;
		  mtsService.state.commandOption='';
		  $scope.item={};
	  };
	  $scope.onSubmit=function() {
		  $log.info("form submitted");
		  $log.info("form: "+mtsService.state.commandOption);
		  $log.info($scope.item);
		  var command='';
		  switch(mtsService.state.commandOption){
		  case 'addBusStop':
			  command = 'add_stop,'+$scope.item.id+','+$scope.item.name+','+$scope.item.riders+','+$scope.item.xCoord+','+$scope.item.yCoord;
			  break;
		  case 'addBusRoute':
			  command = 'add_route,'+$scope.item.id+','+$scope.item.number+','+$scope.item.name;
			  break;
		  case 'extendBusRoute':
			  command = 'extend_route,'+$scope.item.routeId+','+$scope.item.stopId;
			  break;
		  case 'addBus':
			  command = 'add_bus,'+$scope.item.id+','+$scope.item.routeId+','+$scope.item.location+','+$scope.item.passengers+','+$scope.item.capacity+','+$scope.item.speed;
			  break;
		  case 'addEvent':
			  command = 'add_event,'+$scope.item.time+','+$scope.item.type+','+$scope.item.id;
			  break;
		  case 'pathDelay':
			  command = 'path_delay,'+$scope.item.time+','+$scope.item.duration+','+$scope.item.originStopId+','+$scope.item.destinationStopId+','+$scope.item.delayFactor;
			  break;
		  case 'setBusSpeedLimit':
			  command = 'add_train,'+$scope.item.time+','+$scope.item.duration+','+$scope.item.originStopId+','+$scope.item.destinationStopId+','+$scope.item.delayFactor;
			  break;
		  case 'addTrainStation':
			  command = 'add_station,'+$scope.item.time+','+$scope.item.duration+','+$scope.item.originStopId+','+$scope.item.destinationStopId+','+$scope.item.delayFactor;
			  break;
		  case 'addRailLine':
			  command = 'path_delay,'+$scope.item.time+','+$scope.item.duration+','+$scope.item.originStopId+','+$scope.item.destinationStopId+','+$scope.item.delayFactor;
			  break;
		  case 'extendRailLine':
			  command = 'path_delay,'+$scope.item.time+','+$scope.item.duration+','+$scope.item.originStopId+','+$scope.item.destinationStopId+','+$scope.item.delayFactor;
			  break;
		  case 'addTrainPathDelay':
			  command = 'path_delay,'+$scope.item.time+','+$scope.item.duration+','+$scope.item.originStopId+','+$scope.item.destinationStopId+','+$scope.item.delayFactor;
			  break;
		  case 'addBusPathDelay':
			  command = 'path_delay,'+$scope.item.time+','+$scope.item.duration+','+$scope.item.originStopId+','+$scope.item.destinationStopId+','+$scope.item.delayFactor;
			  break;
		  case 'setTrainSpeedLimit':
			  command = 'path_delay,'+$scope.item.time+','+$scope.item.duration+','+$scope.item.originStopId+','+$scope.item.destinationStopId+','+$scope.item.delayFactor;
			  break;
		  }
		  if(command && command.length>0){
			  $log.info('command: '+command);
			  mtsService.state.commands.push({index:mtsService.state.commands.length,line:command,processed:false});
			  mtsService.executeCommand(command);
		  }
		  //$log.info(mtsService.state);
		  mtsService.state.editMode=false;
		  mtsService.state.commandOption='';
		  $scope.item={};
		  //$log.info(mtsService.state);
	  };
  };
  
  angular.module('CommandsConsole')
  .directive('addBusStopForm',[addBusStopInputFormDirective])
  .directive('addBusRouteForm',[addBusRouteInputFormDirective])
  .directive('extendBusRouteForm',[extendBusRouteInputFormDirective])
  .directive('addBusForm',[addBusFormDirective])
  .directive('addEventForm',[addEventFormDirective])
  .directive('addBusPathDelayForm',[addBusPathDelayFormDirective])
  .directive('setBusSpeedLimitForm',[setBusSpeedLimitFormDirective])
  .directive('addTrainStationForm',[addTrainStationFormDirective])
  .directive('addRailLineForm',[addRailLineFormDirective])
  .directive('extendRailLineForm',[extendRailLineFormDirective])
  .directive('addTrainForm',[addTrainFormDirective])
  .directive('addTrainPathDelayForm',[addTrainPathDelayFormDirective])
  .directive('setTrainSpeedLimitForm',[setTrainSpeedLimitFormDirective])
  .controller('inputFormController',['$scope', '$log','MTSService', inputFormController]);
}());
