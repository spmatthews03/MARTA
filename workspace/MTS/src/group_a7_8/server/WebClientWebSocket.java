package group_a7_8.server;



import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket(maxIdleTime=30000)
public class WebClientWebSocket {
	private UpdateManager updateManager;
	private Session session;

	public WebClientWebSocket(UpdateManager updateManager) {
		this.updateManager = updateManager;
	}

	public Session getSession(){ return session;}
		
	@OnWebSocketConnect
	public void onConnect(Session session){
		this.session = session;
		updateManager.connected(this);
	}

	@OnWebSocketClose
	public void onClose(Session session, int closeCode, String closeReason){
		updateManager.removeSocket(this);
        System.out.println("session closed");
	}
	
    @OnWebSocketMessage
    public void onText(Session session, String message) {
    	
    	updateManager.process(message);
//	        if (session.isOpen()) {
//	        	String response = "{\"eventType\": \"RobotStateNotification\", \"messageId\":25, \"timestamp\": 1458450677922, \"state\":\"AutonomousPeriodic\"}";
//	            System.out.printf("response: %s\n", response);
//	            session.getRemote().sendString(response, null);
//	            
//	        }
        
    }

    @OnWebSocketMessage
    public void onBinary(Session session, byte[] buffer, int offset, int length) {
    	System.out.printf("WARNING: binary message received.  Binary Messages NOT SUPPORTED.  Message ignored\n");
    }
    
    @OnWebSocketError
    public void onError(Session session,Throwable err){
    	System.out.printf("socket error: %s\n",err.getMessage());
    }

}
