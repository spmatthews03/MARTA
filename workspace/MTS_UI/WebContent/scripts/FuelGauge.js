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
  		scope:{level:"=",busid:"="},
  		controller: 'fuelGaugeController',
  		replace: true,
      template: '<div><svg id="{{busid}}gauge" width="200" height="200" ng-click="update()"></svg></div>',
      link: function(scope, element, attrs) {
        console.log("busid: "+scope.busid+", level: "+scope.level);
        var config = d3.liquidFillGauge.liquidFillGaugeDefaultSettings();
        config.textVertPosition = 0.8;
        config.waveAnimateTime = 5000;
        config.waveHeight = 0.15;
        config.waveAnimate = false;
        config.waveOffset = 0.25;
        config.valueCountUp = false;
        config.displayPercent = false;    
        var gauge = d3.liquidFillGauge.loadLiquidFillGauge(element.find("svg")[0], scope.level,config,scope.busid+"gauge");
        scope.$watch('level',function(newVal,oldVal){
              console.log("level was "+oldVal+". now is "+newVal);
              gauge.update(newVal);
        });
      }
  	}
  };
  //controller
  var fuelGaugeController = function($scope, $log){
		$log.info('gaugeController for '+$scope.busid);
    $scope.update = function(){
      $scope.level = NewValue();
    };
  };
  angular.module('d3Module')
  .directive('fuelGauge',['d3Service', fuelGuageDirective])
  .controller('fuelGaugeController',['$scope', '$log', 'd3Service', fuelGaugeController]);  
}());