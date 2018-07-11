// CommandsConsole directive
(function(){
  //Directives
  var simExecutorDirective = function(){
  	return{
  		restrict:'E',
  		scope:{},
  		controller: 'simExecutorController',
  		replace: true,
      template: 
            '<div class="sim-executor-panel" layout="column">'+
               '<div>Sim Executor</div>'+
            '</div>'
  	}
  };
   //controllers
  var simExecutorController = function($scope, $log, mtsService){
	  $log.info('simExecutorController');
  };
  
  angular.module('CommandsConsole')
  .directive('simExecutor',[simExecutorDirective])
  .controller('simExecutorController',['$scope', '$log','MTSService', simExecutorController]);
}());
