//declare the module and the app level controller
(function(){
	//console.log('bootstrapping angular');
	//defining the application controller
	var appController = function($scope, $log, $attrs){
		$log.info('MTSController');
		$scope.bus1 = {name:'r1_1',level:30.3};
	};

	angular.module('MTS',['ngMaterial','d3Module',])
		.controller('MTSController',['$scope','$log', '$attrs',  appController])
		;
}());
