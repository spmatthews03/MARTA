// MTS SimExec Module
(function(){
  //Directives
  var stopDirective = function(){
      return{
		 restrict:'E',
		 scope:{stop:"="},
		 replace: true,
		 controller: 'stopController',
         template: 
        '<div class="stop">'+
          '<div><span>{{stop}}</span></div>'+
        '</div>'
	  }
   };
  var routeDirective = function(){
	      return{
			 restrict:'E',
			 scope:{route:"="},
			 replace: true,
			 controller: 'routeController',
	         template: 
	        '<div class="route">'+
	          '<div><span>{{route}}</span></div>'+
	        '</div>'
		  }
  };   
  var pathDirective = function(){
      return{
		 restrict:'E',
		 scope:{path:"="},
		 replace: true,
		 controller: 'pathController',
         template: 
        '<div class="path">'+
          '<div><span>{{path}}</span></div>'+
        '</div>'
	  }
  };
  var vehicleDirective = function(){
      return{
		 restrict:'E',
		 scope:{vehicle:"="},
		 replace: true,
		 controller: 'vehicleController',
         template: 
        '<div class="vehicle">'+
          '<div><span>{{vehicle}}</span></div>'+
        '</div>'
	  }
  };
  var eventDirective = function(){
      return{
		 restrict:'E',
		 scope:{event:"="},
		 replace: true,
		 controller: 'eventController',
         template: 
        '<div class="event">'+
          '<div><span>{{event}}</span></div>'+
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
  var stopController = function($scope, $log){
		$log.info('stopController');
  };
  var routeController = function($scope, $log){
		$log.info('routeController');
  };
  var pathController = function($scope, $log){
		$log.info('pathController');
  };
  var vehicleController = function($scope, $log){
		$log.info('vehicleController');
  };
  var eventController = function($scope, $log){
		$log.info('eventController');
  };  
 
  angular.module('SimExec',['ngMaterial','MTSService'])
  .directive('stop',[stopDirective])
  .directive('route',[routeDirective])
  .directive('path',[pathDirective])
  .directive('vehicle',[vehicleDirective])
  .directive('event',[eventDirective])
  .directive('sim',[simDirective])
  .controller('stopController',['$scope', '$log', stopController])  
  .controller('routeController',['$scope', '$log', routeController])  
  .controller('pathController',['$scope', '$log', pathController])  
  .controller('vehicleController',['$scope', '$log', vehicleController])  
  .controller('eventController',['$scope', '$log', eventController])  
  .controller('simController',['$scope', '$log','MTSService', simController]);  
}());