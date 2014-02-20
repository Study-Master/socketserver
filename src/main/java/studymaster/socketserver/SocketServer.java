package studymaster.socketserver;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class SocketServer extends WebSocketServer {
	private static SocketServer instance;

	private SocketServer(InetSocketAddress address) {
        super(address);
		System.out.println("Start server on " + address.getHostString() + " port " + address.getPort());
    }

	public static SocketServer getInstance() {
		if(instance == null) {
			String localhost = "0.0.0.0";
			int port = 31416;
			InetSocketAddress address = new InetSocketAddress(localhost, port);
			instance = new SocketServer(address);
		}
		return instance;
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		System.out.println("New connection to " + conn.getRemoteSocketAddress());
	}

	@Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
    }

    @Override
    public synchronized void onMessage(WebSocket conn, String message) {
    	System.out.println("received message from " + conn.getRemoteSocketAddress() + ": " + message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
    	System.err.println(ex.toString());
    }
}