package com.restapi.app.twittor.DTO;

import java.io.Serializable;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class DevuelvoTweetsSeguidoresDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5927590390113277130L;
	
	@Field("_id")
	String _id;

	@Field("usuarioid")
	String userId;
	
	@Field("usuariorelacionid")
	String userRelationId;
	
	@Field("tweet")
	TweetDTO Tweet;

	@Override
	public String toString() {
		return "DevuelvoTweetsSeguidoresDTO [id=" + _id + ", usuarioId=" + userId + ", usuarioRelacionId="
				+ userRelationId + ", tweet=" + Tweet + "]";
	}	
}
