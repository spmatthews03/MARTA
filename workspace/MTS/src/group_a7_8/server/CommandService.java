package group_a7_8.server;


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
	
	@GET
    @Path("command")
    @Produces(MediaType.APPLICATION_JSON)
    public Result addCommandt(@QueryParam("line") String userCommandLine,@Context HttpServletRequest request){
		SimDriver driver = AsRestServer.getDriver();
		
		System.out.printf("session id %s\n", request.getSession().getId());
		System.out.printf("driver %s\n", driver);
    	System.out.printf("command %s\n", userCommandLine);
        Result result = new Result(userCommandLine);
        result.setQuit(driver.processCommand(userCommandLine));
        return result;
    }
	public static class Result{
        String userCommandLine;
        boolean quit;

        public Result(){}

        public Result(String userCommandLine) {
            this.userCommandLine = userCommandLine;
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
