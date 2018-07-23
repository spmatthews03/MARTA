//declare the module and the app level controller
(function(){
	//console.log('bootstrapping angular');
	//defining the application controller
	var appController = function($scope, $log, $rootScope,mtsService){
		$log.info('MTSController');
		$scope.mts = mtsService.state;
		$scope.priorSimToggle=function(){
			mtsService.state.priorSim = !mtsService.state.priorSim;
		};
		$scope.newSimulation = function(){
			mtsService.state.showMenu = false;
			mtsService.state.execMode = false;
			$log.info("new simulation ...");
		};
		$scope.resumeSimulation = function(){
			$log.info("resuming prior simulation ...");
			mtsService.state.execMode=true; //default
			mtsService.state.showMenu= false;
			$scope.activeTab = 1;
		};
		$rootScope.setExecMode=function(mode){
			//$log.info('setting exec mode to '+mode);
			mtsService.state.execMode = false;
			$scope.activeTab = ($scope.execMode ? 0 : 1);
		};
		$scope.toggleShowVideo = function(){
			mtsService.state.showVideo = !mtsService.state.showVideo;
			$log.info('toggle showVideo');
			if(mtsService.state.showVideo) mtsService.state.showMenu = false;
			if(!mtsService.state.showVideo) mtsService.state.showMenu=true;
		}
		$scope.activeTab = 0;
		$log.info('MTSController');
		
	};

	angular.module('MTS',['ngMaterial','CommandsConsole','MTSDashboard','SimExec','d3Module','Report','GTColors','Video'])
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
