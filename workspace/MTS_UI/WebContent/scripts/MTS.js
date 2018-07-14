//declare the module and the app level controller
(function(){
	//console.log('bootstrapping angular');
	//defining the application controller
	var appController = function($scope, $log, $attrs, $mdSidenav){
		//$log.info('MTSController');
		$scope.showMenu = true;
		$scope.execMode=true; //default
		$scope.newSimulation = function(){
			$scope.showMenu = false;
			$scope.execMode = false;
			$log.info("new simulation ...");
		};
		$scope.resumeSimulation = function(){
			$log.info("resuming prior simulation ...");
			$scope.execMode=true; //default
			$scope.showMenu = false;
		};
		$scope.setExecMode=function(mode){
			$log.info('setting exec mode to '+mode);
			$scope.execMode = false;
			$scope.activeTab = ($scope.execMode ? 1 : 2);
		};
		$scope.activeTab = 1;
	};

	angular.module('MTS',['ngMaterial','CommandsConsole','MTSDashboard','SimExec'])
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
		.controller('MTSController',['$scope','$log', '$attrs', '$mdSidenav', appController])
		;
}());
