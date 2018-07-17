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
		$scope.getTotal = function(vehicle){
			var totalAmount=0;
			
				$log.info('fuel report for ');
				$log.info(vehicle);
				for(var i=0;i<mtsService.state.reports.length;i++){
					var vehicleReport = mtsService.state.reports[i];
					$log.info(vehicleReport);
					if(vehicleReport.vehicle.type==vehicle.type && vehicleReport.vehicle.ID == vehicle.ID){
						for(var j=0;j<vehicleReport.reports.length;j++){
							var report = vehicleReport.reports[j];
							$log.info(vehicleReport);
							totalAmount = totalAmount+report.amount;
							$log.info('totalAmount: '+totalAmount);
						}
					}
				}
			

			return totalAmount;
		};
		
  };
 
  angular.module('SimExec')
  .directive('fuelConsumption',[fuelConsumptionDirective])
  .controller('fuelConsumptionController',['$scope', '$log',  'MTSService', fuelConsumptionController])  
}());