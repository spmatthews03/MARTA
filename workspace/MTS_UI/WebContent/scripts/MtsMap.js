// MTS Map directive
(function(){
  //Directives
  var mapDirective = function(){
  	return{
  		restrict:'E',
  		scope:{},
  		controller: 'mapController',
  		replace: true,
      template: '<div id="viz" style="widht:100%;height=:100%" class="viz"></div>',
      link: function(scope, element, attrs,d3Service) {
           
        }
  	}
  };
  //controller
  var mapController = function($scope, $log){
		$log.info('mapController');
  };
  angular.module('MTSMap',['ngMaterial','d3'])
  .directive('mtsMap',[mapDirective])
  .controller('mapController',['$scope', '$log', 'd3Service', mapController]);  
}());