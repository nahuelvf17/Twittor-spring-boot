package com.restapi.app.twittor.Controller;

import com.restapi.app.twittor.Entity.Usuario;
import com.restapi.app.twittor.Service.UsuarioService;
import IAuthenticationFacade.IAuthenticationFacade;
import io.swagger.annotations.ApiOperation;

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

	@ApiOperation(value = "Gets all values", notes = "Gets all values")
    @GetMapping("/users")
    public Collection<Usuario> getUsers(){
        return usuarioService.getUsuarios();
    }

	@ApiOperation(value = "Get profile", notes = "Gets all values by user by.")
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

	@ApiOperation(value = "Register", notes = "Register new user.")
    @PostMapping(value= {"/registro"})
    public ResponseEntity<?> registro(@RequestBody Usuario usuario) {
    	
    	try {
        	usuarioService.Registro(usuario);
    	}catch(Exception e) {
    		ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    	}
    	
    	return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
	@ApiOperation(value = "Update", notes = "Update user data.")
    @PutMapping(value= {"/modificarPerfil"})
    public ResponseEntity<?> modificarPerfil(@RequestBody Usuario usuarioUpdate){
    	final Authentication authentication = authenticationFacade.getAuthentication();
    	final Usuario usuarioForUpdate = usuarioService.getUserByName(authentication.getName());
    		
    	boolean updateOk = usuarioService.ModificoRegistro(usuarioUpdate, usuarioForUpdate.getId());
    	
    	if(updateOk) {
        	return ResponseEntity.status(HttpStatus.CREATED).build();
    	}else {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error al actualizar el perfil");
    	}
    }
}

