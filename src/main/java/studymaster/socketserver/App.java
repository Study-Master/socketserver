package studymaster.socketserver;

public class App {
	public static void main(String[] args) {
		SocketServer server = SocketServer.getInstance();
		server.run();
	}
}
