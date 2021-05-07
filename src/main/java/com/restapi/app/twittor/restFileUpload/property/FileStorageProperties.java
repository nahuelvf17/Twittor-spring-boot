package com.restapi.app.twittor.restFileUpload.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "upload") 
public class FileStorageProperties {

	private String pathAvatar;
	private String pathBanner;
	public String getPathAvatar() {
		return pathAvatar;
	}
	public void setPathAvatar(String pathAvatar) {
		this.pathAvatar = pathAvatar;
	}
	public String getPathBanner() {
		return pathBanner;
	}
	public void setPathBanner(String pathBanner) {
		this.pathBanner = pathBanner;
	}
	
	
}