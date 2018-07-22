package group_a7_8.server;


import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import edu.gatech.SimDriver;


@Path("MTS")
public class StateService {
	
	public StateService() {
	}

	
//you can call this endpoint by using url:
//  http://localhost:6310/api/MTS/reset   
// sample response    {"message":"resetting ...","resultCode":true}
// the response may change as the implementation gets refactored
	
	@GET
    @Path("reset")
    @Produces(MediaType.APPLICATION_JSON)
    public Result resetState(@Context HttpServletRequest request){
		SimDriver driver = AsRestServer.getDriver();
		
		System.out.printf("UI requested state reset\n");
		
		driver.reset();
		
        Result result = new Result();
        result.setMessage("system reset");
        //TODO refactor as needed when integrating with driver
        result.setResultCode(true);
        return result;
    }

//you can call this endpoint by using url:
//  http://localhost:6310/api/MTS/priorSim   
// sample response    {"message":"prior sim ...","resultCode":true}
// the response may change as the implementation gets refactored	
	
	@GET
    @Path("priorSim")
    @Produces(MediaType.APPLICATION_JSON)
    public Result hasPriorSimulation(@Context HttpServletRequest request){
		SimDriver driver = AsRestServer.getDriver();
		
		
		System.out.printf("UI requested has prior sim\n");
        Result result = new Result();
        result.setMessage("prior sim ...");
        //TODO refactor as needed when integrating with driver
        result.setResultCode(driver.hasSavedSimulation());
        return result;
    }
	public static class Result{
        String message;
        boolean resultCode;

        public Result(){}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public boolean isResultCode() {
			return resultCode;
		}

		public void setResultCode(boolean resultCode) {
			this.resultCode = resultCode;
		}

        
    }
}
