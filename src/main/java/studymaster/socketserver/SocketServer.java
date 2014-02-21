package studymaster.socketserver;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class SocketServer extends WebSocketServer {
	private static SocketServer instance;
    private Map<String, WebSocket> clients;

	public static SocketServer getInstance() {
		if(instance == null) {
			String localhost = "0.0.0.0";
			int port = 8087;
			InetSocketAddress address = new InetSocketAddress(localhost, port);
			instance = new SocketServer(address);
		}
		return instance;
	}

	public SocketServer(InetSocketAddress address) {
        super(address);
        clients = new HashMap<String, WebSocket>();
        System.out.println("Start server on " + address.getHostString() + " port " + address.getPort());
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("new connection to " + conn.getRemoteSocketAddress());

    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("received message from " + conn.getRemoteSocketAddress() + ": " + message);
        JSONObject event = new JSONObject(message);

        if(event.getString("event").equals("login")) {
            if(event.getJSONObject("content").getString("account").equals("studymaster") && event.getJSONObject("content").getString("password").equals("e807f1fcf82d132f9bb018ca6738a19f")) {
                JSONObject eventb = new JSONObject();
                JSONObject msgb = new JSONObject();
                msgb.put("status", "success");
                eventb.put("event", "login");
                eventb.put("content", msgb);
                conn.send(eventb.toString());
                return;
            }
            conn.send("error");
        }
        else if(event.getString("event").equals("talk")) {
            WebSocket receiver = clients.get(event.getJSONObject("content").getString("receiver"));
            if(receiver==null)
                conn.send("No such client.");
            else
                receiver.send(event.getJSONObject("content").getString("text"));
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("an error occured on connection " + conn.getRemoteSocketAddress()  + ":" + ex);
    }
}