package com.restapi.app.twittor.Entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document(collection = "tweet")
@JsonInclude(Include.NON_NULL)
public class Tweet implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7021949247088159250L;

	@Id
    private String id;

    private String userid;
    private String mensaje;
    private Date fecha;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	@Override
	public String toString() {
		return "Tweet [id=" + id + ", userid=" + userid + ", mensaje=" + mensaje + ", fecha=" + fecha + "]";
	}
}
