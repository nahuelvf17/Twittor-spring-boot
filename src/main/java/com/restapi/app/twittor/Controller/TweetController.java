package com.restapi.app.twittor.Controller;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.restapi.app.twittor.DTO.TweetListDTO;
import com.restapi.app.twittor.Entity.Tweet;
import com.restapi.app.twittor.Service.TweetService;
import com.restapi.app.twittor.Service.UsuarioService;

import IAuthenticationFacade.IAuthenticationFacade;

@RestController
public class TweetController {
	
	@Autowired
	TweetService tweetService;
	
	@Autowired
	private IAuthenticationFacade authenticationFacade;
	
	@Autowired
	UsuarioService usuarioService;
	
	@PostMapping("/tweet")
    public ResponseEntity<?> graboTweet(@RequestBody Tweet tweet){
        
		final Authentication authentication = authenticationFacade.getAuthentication();
    	final String usuarioId = usuarioService.getUserIdByName(authentication.getName());
		
		final Tweet tweetCreated = tweetService.graboTweet(usuarioId, tweet);
        
        return ResponseEntity.status(HttpStatus.CREATED).body("");
	}
	
	@DeleteMapping("/eliminarTweet")
	public ResponseEntity<?> eliminarTweet(@RequestParam("id") String tweetId){
		logger.info(String.format("Prueba id param: %s", tweetId));
		
		if(tweetId.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Debe enviar el parametro ID");	
		}
		
		try {
			tweetService.borroTweet(tweetId);

		}catch (Exception e){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ocurrio un errorr al borrar el tweet");
		}
		return ResponseEntity.status(HttpStatus.CREATED).body("");
	}
	
	@GetMapping("/leoTweets")
	public ResponseEntity<?> leoTweets(@RequestParam("id") String usuarioId, @RequestParam("pagina") String pagina){
		if(usuarioId.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Debe enviar el parametro ID");	
		}
		
		if(pagina.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Debe enviar el numero de pagina");	
		}
		
		
		Collection<TweetListDTO> tweets = tweetService.leoTweets(usuarioId, pagina);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(tweets);
	}
	
    private final static Logger logger = LoggerFactory.getLogger(TweetController.class);

}
