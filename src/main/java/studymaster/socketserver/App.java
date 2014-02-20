package studymaster.socketserver;

public class App {
	public static void main(String[] args) {
		SocketServer ss = SocketServer.getInstance();
		ss.start();
	}
}
