// Report directive
(function(){
  //Directives
  var reportDirective = function(){
  	return{
  		restrict:'E',
  		scope:{reportdata:"=",title:"@",summarylabel:"@"},
  		controller: 'reportController',
  		replace: true,
      template: '<div layout="column">'+
                   '<div class="report-title">{{title}}</div>'+
                   '<report-summary-header data-count="reportdata.items.length" data-total="reportdata.total" data-label="summarylabel"></report-summary-header>'+
                   '<div class="bar-chart-body">'+
                     '<bar-item class="bar-item" ng-repeat="item in reportdata.items | orderBy:\'amount\':\'reverse:true\'" data-item="item" data-min="reportdata.min" data-max="reportdata.max" data-index="$index"></bar-item>'+
                   '</div>'+
                '</div>' 
  	}
  };
  var reportSummaryHeaderDirective = function(){
    return{
      restrict:'E',
      scope:{count:"=",total:"=",label:"="},
      controller: 'reportSummaryHeaderController',
      replace: true,
      template: '<div layout="row" class="summary-header" layout-align="end center">'+
                   '<div class="value">{{count}}</div><div class="label">{{label}}</div><div class="value">{{total}}</div></div>'+
                '</div>' 
    }
  };
  var barItemDirective = function(){
    return{
      restrict:'E',
      scope:{item:"=",min:"=",max:"=",index:'='},
      controller: 'barItemController',
      replace: true,
      template: '<div>'+
                    '<div class="bar-chart-item"  layout="row" layout-wrap>'+
                        '<div class="item-label" layout="column" layout-align="center center">{{item.name}}</div>'+
                        '<div flex layout="column">'+
                          '<div class="item-element color-{{(index%colorCount+1)}}" style="width:{{getWidth()}}%;" ng-class=""></div>'+
                        '</div>'+
                        '<div flex="5"></div>'+
                   '</div>'+
                '</div>' 
    }
  };
  //controller
  var barItemController = function($scope, $log){
    //$log.info('barItemController');
    $scope.colorCount=6; //unfortunately color count is hard coded to match the paletted in the css

    $scope.getWidth=function(){
      var width=0;
      width = Math.round($scope.item.amount / $scope.max * 100);
      //$log.info($scope.item);
      //$log.info("width set to "+width+"%");
      return width;
    };

  };

  var reportSummaryHeaderController = function($scope, $log){
    $log.info('reportSummaryHeaderController');
  };
  var reportController = function($scope, $log){
		$log.info('reportController');
  };
  angular.module('Report',['ngMaterial'])
  .directive('reportSummaryHeader',[reportSummaryHeaderDirective])
  .directive('barItem',[barItemDirective])
  .directive('report',[reportDirective])
  .controller('reportSummaryHeaderController',['$scope', '$log', reportSummaryHeaderController])
  .controller('barItemController',['$scope', '$log', barItemController])
  .controller('reportController',['$scope', '$log', reportController]);  
}());
