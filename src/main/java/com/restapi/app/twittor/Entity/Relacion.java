package com.restapi.app.twittor.Entity;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document(collection = "relacion")
@JsonInclude(Include.NON_NULL)
public class Relacion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7462765048797699990L;
	
	@Field("usuarioid")
	private String usuarioId;
	
	@Field("usuariorelacionid")
	private String usuarioRelacionId;

	public Relacion(String usuarioId, String usuarioRelacionId) {
		super();
		this.usuarioId = usuarioId;
		this.usuarioRelacionId = usuarioRelacionId;
	}

	public String getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(String usuarioId) {
		this.usuarioId = usuarioId;
	}

	public String getUsuarioRelacionId() {
		return usuarioRelacionId;
	}

	public void setUsuarioRelacionId(String usuarioRelacionId) {
		this.usuarioRelacionId = usuarioRelacionId;
	}

	@Override
	public String toString() {
		return "Relacion [usuarioId=" + usuarioId + ", usuarioRelacionId=" + usuarioRelacionId + "]";
	}
	
}
