package group_a7_8.server;

import java.io.IOException;

public interface  UpdateManager {

	void process(String message);

	void removeSocket(WebClientWebSocket webClientWebSocket);

	void connected(WebClientWebSocket webClientWebSocket);
	
	void post(String message) throws IOException;

}
