package com.restapi.app.twittor.DTO;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class TweetListDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -92544347495763144L;
	
	Date fecha;
    String mensaje;
    String userId;

    @JsonProperty ("_id") 
    String id;

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setId(String id) {
		this.id = id;
	}
   
}
