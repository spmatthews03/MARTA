package group_a7_8.server;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

public class WebClientWebSocketCreator implements WebSocketCreator{
	private UpdateManager updateManager;
	public WebClientWebSocketCreator(UpdateManager updateManager) {
		//System.out.println("WebClientWebSocketCreator constructor");
		this.updateManager = updateManager;
	}
	public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse res) {
		//System.out.println("WebClientWebSocketCreator createWebSocket()");
		return new WebClientWebSocket(updateManager);
	}
}
