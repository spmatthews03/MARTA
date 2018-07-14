// MTS SimExec Module
(function(){
  //Directives
  var busDirective = function(){
      return{
		 restrict:'E',
		 scope:{vehicle:"="},
		 replace: true,
		 controller: 'busController',
         template: 
        '<div class="bus">'+
          '<div><span>{{vehicle}}</span></div>'+
        '</div>'
	  }
   };
   var simDirective = function(){
	  	return{
	  		restrict:'E',
			scope:{},
			controller: 'simController',
	        replace: true,
            templateUrl: 'scripts/SimExecution/SimMain.html' 

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