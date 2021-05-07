package com.restapi.app.twittor.DTO;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class UserDTO implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 5102387453777382099L;

	@Field("id")
	String _id;
    
    String nombre;
    String apellidos;
    Date fechaNacimiento;
	

	public void setId(String id) {
		this._id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
    
    
}
