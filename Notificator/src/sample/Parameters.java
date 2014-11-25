package sample;

public class Parameters {
	private static Parameters parameters = new Parameters();
	private String ServerIP = "";
	private int ServerPort = 0;

	public static Parameters getInstance () {
		return parameters;
	}

	public String getServerIP () {
		return ServerIP;
	}

	public void setServerIP (String serverIP) {
		ServerIP = serverIP;
	}

	public int getServerPort () {
		return ServerPort;
	}

	public void setServerPort (int serverPort) {
		ServerPort = serverPort;
	}
}
