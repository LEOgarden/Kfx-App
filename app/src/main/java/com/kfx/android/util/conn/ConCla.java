package com.kfx.android.util.conn;

import com.kfx.android.util.common.Properties;

public class ConCla {
	// http://17.18.2.109:8080/Kfx-Azd-Net/maps.json
	public static String getConCla(String ip, String port, String procName,
			String action) {
		StringBuilder sb = new StringBuilder();
		sb.append("http://");
		if (ip != null && ip.length() > 0) {
			sb.append(ip);
		} else {
			sb.append("localhost");
		}
		sb.append(":");
		if (port != null && port.length() > 0) {
			sb.append(port);
		} else {
			sb.append("8080");
		}
		sb.append("/");
		sb.append(procName);
		sb.append("/");
		sb.append(action);
		return sb.toString();
	}

	public static void main(String[] args) {
		getConCla(Properties.web_ip, Properties.web_port, Properties.web_name,Properties.web_action_map);
	}
}
