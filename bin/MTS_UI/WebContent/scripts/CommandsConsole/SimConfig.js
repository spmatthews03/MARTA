// CommandsConsole directive
(function(){
  //Directives
  var simConfigDirective = function(){
  	return{
  		restrict:'E',
  		scope:{},
  		controller: 'simConfigController',
  		replace: true,
      template: 
            '<div class="sim-config-panel" layout="row">'+
               '<div layout="column" class="config-panel">'+
                  '<div><span class="sim-configmode-toggle-label">configuration mode:</span></div>'+
                  '<md-button ng-click="switch()">'+
                    '<div layout="row">'+
                      '<div layout="column"><md-icon ng-class="{\'toggle-option-selected\':!toggleOn}" class="manual-config-option-icon">playlist_add</md-icon><div><span class="sim-configmode-toggle-label" ng-class="{\'toggle-option-selected\':!toggleOn}">manual</span></div></div>'+
                      '<md-icon class="toggle" md-font-set="fa" ng-hide="toggleOn">&#xf204</md-icon>'+
                      '<md-icon class="toggle" md-font-set="fa" ng-show="toggleOn">&#xf205</md-icon>'+
                      '<div layout="column"><md-icon ng-class="{\'toggle-option-selected\':toggleOn}" class="file-config-option-icon" md-font-set="fa">&#xf56f</md-icon><div><span class="sim-configmode-toggle-label" ng-class="{\'toggle-option-selected\':toggleOn}">from file</span></div></div>'+
                    '</div>'+
                  '</md-button>'+
	              '<div ng-show="!toggleOn" layout="column">'+
	                 '<div ng-show="!mts.editMode" layout="column">'+

		                   '<div class="command-panel-label">Select command to add:</div>'+
	                       '<md-content class="sim-config-controls-panel">'+
	                           '<div layout="row" layout-wrap>'+
			                   '<md-button flex="40" ng-click="addBusStop()">Add Bus Stop</md-button>'+
			                   '<md-button flex="40" ng-click="addBusRoute()">Add Bus Route</md-button>'+
			                   '<md-button flex="40" ng-click="extendBusRoute()">Extend Bus Route</md-button>'+
			                   '<md-button flex="40" ng-click="addBus()">Add Bus</md-button>'+
			                   '<md-button flex="40" ng-click="addBusPathDelay()">Add Bus Path Delay</md-button>'+
			                   '<md-button flex="40" ng-click="setBusSpeedLimit()">Set Bus Speed Limit</md-button>'+
			                   '<md-button flex="40" ng-click="addTrainStation()">Add Train Station</md-button>'+
			                   '<md-button flex="40" ng-click="addRailLine()">Add Rail Line</md-button>'+
			                   '<md-button flex="40" ng-click="extendRailLine()">Extend Rail Line</md-button>'+
			                   '<md-button flex="40" ng-click="addTrain()">Add Train</md-button>'+
			                   '<md-button flex="40" ng-click="addTrainPathDelay()">Add Train Path Delay</md-button>'+
			                   '<md-button flex="40" ng-click="setTrainSpeedLimit()">Set Train Speed Limit</md-button>'+
			                   '<md-button flex="40" ng-click="addEvent()">add Event</md-button>'+
			                   '</div>'+
	                       '</md-content">'+
		              '</div>'+     
		              '<div ng-show="mts.editMode" layout="column">'+
		                 '<div ng-switch="mts.commandOption">'+
			                 '<add-bus-stop-form ng-switch-when="addBusStop"></add-bus-stop-form>'+
			                 '<add-bus-route-form ng-switch-when="addBusRoute"></add-bus-route-form>'+
			                 '<extend-bus-route-form ng-switch-when="extendBusRoute"></extend-bus-route-form>'+
			                 '<add-bus-form ng-switch-when="addBus"></add-bus-form>'+
			                 '<add-event-form ng-switch-when="addEvent"></add-event-form>'+
			                 '<add-bus-path-delay-form ng-switch-when="addBusPathDelay"></add-bus-path-delay-form>'+
			                 '<command-entry ng-switch-default></command-entry>'+
		                 '</div>'+
		              '</div>'+

	              '</div>'+
	              '<div ng-show="toggleOn" layout="column">'+
		              '<input class="command-file-input" type="file" ng-file-select="onFileSelect($files)"></input>'+
	              '</div>'+
	           '</div>'+
               '<div layout="column" flex>'+
	              '<commands-console></commands-console>'+
	           '</div>'+
            '</div>'
  	}
  };
  var commandEntryDirective = function(){
	  	return{
	  		restrict:'E',
  		scope: false,
  		replace: true,
      template: 
            '<div class="sim-command-entry-form" layout="column">'+
               '<div><span class="command-entry-command-label">{{commandOption}}</span></div>'+
               '<div>input form controls</div>'+
               '<div class="command-entry-buttons" layout="row" layout-align="space-around center">'+
                  '<md-button ng-click="addCommand()">add</md-button>'+
                  '<md-button ng-click="cancelAddCommand()">cancel</md-button>'+
               '</div>'+
            '</div>'
  	}
  }; 
  //controllers
  var simConfigController = function($scope, $log, mtsService, fileReader){
	  $log.info('simConfigController');
	  $scope.toggleOn = false;
	  $scope.switch = function(){
	  	$scope.toggleOn = !$scope.toggleOn;
	  };
	  mtsService.state.editMode=false;
	  mtsService.state.commandOption='';
	  $scope.mts = mtsService.state;
	  
	  //panel controls
	  $scope.panelsState={busPanel:false,trainPanel:false,eventPanel:false};
	 

	  //Bus methods 
	  $scope.addBusStop = function(){
		  mtsService.state.editMode=true;
		  mtsService.state.commandOption='addBusStop';
	  };
	  $scope.addBusRoute = function(){
		  mtsService.state.editMode=true;
		  mtsService.state.commandOption='addBusRoute';
	  };
	  $scope.extendBusRoute = function(){
		  mtsService.state.editMode=true;
		  mtsService.state.commandOption='extendBusRoute';
	  };
	  $scope.addBus = function(){
		  mtsService.state.editMode=true;
		  mtsService.state.commandOption='addBus';
	  };
	  $scope.addBusPathDelay = function(){
		  mtsService.state.editMode=true;
		  mtsService.state.commandOption='addBusPathDelay';
	  };
	  $scope.setBusSpeedLimit = function(){
		  $log.info('set bus speed limit');
	  };
	  //Train methods
	  $scope.addTrainStation = function(){
		  $log.info('add train station');
	  };
	  $scope.addRailLine = function(){
		  $log.info('add rail line');
	  };
	  $scope.extendRailLine = function(){
		  $log.info('extend rail line');
	  };
	  $scope.addTrain = function(){
		  $log.info('add train');
	  };
	  $scope.addTrainPathDelay = function(){
		  $log.info('add train path delay');
	  };
	  $scope.setTrainSpeedLimit = function(){
		  $log.info('set train speed limit');
	  };
	  //	  
	  //Sim Engine methods
	  $scope.addEvent = function(){
		  mtsService.state.editMode=true;
		  mtsService.state.commandOption='addEvent';
	  };
	  
	  //command entry form buttons
	  $scope.addCommand = function(){
		  switch($scope.commandOption){
			  case 'addBusStop':
				  $scope.commandLine='add_stop,0,Appleville,5,0.0,0.08';
				  break;
			  case 'addBusRoute':
				  $scope.commandLine='add_route,0,10,Express';
				  break;
			  case 'extendBusRoute':
				  $scope.commandLine='extend_route,0,5';
				  break;
			  case 'addBus':
				  $scope.commandLine='add_bus,7,0,0,0,10,50';
				  break;
			  case 'addBusPathDelay':
				  $scope.commandLine='path_delay,20,30,5,7,1.2';
				  break;
			  case 'addEvent':
				  $scope.commandLine='add_event,1,move_bus,11';
				  break;
			  default:
				  $log.info('adding command '+$scope.commandOption+' is not yet supported');
		  }
		  if($scope.commandLine && $scope.commandLine.length>0){
			  mtsService.state.commands.push({index:mtsService.state.commands.length,line:$scope.commandLine,processed:false});
			  mtsService.executeCommand($scope.commandLine);
		  }
		  mtsService.state.editMode=false;
		  mtsService.state.commandOption='';
	  };
	  $scope.cancelAddCommand = function(){
		  $log.info('cancel add command');
		  mtsService.state.editMode=false;
		  mtsService.state.commandOption='';
		  //TODO reset input fields
	  };
	  

	  $scope.getFile = function(){
			$log.info('reading file '+$scope.file.name);
			fileReader.readAsText($scope.file, $scope)
            .then(function(result) {
                 $log.info(result);
                 var commands = result.split("\n"); 
                 $log.info(commands);
                 mtsService.state.commands.splice(0,mtsService.state.commands.length);
                 for(var i=0;i<commands.length;i++){
                	 $log.info(commands[i]);
                	 if(commands[i] && commands[i].length>0){
                		 mtsService.state.commands.push({index:mtsService.state.commands.length,line:commands[i],processed:false});
                		 mtsService.executeCommand(commands[i]);
                	 }
                 }
                 //$log.info(mtsService.state.commands);               
             });			
	  }
  };
  
  angular.module('CommandsConsole')
  .directive('simConfig',[simConfigDirective])
  .directive('commandEntry',[commandEntryDirective])
  .directive("ngFileSelect",['$log',function($log){
	  return {
	    link: function($scope,el){
	      //$log.info('file select directive fired');
	      el.bind("change", function(e){
	      
	        $scope.file = (e.srcElement || e.target).files[0];
	        $scope.getFile();
	      })
	      
	    }
	    
	  }
  }])
  .controller('simConfigController',['$scope', '$log','MTSService', 'FileReader', simConfigController]);
}());
