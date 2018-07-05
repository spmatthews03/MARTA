//declare the module and the app level controller
(function(){
	//console.log('bootstrapping angular');
	//defining the application controller
	var appController = function($scope, $log, $attrs, $mdSidenav, fileReader){
		//$log.info('MTSController');

		$scope.getFile = function(){
			$log.info('reading file '+$scope.file.name);
			fileReader.readAsText($scope.file, $scope)
            .then(function(result) {
                 //$log.info(result);
                 $scope.commands = result.split("\n");
                 //$log.info($scope.commands);               
             });			
		}
	};

	angular.module('MTS',['ngMaterial','CommandsConsole','MTSDashboard'])
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
		.controller('MTSController',['$scope','$log', '$attrs', '$mdSidenav', 'FileReader', appController])
		;
}());
