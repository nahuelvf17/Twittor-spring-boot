package com.restapi.app.twittor.Controller;

import com.restapi.app.twittor.Entity.Usuario;
import com.restapi.app.twittor.Service.UsuarioService;
import com.restapi.app.twittor.securityJwt.JwtTokenFilter;

import IAuthenticationFacade.IAuthenticationFacade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    	
    	final Authentication authentication = authenticationFacade.getAuthentication();
    	logger.error(String.format("ac aes modif: %s", authentication) );
    	logger.error(String.format("ac aes modif: %s", authentication.getName()) );
    	logger.error(String.format("ac aes modif: %s", authentication.getPrincipal()) );

    	if(id.length()<1) {
    		return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Debe enviar el parametro ID");
    	}
    	
    	try {
        	final Usuario usuario = usuarioService.getUsuario(id);
        	return ResponseEntity.status(HttpStatus.CREATED).body(usuario);

    	}catch(RuntimeException exc) {
    		return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(String.format("Ocurrio un error al intentar buscar el registro, o no existe el usuario"));
    	}
    }
    
    @PostMapping(value= {"/registro"})
    public ResponseEntity<?> registro(@RequestBody Usuario usuario) {
    	
    	if(usuario.getEmail().isEmpty()) {
    		return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("El mail de usuario es requerido");
    	}
    	
    	if(usuario.getPassword().length()<6) {
    		return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Debe especificar una contraseña de almenos 6 caracteres");
    	}
    	
    	final Usuario checkUsuario = usuarioService.getUserByName(usuario.getEmail());
    	if(checkUsuario!=null) {
    		return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Ya existe un usuario registrado con este email");
    	}
    	
    	final Usuario newUsuario = usuarioService.Registro(usuario);
    	return ResponseEntity.status(HttpStatus.CREATED).body("");
    }
    
    @PutMapping(value= {"/modificarPerfil"})
    public ResponseEntity<?> modificarPerfil(@RequestBody Usuario usuarioUpdate){
    	final Authentication authentication = authenticationFacade.getAuthentication();
    	final Usuario usuarioForUpdate = usuarioService.getUserByName(authentication.getName());
    		
   
    	logger.info(usuarioForUpdate.toString());
    	
    	boolean updateOk = usuarioService.ModificoRegistro(usuarioUpdate, usuarioForUpdate.getId());
    	
    	if(updateOk) {
        	return ResponseEntity.status(HttpStatus.CREATED).body("");
    	}else {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error al actualizar el perfil");
    	}
    }
}

