// MTS Map directive
(function(){
    function NewValue(){
        if(Math.random() > .5){
            return Math.round(Math.random()*100);
        } else {
            return (Math.random()*100).toFixed(1);
        }
    }  
  //Directives
  var fuelGuageDirective = function(d3){
  	return {
        restrict: 'E',
        scope: {
            options: '=',vehicleid:"=",capacity:"=",level:"="
        },
        replace:true,
        controller: 'fuelGaugeController',
        template: '<div class="fuel-gauge-panel" layout="column" layout-align="center center">'+
                      '<div class="fuel-gauge-label">fuel:</div>'+
                      '<div class="fuel-gauge-display" layout="column" layout-align="end stretch">'+
                      '<div class="fuel-level" style="height:{{getLevelHeight()}}px"></div></div>'+
                  '</div>'
        
    };  	
  };
  //controller
  var fuelGaugeController = function($scope, $log){
		//$log.info('gaugeController for '+$scope.vehicleid);
		$scope.getLevelHeight = function(){
			var levelHeight = Math.round($scope.level/$scope.capacity*30); 
			//$log.info('level: '+$scope.level+', capacity: '+$scope.capacity+' --> '+levelHeight);
			return levelHeight;
		};
  };
  angular.module('d3Module')
  .directive('fuelGauge',['d3Service', fuelGuageDirective])
  .controller('fuelGaugeController',['$scope', '$log', 'd3Service', fuelGaugeController]);  
}());