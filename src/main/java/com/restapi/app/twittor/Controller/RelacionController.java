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
import com.restapi.app.twittor.Service.RelacionService;
import com.restapi.app.twittor.Service.UsuarioService;

import io.swagger.annotations.ApiOperation;


@RestController
public class RelacionController {

	@Autowired
	RelacionService relacionService;
	
	@Autowired
	UsuarioService usuarioService;
	
		
	private final static Logger logger = LoggerFactory.getLogger(RelacionController.class);

	@ApiOperation(value = "Create", notes = "Create new relation, by user id parameter.")
	@PostMapping("/altaRelacion")
    public ResponseEntity<?> altaRelacion(@RequestParam String id){
        
		try {
			relacionService.createRelation(id);
		}catch(final Exception e) {
			
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
	
	
	@ApiOperation(value = "Get relation", notes = "Get relacion by user id parameter.")
	@GetMapping("/consultaRelacion")
    public ResponseEntity<?> consultaRelacion(@RequestParam String id){
		final Relacion findRelation;
		
		try {
			findRelation = relacionService.findRelation(id);	
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
        
		// Response
        Map<String, Boolean> body = new HashMap<>();
	        
		if(findRelation==null) {
			body.put("status", false);
		}
		else {
            body.put("status", true);
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }
	
	@ApiOperation(value = "Delete", notes = "Delte relation.")
	@DeleteMapping("/bajaRelacion")
	public ResponseEntity<?> bajaRelacion(@RequestParam String id){
		final Relacion deletedRelation;
		try {
			deletedRelation = relacionService.deleteRelation(id);

		}catch(final Exception e) {
			return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
		}
		
        return ResponseEntity.status(HttpStatus.CREATED).build();

	}
	
	@ApiOperation(value = "Gets all values", notes = "Gets all tweets, paginated.")
	@GetMapping("/leoTweetsSeguidores")
	public ResponseEntity<?> leoTweetsSeguidores(@RequestParam String pagina){
        
		List<DevuelvoTweetsSeguidoresDTO> followersList;
		
		try {
			followersList = relacionService.getTweetFollowers(pagina);
		}catch(Exception e) {
			return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(followersList);
	}
	
	@ApiOperation(value = "Gets all values", notes = "Gets all values, optional filtered by name, and paginated.")
	@GetMapping("/listaUsuarios")
	public ResponseEntity<?> listaUsuarios(@RequestParam String tipo, @RequestParam String page, @RequestParam(required=false) String search){
				
		final List<UserDTO> results;
		try {
			results = relacionService.getAllUser(tipo, page, search);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		
		return ResponseEntity.status(HttpStatus.CREATED).body(results);
	}
}
