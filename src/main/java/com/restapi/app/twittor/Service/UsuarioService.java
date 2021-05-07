package com.restapi.app.twittor.Service;

import com.restapi.app.twittor.DAO.UsuarioDAO;
import com.restapi.app.twittor.Entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioDAO usuarioDAO;

    public Collection<Usuario> getUsuarios(){
        return usuarioDAO.getUsuarios();
    }

    public Usuario getUsuario(String id){
        return usuarioDAO.getUsuario(id);
    }
    
    public Usuario getUserByName(String email){
    	return usuarioDAO.findUsuarioByEmail(email);
    }
    
    public String getUserIdByName(String email){
    	return usuarioDAO.findUsuarioByEmail(email).getId();
    }
    
    public Usuario Registro(Usuario usuario) {
    	return usuarioDAO.createUsuario(usuario);
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
