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
  	return{
  		restrict:'E',
  		scope:{level:"=",capacity:"=",vehicleid:"="},
  		controller: 'fuelGaugeController',
  		replace: true,
      template: '<div laytout="column" layout-align="end end">'+
    	           '<div class="gauge-label">fuel</div>'+
    	           '<div><svg class="gauge-display"></svg><div>'+
    	        '</div>',
      link: function(scope, element, attrs) {
        console.log("busid: "+scope.vehicleid+", level: "+scope.level+", capacity: "+scope.capacity+", perc: "+scope.perc);
        //d3 code here
        scope.$watch('perc',function(newVal,oldVal){
              console.log("perc was "+oldVal+". now is "+newVal);
              //gauge.update(newVal);
        });
      }
  	}
  };
  //controller
  var fuelGaugeController = function($scope, $log){
		$log.info('gaugeController for '+$scope.vehicleid);
		$scope.perc = $scope.level/$scope.capacity*100;
    $scope.update = function(){
      $scope.perc = NewValue();
    };
  };
  angular.module('d3Module')
  .directive('fuelGauge',['d3Service', fuelGuageDirective])
  .controller('fuelGaugeController',['$scope', '$log', 'd3Service', fuelGaugeController]);  
}());