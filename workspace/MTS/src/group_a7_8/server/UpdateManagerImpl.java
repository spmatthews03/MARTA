package group_a7_8.server;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;


public class UpdateManagerImpl implements UpdateManager {
	private WebClientWebSocket socket;

	@Override
	public void process(String message) {
		//System.out.printf("message received: %s\n",message);
		//method to process updates that came fromt the websocket
		//System.out.printf("WARNING: Processing messages from web client to server via sockets is not yet supported.  Message ignored.\n");
	}

	@Override
	public void removeSocket(WebClientWebSocket socket) {
		socket = null;
		System.out.printf("closing socket\n");
	}

	@Override
	public void connected(WebClientWebSocket socket) {
		this.socket=socket;
		System.out.printf("socket connected\n");
		
	}

	@Override
	public void post(String message) throws IOException {
		if(socket==null) {
			System.out.printf("ERROR:  socket is unexpectedly null\n");
			return;
		}
		Session session = socket.getSession();
		if(session==null || !session.isOpen()) {
			System.out.printf("ERROR:  socket session is unexpectedly null or closed\n");
			return;			
		}
		//System.out.printf("posting message:\n---------------------\n%s\n---------------------\\n", message);
		session.getRemote().sendString(message);
		
	}
}
