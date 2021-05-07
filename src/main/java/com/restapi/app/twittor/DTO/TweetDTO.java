package com.restapi.app.twittor.DTO;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class TweetDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 665115323953629241L;
	
	
	String mensaje;
	Date fecha;
	@Field("_id")
	String _id;
	
}
