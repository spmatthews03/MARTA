// CommandsConsole directive
(function(){
  //Directives
  var consoleDirective = function(){
  	return{
  		restrict:'E',
  		scope:{commands:"="},
  		controller: 'commandsConsoleController',
  		replace: true,
      template: 
            '<div class="mts-console" layout="column">'+
              '<div>Commands Console:</div>'+
              '<div layout="row">'+
                 '<div layout="column" flex>'+
                 	'<command ng-repeat="c in commands | filter:isValidCommand track by $index" data-command="c" data-index="$index"></command>'+
                 '</div>'+
                 '<mts-dashboard></mts-dashboard>'+
              '</div>'+
            '</div>'
  	}
  };
  var commandDirective = function(){
	  	return{
	  		restrict:'E',
	  		scope:{command:"=",index:"="},
	  		controller: 'commandController',
	  		replace: true,
	      template:
	    	    '<div class="mts-command" ng-class="{ \'current-command\': isCurrentCommand()}" layout="row" layout-align="center"><div flex="5"><md-button ng-click="execute()" ng-show="isCurrentCommand()"><md-icon>play_circle_outline</md-icon></md-button></div>'+
	            '<div flex ng-switch="commandType">'+
	               '<add-stop-command class="command-ct" ng-switch-when="add_stop" data-id="parameters[1]" data-name="parameters[2]" data-waiting="parameters[3]" data-x-coord="parameters[4]" data-y-coord="parameters[5]"></add-stop-command>'+
	               '<add-route-command ng-switch-when="add_route" data-id="parameters[1]" data-number="parameters[2]" data-name="parameters[3]"></add-route-command>'+
	               '<add-bus-command ng-switch-when="add_bus" data-id="parameters[1]" data-route="parameters[2]" data-location="parameters[3]" data-passengers="parameters[4]" data-capacity="parameters[5]" data-speed="parameters[6]"></add-bus-command>'+
	               '<add-event-command ng-switch-when="add_event" data-id="parameters[3]" data-rank="parameters[1]" data-type="parameters[2]"></add-event-command>'+
	               '<extend-route-command ng-switch-when="extend_route" data-route-id="parameters[1]" data-stop-id="parameters[2]"></extend-route-command>'+
	               '<step-multi-command ng-switch-when="step_multi" data-count="parameters[1]"></step-multi-command>'+
	               '<div ng-switch-default>{{command}}</div>'+
	            '</div></div>'
	  	}
  };


  var addStopCommandDirective = function(){
	  	return{
	  		restrict:'E',
	  		scope: {id:"=",name:"=", waiting:"=", xCoord:"=", yCoord:"="},
	  		replace: true,
	      template: 
	        '<div layout="row" layout-align="start center">'+
	            '<div>add stop [{{id}}] - {{name}} {waiting:"{{waiting}}", xCoord:"{{xCoord}}", yCoord:"{{yCoord}}"}</div>'+
	  		'</div>'
	  	}
  };
  var addRouteCommandDirective = function(){
	  	return{
	  		restrict:'E',
	  		scope: {id:"=",name:"=", number:"="},
	  		replace: true,
	      template: 
	        '<div>'+
	            'add route [{{id}}] {{number}} - {{name}}'+
	  		'</div>'
	  	}
};
var addBusCommandDirective = function(){
  	return{
  		restrict:'E',
  		scope: {id:"=", route:"=", location:"=", passengers:"=", capacity:"=", speed:"="},
  		replace: true,
      template: 
        '<div>'+
            'add bus [{{id}}] {route:{{route}}, location:{{location}}, passengers:{{passengers}}, capacity:{{capacity}}, speed:{{speed}}}'+
  		'</div>'
  	}
};
var addEventCommandDirective = function(){
  	return{
  		restrict:'E',
  		scope: {id:"=", rank:"=", type:"="},
  		replace: true,
      template: 
        '<div>'+
            'add event [{{id}}] {rank:{{rank}}, type:{{type}}}'+
  		'</div>'
  	}
};

var extendRouteCommandDirective = function(){
  	return{
  		restrict:'E',
  		scope: {routeId:"=", stopId:"="},
  		replace: true,
      template: 
        '<div>'+
            'extend route {{routeId}} by adding stop {{stopId}}'+
  		'</div>'
  	}
};
var stepMultiCommandDirective = function(){
  	return{
  		restrict:'E',
  		scope: {count:"="},
  		replace: true,
      template: 
        '<div>'+
            'step through {{count}} events'+
  		'</div>'
  	}
};

  
  //controllers
  var consoleController = function($scope, $log, mtsService){
		$scope.current = {id:0};  //default to 1st command
		$scope.mts = mtsService;
		$scope.isValidCommand = function(item){
			if(item.length<1) return false;
			var tokens = item.split(',');
			switch(tokens[0]){
			case 'add_stop':
				if(tokens.length!=6) return false;
				break;
			case 'add_route':
				if(tokens.length!=4) return false;
				break;
			case 'add_bus':
				if(tokens.length!=7) return false;
				break;
			case 'add_event':
				if(tokens.length!=4) return false;
				break;
			case 'extend_route':
				if(tokens.length!=3) return false;
				break;
			case 'step_multi':
				if(tokens.length!=2) return false;
				break;
			case 'system_report':
				if(tokens.length!=1) return false;
				break;
			case 'display_model':
				if(tokens.length!=1) return false;
				break;
			case 'quit':
				if(tokens.length!=1) return false;
				break;
			default:
				return false;
			}
			return true;
		}
  };
  var commandController = function($scope, $log, mtsService){
	//$log.info('commandController:'+$scope.index);
	
	$scope.parameters = $scope.command.split(',');
	$scope.commandType = $scope.parameters[0];
	$scope.isCurrentCommand = function(){
		//$log.info($scope.index);
		//$log.info($scope.$parent.current.id);
		return $scope.index == $scope.$parent.current.id;
	}
	$scope.execute = function(){
		//$log.info($scope.command);
		//$log.info('command count = '+$scope.$parent.commands.length);
		//$log.info('command current ['+$scope.$parent.current.id+']='+$scope.$parent.commands[$scope.$parent.current.id]);
		mtsService.executeCommand($scope.command);
		if($scope.$parent.current.id < $scope.$parent.commands.length){
			$scope.$parent.current.id = $scope.$parent.current.id +1;
		}
	};
  };
  angular.module('CommandsConsole',['ngMaterial','MTSService'])
  .directive('addRouteCommand',[addRouteCommandDirective])
  .directive('addBusCommand',[addBusCommandDirective])
  .directive('addStopCommand',[addStopCommandDirective])
  .directive('addEventCommand',[addEventCommandDirective])
  .directive('stepMultiCommand',[stepMultiCommandDirective])
  .directive('extendRouteCommand',[extendRouteCommandDirective])
  .directive('command',[commandDirective])
  .directive('commandsConsole',[consoleDirective])
  .controller('commandController',['$scope', '$log','MTSService', commandController])
  .controller('commandsConsoleController',['$scope', '$log', 'MTSService', consoleController]);
}());
