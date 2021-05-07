package com.restapi.app.twittor.Service;

import com.restapi.app.twittor.DAO.UsuarioDAO;
import com.restapi.app.twittor.Entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioDAO usuarioDAO;

    public Collection<Usuario> getUsuarios(){
        return usuarioDAO.getUsuarios();
    }

    public Usuario getUsuario(String id) throws Exception{
        
    	if(id.length()<1) {
    		throw new Exception("Debe enviar el parametro ID");
    	}
    	
    	return usuarioDAO.getUsuario(id);
    }
    
    public Usuario getUserByName(String email){
    	return usuarioDAO.findUsuarioByEmail(email);
    }
    
    public String getUserIdByName(String email) {
    	return usuarioDAO.findUsuarioByEmail(email).getId();
    }
    
    public Usuario Registro(Usuario usuario) throws Exception{
    	
    	if(usuario.getEmail().isEmpty()) {
    		throw new Exception("El mail de usuario es requerido");
    	}
    	
    	if(usuario.getPassword().length()<6) {
    		throw new Exception("Debe especificar una contraseña de almenos 6 caracteres");
    	}
    	
    	final Usuario checkUsuario = getUserByName(usuario.getEmail());
    	if(checkUsuario!=null) {
    		throw new Exception("Ya existe un usuario registrado con este email");
    	}
    	Usuario userFound;
    	try {
        	userFound = usuarioDAO.createUsuario(usuario);
    	}catch(Exception e) {
    		throw e;
    	}
    	
    	return userFound;
    }
    
    public Boolean ModificoRegistro(Usuario usuarioUpdate, String usuarioId) {
    	final Usuario usuarioUpdated = usuarioDAO.modificoRegistro(usuarioUpdate, usuarioId);
    	return usuarioUpdated!=null;
    }
    
    public Boolean ModificoRegistro(Usuario usuarioUpdate) {
    	final Usuario usuarioUpdated = usuarioDAO.modificoRegistro(usuarioUpdate);
    	return usuarioUpdated!=null;
    }
}
