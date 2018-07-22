//declare the module and the app level controller
(function(){
	//console.log('bootstrapping angular');
	//defining the application controller
	var appController = function($scope, $log, $rootScope,mtsService){
		//$log.info('MTSController');
		$scope.showMenu = true;
		$scope.execMode=true; //default
		$scope.mts = mtsService.state;
		$scope.priorSimToggle=function(){
			mtsService.state.priorSim = !mtsService.state.priorSim;
		};
		$scope.newSimulation = function(){
			$scope.showMenu = false;
			$scope.execMode = false;
			$log.info("new simulation ...");
		};
		$scope.resumeSimulation = function(){
			$log.info("resuming prior simulation ...");
			$scope.execMode=true; //default
			$scope.showMenu = false;
			$scope.activeTab = 1;
		};
		$rootScope.setExecMode=function(mode){
			//$log.info('setting exec mode to '+mode);
			$scope.execMode = false;
			$scope.activeTab = ($scope.execMode ? 0 : 1);
		};
		$scope.activeTab = 0;

	};

	angular.module('MTS',['ngMaterial','CommandsConsole','MTSDashboard','SimExec','d3Module','Report','GTColors'])
	.config(['$httpProvider', function ($httpProvider) {
	            // enable http caching
	           $httpProvider.defaults.cache = false;
	      }])
  	 	.config(function($mdThemingProvider) {
		    var greyPalette = $mdThemingProvider.extendPalette('grey',{
		    	'50':'#ffffff'
		    });
		    $mdThemingProvider.definePalette('grey2',greyPalette);

	  		$mdThemingProvider.theme('default')
			    .primaryPalette('blue-grey')
			    .accentPalette('red')
			    // .warnPalette('red')
			    .backgroundPalette('grey2')
			    ; 	 	
		})
		.controller('MTSController',['$scope','$log', '$rootScope','MTSService',  appController])
		;
}());
