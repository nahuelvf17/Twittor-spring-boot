package com.restapi.app.twittor.security.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restapi.app.twittor.Entity.Usuario;
import com.restapi.app.twittor.Service.UsuarioService;
import com.restapi.app.twittor.security.dto.JwtDto;
import com.restapi.app.twittor.security.dto.LoginUsuario;
import com.restapi.app.twittor.securityJwt.JwtEntryPoint;
import com.restapi.app.twittor.securityJwt.JwtProvider;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UsuarioService usuarioService;

	@Autowired
	JwtProvider jwtProvider;

	private final static Logger logger = LoggerFactory.getLogger(AuthController.class);

	// Espera un json y lo convierte a tipo clase NuevoUsuario
	@PostMapping("/registro")
	public ResponseEntity<?> nuevoUsuario(@RequestBody Usuario usuario) {

		logger.info("aca es nuevo usuario: %s", usuario);

		if (usuario.getEmail().isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El mail de usuario es requerido");
		}

		if (usuario.getPassword().length() < 6) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Debe especificar una contraseña de almenos 6 caracteres");
		}

		if (usuarioService.getUserByName(usuario.getEmail()) != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ya existe un usuario registrado con este email");
		}

		usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

		usuarioService.Registro(usuario);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");

	}
	
	@PostMapping("/login")
    public ResponseEntity<JwtDto> login( @RequestBody LoginUsuario loginData){

		final Usuario usuarioLogin = usuarioService.getUserByName(loginData.getEmail());

		  logger.error(usuarioLogin.toString());

		
		Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginData.getEmail(),
                                loginData.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(usuarioLogin);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        JwtDto jwtDto = new JwtDto(jwt);
  	  	logger.error("aca paso por el nuevo login");

        return new ResponseEntity<>(jwtDto, HttpStatus.OK);
	}
}
