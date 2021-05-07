package com.restapi.app.twittor.security.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

	// Espera un json y lo convierte a tipo clase NuevoUsuario
	@PostMapping("/registro")
	public ResponseEntity<?> nuevoUsuario(@RequestBody Usuario usuario) {
		
		usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

		try {
			usuarioService.Registro(usuario);	
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

		}

		return ResponseEntity.status(HttpStatus.CREATED).body("");

	}
	
	@PostMapping("/login")
    public ResponseEntity<JwtDto> login( @RequestBody LoginUsuario loginData){

		final Usuario usuarioLogin = usuarioService.getUserByName(loginData.getEmail());

		
		Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginData.getEmail(),
                                loginData.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(usuarioLogin);
        JwtDto jwtDto = new JwtDto(jwt);

        return new ResponseEntity<>(jwtDto, HttpStatus.OK);
	}
}
