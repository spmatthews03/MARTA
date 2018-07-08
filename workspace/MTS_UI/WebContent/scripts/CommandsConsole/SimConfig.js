// CommandsConsole directive
(function(){
  //Directives
  var simConfigDirective = function(){
  	return{
  		restrict:'E',
  		scope:{},
  		controller: 'simConfigController',
  		replace: true,
      template: 
            '<div layout="row">'+
              '<div layout="row">'+
                '<div layout="column">'+
                   '<md-button ng-click="addBus()">Add Bus</md-button>'+
                   '<md-button ng-click="addBusStop()">Add Bus Stop</md-button>'+
                   '<md-button ng-click="addBusRoute()">Add Bus Route</md-button>'+
                   '<md-button ng-click="addTrain()">Add Train</md-button>'+
                   '<md-button ng-click="addTrainStation()">Add Train Station</md-button>'+
                   '<md-button ng-click="addRaiLine()">Add Rail Line</md-button>'+
                   '<md-button ng-click="addHazard()">Add Hazard</md-button>'+
                   '<md-button ng-click="setSpeedLimit()">Set Speed Limit</md-button>'+
                '</div>'+
                '<div layout="column">'+
	               '<div>Commands</div>'+
	            '</div>'+
              '</div>'+
            '</div>'
  	}
  };
  //controllers
  var simConfigController = function($scope, $log, mtsService){
	  $log.info('simConfigController');
	  $scope.addBus = function(){
		  $log.info('add bus');
	  };
	  $scope.addBusStop = function(){
		  $log.info('add bus stop');
	  };
	  $scope.addBusRoute = function(){
		  $log.info('add bus route');
	  };
	  $scope.addTrain = function(){
		  $log.info('add train');
	  };
	  $scope.addTrainStation = function(){
		  $log.info('add train station');
	  };
	  $scope.addRailLine = function(){
		  $log.info('add rail line');
	  };
	  $scope.addHazard = function(){
		  $log.info('add hazard');
	  };
	  $scope.setSpeedLimit = function(){
		  $log.info('set speed limit');
	  };
  };
  
  angular.module('CommandsConsole')
  .directive('simConfig',[simConfigDirective])
  .controller('simConfigController',['$scope', '$log','MTSService', simConfigController]);
}());
