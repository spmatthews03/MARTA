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
  var fuelConsumptionLogDirective = function(){
      return{
		 restrict:'E',
		 scope:{},
		 replace: true,
		 controller: 'fuelConsumptionLogController',
         templateUrl: 'scripts/SimExecution/FuelConsumptionLog.html'
	  }
  };
  
  //controllers
  var fuelConsumptionController = function($scope, $log, mtsService,gtcolors){
		//$log.info('fuelConsumptionController');
	    $scope.selected=1;
	    $scope.colors=gtcolors;
	    $scope.byBus = mtsService.state.fuelByBusData;
	    $scope.echo = function(){
	    	$log.info('bybus:');
	    	$log.info(mtsService.state.fuelByBusData);
	    };
	    $scope.select=function(selection){
	    	$scope.selected=selection;
	    };
		$scope.reports = mtsService.state.reports;
		$scope.roundAmount = function(amount){
			return (Math.round(amount*100)/100);
		};
		
		
  };
  var fuelConsumptionLogController = function($scope, $log, mtsService){
		//$log.info('fuelConsumptionLogController');
		$scope.reports = mtsService.state.reports;
		$scope.roundAmount = function(amount){
			return (Math.round(amount*100)/100);
		};
		$scope.getTotal = function(vehicle){
			var totalAmount=0;
			
				//$log.info('fuel report for ');
				//$log.info(vehicle);
				for(var i=0;i<mtsService.state.reports.length;i++){
					var vehicleReport = mtsService.state.reports[i];
					//$log.info(vehicleReport);
					if(vehicleReport.vehicle.type==vehicle.type && vehicleReport.vehicle.ID == vehicle.ID){
						for(var j=0;j<vehicleReport.reports.length;j++){
							var report = vehicleReport.reports[j];
							//$log.info(vehicleReport);
							totalAmount = totalAmount+report.amount;
							//$log.info('totalAmount: '+totalAmount);
						}
					}
				}
			
				
			return parseFloat(Math.round(totalAmount * 10) / 10).toFixed(1);
		};
};
 
  angular.module('SimExec')
  .directive('fuelConsumption',[fuelConsumptionDirective])
  .directive('fuelConsumptionLog',[fuelConsumptionLogDirective])
  .controller('fuelConsumptionLogController',['$scope', '$log',  'MTSService', fuelConsumptionLogController])  
  .controller('fuelConsumptionController',['$scope', '$log',  'MTSService', 'GTColorsService',fuelConsumptionController])  
}());