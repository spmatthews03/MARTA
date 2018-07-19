// MTS Dashboard directive
(function(){
  //Directives
  var videoDirective = function(){
  	return{
  		restrict:'E',
  		scope:{src:"@",title:"@"},
  		controller: 'videoController',
  		replace: true,
      template: 
            '<div class="video-panel" layout="column" layout-align="center center">'+
              '<div class="video-title">{{title}}</div>'+
              '<div ng-hide="true" class="video-source">{{src}}</div>'+
              '<video width="640" controls>'+
                 '<source src="{{src}}" type="video/mp4">'+
                 'Your browser does not support the video tag.'+
              '</video>'+
            '</div>'
  	}
  };
  var teamDirective = function(){
  	return{
  		restrict:'E',
  		scope:{},
  		controller: 'teamController',
  		replace: true,
      template: 
            '<div class="team-panel" layout="column" layout-align="center center">'+
              '<div class="team-name">{{team.name}}</div>'+
              '<div class="cards-container">'+
                 '<div class="card-holder" ng-repeat="member in team.members track by $index" ng-show="current==$index">'+
				    '<md-card class="member-panel">'+
				       '<md-card-title-text>'+
                          '<span class="md-headline">{{member.name}}</span>'+
    				   '</md-card-title-text>'+
                       '<md-card-header>'+
                          '<md-card-avatar>'+
                             '<img class="md-user-avatar" src="{{member.photo}}">'+
                          '</md-card-avatar>'+
                       '<md-card-header-text>'+
                          '<span class="md-title">{{member.id}}</span>'+
                          '<span class="md-subhead">{{member.description}}</span>'+
                       '</md-card-header-text>'+
                       '</md-card-header>'+
				    '</md-card>'+
                 '</div>'+  	
                 '<div class="carousel-controls" layout="row" layout-wrap>'+
                    '<md-button ng-click="left()"><md-icon>keyboard_arrow_left</md-icon></md-button>'+
                    '<div flex  layout="row" layout="center center">'+
                       '<md-icon class="indicator" ng-repeat="member in team.members track by $index" ng-class="{\'selected\':current==$index}">lens</md-icon>'+
                    '</div>'+
                    '<md-button ng-click="right()"><md-icon>keyboard_arrow_right</md-icon></md-button>'+
                 '</div>'+  	
              '</div>'+  	
            '</div>'  	
  	}
  };
  
  //controller
  var videoController = function($scope, $log){
		$log.info('videoController');
			
  };
  var teamController = function($scope, $log, $interval){
		$log.info('teamController');
		$scope.delay = 30000; // 3 seconds
		$scope.team = {
			name:"Group A7-8",
			members:[
			   {name:"martin",id:"msmith606",description:"Enterprise architect in Irvine, CA.",photo:"images/martin.png"},
			   {name:"viktor",id:"ym32",description:"viktor description",photo:"images/viktor.png"},
			   {name:"sean",id:"smat",description:"sean description",photo:"images/sean.png"},
			   {name:"sebastian",id:"ssan",description:"sebastian description",photo:"images/sebastian.png"},
			   {name:"yev",id:"ykr",description:"yev description",photo:"images/yev.png"}
			]
		};
		$scope.current=0;
		$scope.left=function(){
			$scope.current = $scope.current-1;
			if($scope.current >= $scope.team.members.length) $scope.current=0;
			if($scope.current < 0) $scope.current= $scope.team.members.length-1;
			$log.info('changing to '+$scope.current);
		};
		$scope.right=function(){
			$scope.current = $scope.current+1;
			if($scope.current >= $scope.team.members.length) $scope.current=0;
			if($scope.current < 0) $scope.current= $scope.team.members.length-1;
		};
		$interval(function(){
			$scope.current = $scope.current+1;
			if($scope.current >= $scope.team.members.length) $scope.current=0;
			if($scope.current < 0) $scope.current= $scope.team.members.length-1;
			$log.info('changing to '+$scope.current);
		},$scope.delay);
  };
  angular.module('Video',['ngMaterial'])
  .directive('a78Video',[videoDirective])
  .directive('team',[teamDirective])
  .controller('teamController',['$scope', '$log', '$interval', teamController])  
  .controller('videoController',['$scope', '$log', videoController]);  
		
}());

