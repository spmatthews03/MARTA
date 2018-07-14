// MTS SimExec Module
(function(){
  //Directives
  var busDirective = function(){
      return{
		 restrict:'E',
		 scope:{},
		 replace: true,
         template: 
        '<div class="bus-dashboard tile" layout="row" layout-align="start center">'+
          '<div layput="column" layout-align="center center"><div><span>Bus</span></div></div>'+
        '</div>'
	  }
   };
   var simDirective = function(){
	  	return{
	  		restrict:'E',
			scope:{},
	        replace: true,
            template: 
        '<div class="sim-exec-panel" layout="column">'+
          '<div layout="column" layout-align="center center"><div><span>Simulation</span></div><bus></bus></div>'+
        '</div>'
        }
  };
  //controllers
  var simController = function($scope, $log, mtsService){
		$log.info('simController');
		$scope.mts = mtsService.state;
  };
  var busController = function($scope, $log){
		$log.info('busController');
  };
  angular.module('SimExec',['ngMaterial','MTSService'])
  .directive('bus',[busDirective])
  .directive('sim',[simDirective])
  .controller('busController',['$scope', '$log', busController])  
  .controller('simController',['$scope', '$log','MTSService', simController]);  
}());