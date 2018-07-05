// MTS Dashboard directive
(function(){
  //Directives
  var dashboardDirective = function(){
  	return{
  		restrict:'E',
  		scope:{},
  		controller: 'dashboardController',
  		replace: true,
      template: 
            '<div class="dashboard" layout="column" layout-align="space-around center">'+
              '<div class="tile" layout="column" layout-align="center center"><div class="label">time</div><div class="value">{{mts.time}}</div></div>'+
              '<div class="tile" layout="column" layout-align="center center"><div class="label">buses</div><div class="value">{{mts.vehicles.length}}</div></div>'+
              '<div class="tile" layout="column" layout-align="center center"><div class="label">stops</div><div class="value">{{mts.stops.length}}</div></div>'+
              '<div class="tile" layout="column" layout-align="center center"><div class="label">routes</div><div class="value">{{mts.routes.length}}</div></div>'+
              '<div class="tile" layout="column" layout-align="center center"><div class="label">events</div><div class="value">{{mts.events.length}}</div></div>'+
            '</div>'
  	}
  };
  //controller
  var dashboardController = function($scope, $log, mtsService){
		//$log.info('dashboardController');
		$scope.mts = mtsService.state;
	
  };
  angular.module('MTSDashboard',['ngMaterial','MTSService'])
  .directive('mtsDashboard',[dashboardDirective])
  .controller('dashboardController',['$scope', '$log','MTSService', dashboardController]);  
}());