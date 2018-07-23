package group_a7_8.server;


import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import edu.gatech.SimDriver;

@Path("MTS")
public class CommandService {
	
	public CommandService() {
	}
	
//you can call this endpoint by using url:
//  http://localhost:6310/api/MTS/command?line=add_stop,0,Appleville,5,0.0,0.08
// sample response    {"commandLine":"add_stop,0,Appleville,5,0.0,0.08","quit":false}
//  http://localhost:6310/api/MTS/command?line=quit
// sample response    {"commandLine":"quit","quit":true}
// the response may change as the implementation gets refactored
	
	@GET
    @Path("command")
    @Produces(MediaType.APPLICATION_JSON)
    public Result addCommandt(@QueryParam("line") String userCommandLine,@Context HttpServletRequest request){
		SimDriver driver;
        Result result = new Result(userCommandLine);
		try {
	    	System.out.printf("executing command: %s\n", userCommandLine);
			driver = AsRestServer.getDriver();
	        result.setQuit(driver.processCommand(userCommandLine));
			result.setState(driver.toJSON());
		} catch (Exception e) {
			e.printStackTrace();
	        System.out.printf("ERROR: %s", e.getMessage());
	        result.setQuit(false);
		} 
        return result;
    }
	public static class Result{
        public String state;
		String userCommandLine;
        boolean quit;

        public Result(){}

        public Result(String userCommandLine) {
            this.userCommandLine = userCommandLine;
        }


        public String getSetState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public boolean getQuit() {
            return quit;
        }

        public void setQuit(boolean quit) {
            this.quit = quit;
        }
        public String getCommandLine() {
            return userCommandLine;
        }

    }
}
