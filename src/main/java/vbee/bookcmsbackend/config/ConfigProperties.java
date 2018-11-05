package vbee.bookcmsbackend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "cust.data.config")
@Configuration("configProperties")
public class ConfigProperties {

	private String wsNomalizationPath;
	private String ttsPath;
	private String appId;
	private String ip;
	private String port;
	private String ssoPath;
	
	public String getWsNomalizationPath() {
		return wsNomalizationPath;
	}

	public void setWsNomalizationPath(String wsNomalizationPath) {
		this.wsNomalizationPath = wsNomalizationPath;
	}

	public String getTtsPath() {
		return ttsPath;
	}

	public void setTtsPath(String ttsPath) {
		this.ttsPath = ttsPath;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getSsoPath() {
		return ssoPath;
	}

	public void setSsoPath(String ssoPath) {
		this.ssoPath = ssoPath;
	}

}
