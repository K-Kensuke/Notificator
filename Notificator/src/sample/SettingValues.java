package sample;

import java.io.*;
import java.util.Map;
import java.util.Properties;

public class SettingValues {
	private static SettingValues settingValues = new SettingValues();
	private String ServerIP = "";
	private int ServerPort = 0;

	public static SettingValues getInstance () {
		return settingValues;
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

	public void readValues () throws IOException {
		Properties properties = new Properties();

		try {
			InputStream inputStream = new FileInputStream("properties.xml");

			// このメソッドが終わるとinputStreamはcloseされる．
			properties.loadFromXML(inputStream);

			for (Map.Entry<Object, Object> entry : properties.entrySet()) {
				switch (String.valueOf(entry.getKey())) {
					case "ServerIP":
						ServerIP = String.valueOf(entry.getValue());
						break;
					case "ServerPORT":
						ServerPort = Integer.valueOf(String.valueOf(entry.getValue()));
						break;
				}
			}
		}
		catch (FileNotFoundException e) {
			// ファイルが存在しなければ，何もしない
		}
	}

	public void writeValues () throws IOException {
		Properties properties = new Properties();
		properties.setProperty("ServerIP", ServerIP);
		properties.setProperty("ServerPORT", String.valueOf(ServerPort));

		OutputStream outputStream = new FileOutputStream("properties.xml");
		properties.storeToXML(outputStream, "各種設定値", "UTF-8");

		outputStream.close();
	}

}
