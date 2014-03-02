package studymaster.socketserver;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import org.json.JSONObject;
import org.json.JSONArray;
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
        System.out.println("Start server on " + address.getAddress() + " port " + address.getPort());
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

        JSONObject msg = new JSONObject(message);
        String event = msg.getString("event");
        String endpoint = msg.getString("endpoint");
        JSONObject content = msg.getJSONObject("content");

        JSONObject reMsg = new JSONObject();
        JSONObject reContent = new JSONObject();
        reMsg.put("event", event);
        reMsg.put("endpoint", "Server");
        reMsg.put("content", reContent);

        if(event.equals("login")) {
            if(content.getString("account").equals("studymaster") && content.getString("password").equals("e807f1fcf82d132f9bb018ca6738a19f")) {
                reContent.put("status", "success");
            }
            else {
                reContent.put("status", "failed");
                reContent.put("code", "0");
                reContent.put("reason", "Account or Password is wrong.");
            }
        }

        else if(event.equals("profile")) {
            reContent.put("account", "studymaster");
            JSONObject profile = new JSONObject();

            Set<JSONObject> coursesSet = new HashSet();
            JSONObject course = new JSONObject();
            course.put("code", "CZ2001");
            course.put("name", "Algorithms*");
            course.put("examTime", "");
            course.put("status", "book");
            coursesSet.add(course);
            JSONObject course2 = new JSONObject();
            course2.put("code", "CZ2002");
            course2.put("name", "Object Oriented Design & Programming.");
            course2.put("examTime", "");
            course2.put("status", "book");
            coursesSet.add(course2);

            profile.put("courses", coursesSet);

            reContent.put("profile", profile);
        }

        conn.send(reMsg.toString());
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("an error occured on connection " + conn.getRemoteSocketAddress()  + ":" + ex);
    }
}