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
            '<div class="dashboard" layout="row" layout-align="space-around stretch">'+
              '<bus-dashboard></bus-dashboard>'+
              '<train-dashboard></train-dashboard>'+
              '<sim-dashboard></sim-dashboard>'+
            '</div>'
  	}
  };
  var busDashboardDirective = function(){
      return{
		 restrict:'E',
		 scope:false,
		 replace: true,
         template: 
        '<div class="bus-dashboard tile" layout="row" layout-align="start center">'+
          '<div layout="column" layout-align="center center"><div class="dashboard-tile-label"><md-icon>directions_bus</md-icon></div><div class="dashboard-tile-label"><span>Buses</span></div></div>'+
          '<div layout="row" layout-align="space-around stretch" layout-wrap>'+
          '<div layout="column" flex="50" layout-align="center center"><div class="label">buses</div> <div class="value">{{getBusCount()}}</div></div>'+
          '<div layout="column" flex="50" layout-align="center center"><div class="label">stops</div> <div class="value">{{getBusStopCount()}}</div></div>'+
          '<div layout="column" flex="50" layout-align="center center"><div class="label">routes</div><div class="value">{{getBusRouteCount()}}</div></div>'+
          '<div layout="column" flex="50" layout-align="center center"><div class="label">paths</div> <div class="value">{{getBusPathCount()}}</div></div>'+
          '</div>'+
        '</div>'
	  }
   };
   var trainDashboardDirective = function(){
      return{
		 restrict:'E',
		 scope:false,
		 replace: true,
         template: 
        '<div class="train-dashboard tile" layout="row" layout-align="space-around stretch">'+
        '<div layput="column" layout-align="center center"><div class="dashboard-tile-label"><md-icon>directions_train</md-icon></div><div class="dashboard-tile-label"><span>Trains</span></div></div>'+
        '<div layout="row" layout-align="space-around stretch" layout-wrap>'+
          '<div layout="column" flex="50" layout-align="center center"><div class="label">trains</div> <div class="value">{{getTrainCount()}}</div></div>'+
          '<div layout="column" flex="50" layout-align="center center"><div class="label">stops</div> <div class="value">{{getTrainStopCount()}}</div></div>'+
          '<div layout="column" flex="50" layout-align="center center"><div class="label">routes</div><div class="value">{{getTrainRouteCount()}}</div></div>'+
          '<div layout="column" flex="50" layout-align="center center"><div class="label">paths</div> <div class="value">{{getTrainPathCount()}}</div></div>'+
          '</div>'+
        '</div>'
	  }
   };
   var simDashboardDirective = function(){
	  	return{
	  		restrict:'E',
			scope:false,
	        replace: true,
            template: 
        '<div class="sim-dashboard tile" layout="row" layout-align="space-around stretch">'+
          '<div layput="column" layout-align="center center"><div class="dashboard-tile-label"><md-icon ng-click="echo()">dvr</md-icon></div><div class="dashboard-tile-label"><span>Simulation</span></div></div>'+
          '<div layout="row" layout-align="space-around stretch" layout-wrap>'+
          '<div layout="column" flex="50" layout-align="center center"><div class="label">depots</div>  <div class="value">{{getDepotCount()}}</div></div>'+
          '<div layout="column" flex="50" layout-align="center center"><div class="label">time</div>  <div class="value">{{mts.time}}</div></div>'+
          '<div layout="column" flex="50" layout-align="center center"><div class="label">commands</div><div class="value">{{mts.commands.length}}</div></div>'+
          '<div layout="column" flex="50" layout-align="center center"><div class="label">events</div><div class="value">{{mts.events.length}}</div></div>'+
          '</div>'+
        '</div>'
        }
  };
  //controller
  var dashboardController = function($scope, $log, mtsService){
		//$log.info('dashboardController');
		$scope.mts = mtsService.state;
		//$log.info($scope.mts)
		$scope.getDepotCount = mtsService.countDepot;
		$scope.getBusCount=mtsService.countBus;
		$scope.getBusStopCount=mtsService.countBusStop;
		$scope.getBusRouteCount=mtsService.countBusRoute;
		$scope.getTrainCount=mtsService.countTrain;
		$scope.getTrainStopCount=mtsService.countTrainStop;
		$scope.getTrainRouteCount=mtsService.countTrainRoute;
		$scope.getBusPathCount=function(){
			var c = 0;
			for(var i=0;i<mtsService.state.paths.length;i++){
				if(mtsService.state.paths[i].origin.type=='busStop') c++; 
			}
			return c;
		};
		$scope.echo=function(){
			$log.info(mtsService.state);
		}
		$scope.getTrainPathCount=function(){
			var c = 0;
			for(var i=0;i<mtsService.state.paths.length;i++){
				if(mtsService.state.paths[i].origin.type=='railStop') c++; 
			}
			return c;
		};
	
  };
  angular.module('MTSDashboard',['ngMaterial','MTSService'])
  .directive('mtsDashboard',[dashboardDirective])
  .directive('busDashboard',[busDashboardDirective])
  .directive('trainDashboard',[trainDashboardDirective])
  .directive('simDashboard',[simDashboardDirective])
  .controller('dashboardController',['$scope', '$log','MTSService', dashboardController]);  
}());