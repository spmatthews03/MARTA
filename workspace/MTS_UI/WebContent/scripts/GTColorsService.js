(function(){

var service = function ($log){
   $log.info("GT ColorService");
    
   var colors = [
      "blue",
      "tech-gold",
      "buzz-gold",
      "buzz-gold-60",
      "blue-80",
      "link-blue",
      "link-hover-blue",
      "dark-gray",
      "medium-gray",
      "light-gray",
      "gray-matter",
      "atlanta-fog",
      "mortar",
      "pi-mile",
      "diploma",
      "tower-patina",
      "bobby-jones",
      "burger-bowl",
      "whistle",
      "georgia-clay",
      "horizon"
   ];

   var getColorClass = function(index){
      return colors[index%colors.length]+'-color';
   }

   var getColorNames = function(){
      return colors;
   }
   var getBackgroundColorClass = function(index){
      return colors[index%colors.length]+'-background-color';
   }

   var getPaletteSize = function(){
      return colors.length;
   }
	
    return {
    	getColorClass: getColorClass,
    	getBackgroundColorClass: getBackgroundColorClass,
    	getPaletteSize:getPaletteSize,
      getColorNames:getColorNames
    };
    
  };
  

  

angular.module('GTColors',[])
  .factory ("GTColorsService", ['$log', service]);  
}());
