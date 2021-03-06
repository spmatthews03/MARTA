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
              '<video width="640" controls>'+
                 '<source src="{{src}}" type="video/mp4">'+
                 'Your browser does not support the video tag.'+
              '</video>'+
              '<div ng-click="toggleShowVideo()"><md-icon class="back-icon">arrow_back</md-icon> Back to Menu</div>'+
            '</div>'
  	}
  };
  var paletteDirective = function(){
    return{
      restrict:'E',
      scope:{},
      controller: 'paletteController',
      replace: true,
      template: 
            '<div class="palette-panel" layout="row" layout-wrap>'+
              '<div ng-repeat="color in colors track by $index" class="color-tile" >'+
                 '<div class="color-swatch {{colors[$index]}}-background-color"></div>'+
                 '<div class="name">{{colors[$index]}}</div>'+
              '</div>'+
              '<div flex></div>'+
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
		//$log.info('videoController');
	  $scope.toggleShowVideo=function(){
		  $log.info($scope);
		  $scope.$parent.toggleShowVideo();
	  };
			
  };
  var paletteController = function($scope, $log,gtColors){
    $log.info('paletteController');
    $scope.colors = gtColors.getColorNames();
  };

  var teamController = function($scope, $log, $interval){
		//$log.info('teamController');
		$scope.delay = 5000; // 5 seconds
		$scope.team = {
			name:"Meet Group A7-8",
			members:[
			   {name:"Martin Smith",id:"msmith606",description:"Enterprise architect in Irvine, CA.",photo:"images/martin.png"},
			   {name:"Yuxuan \"Viktor\" Mu",id:"ymu32",description:"Security consultant to local and international firms in the greater Seattle area",photo:"images/viktor.jpg"},
			   {name:"Sean Matthews",id:"smatthews39",description:"Security Software Engineer working as a contractor the Department of Defense in San Diego, CA",photo:"images/sean.png"},
			   {name:"Sebastian Sanchez",id:"ssanchez44",description:"software engineer working in high performance computing in the Portland area",photo:"images/sebastian.jpg"},
			   {name:"Yevgen Kravets",id:"ykravets3",description:"Senior Software Engineer specializing in Python and ML working in NYC Metro area",photo:"images/yev.jpg"}
			]
		};
		$scope.current=0;
		$scope.left=function(){
			$scope.current = $scope.current-1;
			if($scope.current >= $scope.team.members.length) $scope.current=0;
			if($scope.current < 0) $scope.current= $scope.team.members.length-1;
			//$log.info('changing to '+$scope.current);
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
			//$log.info('changing to '+$scope.current);
		},$scope.delay);
  };
  angular.module('Video',['ngMaterial'])
  .directive('a78Video',[videoDirective])
  .directive('team',[teamDirective])
  .directive('palette',[paletteDirective])
  .controller('paletteController',['$scope', '$log', 'GTColorsService', paletteController])  
  .controller('teamController',['$scope', '$log', '$interval', teamController])  
  .controller('videoController',['$scope', '$log', videoController]);  
		
}());

