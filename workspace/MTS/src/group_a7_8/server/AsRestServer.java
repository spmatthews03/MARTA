package group_a7_8.server;



import java.sql.SQLException;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;

import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import edu.gatech.SimDriver;
import group_a7_8.FileProps;

public class AsRestServer {

	
	private static UpdateManager updateManager;
	private static SimDriver driver;
	private static final String CONFIG_PATH_TOKEN="-config:";
	private static final String USE_DB="-usedb";
	private static boolean usedb=false;
	
	public static void main(String[] args) throws Exception {
		
		// Create a basic Jetty server object that will listen on port 8080.  Note that if you set this to port 0
	    // then a randomly available port will be assigned that you can either look in the logs for the port,
	    // or programmatically obtain it for use in test cases.
		Integer port = 6310;  //default port #
        String key = "port";
		
		for(String arg:args) {
			  //System.out.printf("arg %s\n",arg);
			  if(arg.startsWith(CONFIG_PATH_TOKEN)) {
				  FileProps.SetConfigPath(arg.substring(CONFIG_PATH_TOKEN.length()).trim());
			  }
			  if(arg.equals(USE_DB)) {
				  usedb = true;
			  }
		}
        if(FileProps.contains(key)) {
        	try{
        	port = Integer.decode(FileProps.get(key));
        	} catch(NumberFormatException nfe) {
        		System.out.printf("WARNING: %s is not a valid value for server port.\n", FileProps.get(key));
        	}
        }
        System.out.printf("Server listening on port %d\n", port);
        Server jettyServer = new Server(port);

	    // Create the ResourceHandler. It is the object that will actually handle the request for a given file. It is
	    // a Jetty Handler object so it is suitable for chaining with other handlers as you will see in other examples.
	    ResourceHandler resource_handler = new ResourceHandler();

	    // Configure the ResourceHandler. Setting the resource base indicates where the files should be served out of.
	    // In this example it is the current directory but it can be configured to anything that the jvm has access to.
	    resource_handler.setDirectoriesListed(true);
	    resource_handler.setWelcomeFiles(new String[]{ "index.html" });
	    resource_handler.setResourceBase("WebContent");
	    resource_handler.setCacheControl("max_age=0");
	    
	    
	    //Create a REST API Context Handler
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/api");
        
        //Create a Websocket handler for pushing state to the browser
//        WebSocketHandler wsHandler = new WebSocketHandler()
//	    {
//	        @Override
//	        public void configure(WebSocketServletFactory factory)
//	        {
//	            factory.setCreator(new WebClientWebSocketCreator(getUpdateManager()));
//	        }
//	    };


	    // Add the ResourceHandler  and rest API handler to the server.
	    HandlerList handlers = new HandlerList();
	    //handlers.setHandlers(new Handler[] { wsHandler, resource_handler,context });
	    handlers.setHandlers(new Handler[] {resource_handler,context });
	    
	    
	    jettyServer.setHandler(handlers);

	    //Configure Jersey handlers
        ServletHolder jerseyServlet = context.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        StringBuilder restImplementationClassNames = new StringBuilder();
        restImplementationClassNames.append(CommandService.class.getCanonicalName());
        restImplementationClassNames.append(";");
        restImplementationClassNames.append(StateService.class.getCanonicalName());
        
        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.classnames",
                restImplementationClassNames.toString());
        
	    // Start things up! By using the server.join() the server thread will join with the current thread.
	    // See "http://docs.oracle.com/javase/1.5.0/docs/api/java/lang/Thread.html#join()" for more details.
	    jettyServer.start();
	    jettyServer.join();
	}

	public static SimDriver getDriver() throws ClassNotFoundException, SQLException, Exception {
		if(driver==null) {
			driver = new SimDriver(usedb);
			driver.checkPriorSim();
			//driver.setUpdateManager(getUpdateManager());
			
			//getUpdateManager().setDriver(driver);
		}
		return driver;
	}


	private static UpdateManager getUpdateManager() {
		if(updateManager==null) updateManager= new UpdateManagerImpl();
		return updateManager;
	}

}
