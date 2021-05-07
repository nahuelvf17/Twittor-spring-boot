package com.restapi.app.twittor.Controller;

import com.restapi.app.twittor.Entity.Usuario;
import com.restapi.app.twittor.Service.UsuarioService;
import IAuthenticationFacade.IAuthenticationFacade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private IAuthenticationFacade authenticationFacade;

    private final static Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    
    @GetMapping("/users")
    public Collection<Usuario> getUsers(){
        return usuarioService.getUsuarios();
    }

    @GetMapping(value= {"/verperfil"})
    public ResponseEntity<?>  verPerfil(@RequestParam String id) {
    	final Usuario usuario;
    	try {
        	usuario = usuarioService.getUsuario(id);

    	}catch(Exception e) {
    		return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
    	}
    	
    	return ResponseEntity.status(HttpStatus.CREATED).body(usuario);

    }
    
    @PostMapping(value= {"/registro"})
    public ResponseEntity<?> registro(@RequestBody Usuario usuario) {
    	
    	try {
        	usuarioService.Registro(usuario);
    	}catch(Exception e) {
    		ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    	}
    	
    	return ResponseEntity.status(HttpStatus.CREATED).body("");
    }
    
    @PutMapping(value= {"/modificarPerfil"})
    public ResponseEntity<?> modificarPerfil(@RequestBody Usuario usuarioUpdate){
    	final Authentication authentication = authenticationFacade.getAuthentication();
    	final Usuario usuarioForUpdate = usuarioService.getUserByName(authentication.getName());
    		
    	boolean updateOk = usuarioService.ModificoRegistro(usuarioUpdate, usuarioForUpdate.getId());
    	
    	if(updateOk) {
        	return ResponseEntity.status(HttpStatus.CREATED).body("");
    	}else {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error al actualizar el perfil");
    	}
    }
}

