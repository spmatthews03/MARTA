// MTS Fuel Consumption
(function(){
  //Directives
  var fuelConsumptionDirective = function(){
      return{
		 restrict:'E',
		 scope:{},
		 replace: true,
		 controller: 'fuelConsumptionController',
         templateUrl: 'scripts/SimExecution/FuelConsumption.html'
	  }
  };

  //controllers
  var fuelConsumptionController = function($scope, $log, mtsService){
		$log.info('fuelConsumptionController');
		$scope.reports = mtsService.state.reports;
  };
 
  angular.module('SimExec')
  .directive('fuelConsumption',[fuelConsumptionDirective])
  .controller('fuelConsumptionController',['$scope', '$log',  'MTSService', fuelConsumptionController])  
}());