package com.restapi.app.twittor.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.restapi.app.twittor.DTO.DevuelvoTweetsSeguidoresDTO;
import com.restapi.app.twittor.DTO.UserDTO;
import com.restapi.app.twittor.Entity.Relacion;
import com.restapi.app.twittor.Entity.Tweet;
import com.restapi.app.twittor.Service.RelacionService;
import com.restapi.app.twittor.Service.UsuarioService;
import com.restapi.app.twittor.security.AuthenticationFacade;
import com.restapi.app.twittor.security.Controller.AuthController;


@RestController
public class RelacionController {

	@Autowired
	RelacionService relacionService;
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	AuthenticationFacade authenticationFacade;
		
	private final static Logger logger = LoggerFactory.getLogger(RelacionController.class);
		
	@PostMapping("/altaRelacion")
    public ResponseEntity<?> altaRelacion(@RequestParam String id){
        
		if(id.isEmpty()) {
			return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Debe enviar el parametro ID");
		}
		
		final String userId = usuarioService.getUserIdByName(authenticationFacade.getAuthentication().getName());
		
		final Relacion relacion = new Relacion(userId, id);
        
		logger.info(relacion.toString());
		
		if(!relacionService.createRelation(relacion)) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Hubo un erro al intentar grabar la relacion");
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }
	
	@GetMapping("/consultaRelacion")
    public ResponseEntity<?> consultaRelacion(@RequestParam String id){
		
		if(id.isEmpty()) {
			return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Debe enviar el parametro ID");
		}
		
		final String userRelatedId = id;
		final String userId = usuarioService.getUserIdByName(authenticationFacade.getAuthentication().getName());
		
		logger.info(userId);
		logger.info(userRelatedId);
		final Relacion findRelation = relacionService.findRelation(userId, userRelatedId);
        
		if(findRelation!=null)
			logger.info(findRelation.toString());
		else
			logger.info("aca findRelation es null");
		// Response
        Map<String, String> body = new HashMap<>();
	        
		if(findRelation==null) {
			body.put("status", "false");
		}
		else {
            body.put("status", "true");
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }
	
	@DeleteMapping("/bajaRelacion")
	public ResponseEntity<?> bajaRelacion(@RequestParam String id){

		if(id.isEmpty()) {
			return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Debe enviar el parametro ID");
		}
		
		final String userRelatedId = id;
		final String userId = usuarioService.getUserIdByName(authenticationFacade.getAuthentication().getName());
		
		final Relacion deletedRelation = relacionService.deleteRelation(userId, userRelatedId);
		if(deletedRelation!=null) logger.info(deletedRelation.toString());
		if(deletedRelation==null) {
			return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Hubo un problema al eliminar");
		}
		
        return ResponseEntity.status(HttpStatus.CREATED).body("");

	}
	
	@GetMapping("/leoTweetsSeguidores")
	public ResponseEntity<?> leoTweetsSeguidores(@RequestParam String pagina){
        
		final String userId = usuarioService.getUserIdByName(authenticationFacade.getAuthentication().getName());
		
		if(pagina.isEmpty()) {
			return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Debe enviar el parametro PAGINA");
		}
			
		List<DevuelvoTweetsSeguidoresDTO> followersList = relacionService.getTweetFollowers(userId, pagina);
		
		logger.info(followersList.toString());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(followersList);
	}
	
	@GetMapping("/listaUsuarios")
	public ResponseEntity<?> listaUsuarios(@RequestParam String tipo, @RequestParam String page, @RequestParam String search){
		
		final String userId = usuarioService.getUserIdByName(authenticationFacade.getAuthentication().getName());
		
		Integer pageValue;
		
		try {
			pageValue = Integer.valueOf(page);	
		}catch(Exception e){
			return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Debe enviar el parametro PAGINA como un entero");
		}
		
		if(pageValue <=0 ) {
			return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Debe enviar el parametro PAGINA como entero mayor a 0");
		}
		
		final List<UserDTO> results = relacionService.getAllUser(userId, tipo, pageValue, search);
		
		
		return ResponseEntity.status(HttpStatus.CREATED).body(results);
	}
}
