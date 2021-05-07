package com.restapi.app.twittor.restFileUpload.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.restapi.app.twittor.Entity.Usuario;
import com.restapi.app.twittor.Service.UsuarioService;
import com.restapi.app.twittor.restFileUpload.property.FileStorageProperties;
import com.restapi.app.twittor.restFileUpload.service.FileStorageService;

import IAuthenticationFacade.IAuthenticationFacade;

@RestController
public class FileUploadController {
	
    private final static Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @Autowired
    FileStorageService fileStorageService;
    
    @Autowired
    FileStorageProperties fileStorageProperties;
    
	@Autowired
    private IAuthenticationFacade authenticationFacade;
	
	@Autowired
	UsuarioService usuarioService;
	
    @PostMapping(value= {"/subirAvatar"})
    public ResponseEntity<?> uploadAvatar(@RequestParam("avatar") MultipartFile avatar) {
                
        if(saveFileUpdateUser(fileStorageProperties.getPathAvatar(), avatar, "AVATAR")) {
            return ResponseEntity.status(HttpStatus.CREATED).body("");
        }
   
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Hubo error copiendo la imagen");

    }
    
    @PostMapping("/subirBanner")
    public ResponseEntity<?> uploadBanner(@RequestParam("banner") MultipartFile banner) {        
        if(saveFileUpdateUser(fileStorageProperties.getPathBanner(), banner, "BANNER")) {
            return ResponseEntity.status(HttpStatus.CREATED).body("");
        }
   
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Hubo error copiendo la imagen");

    }
      
    @GetMapping("/obtenerBanner")
    public ResponseEntity<?> obtenerBanner(@RequestParam String id, HttpServletRequest request) {
        
    	if(id.length()<1) {
    		return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Debe enviar el parametro ID");
    	}
    	
    	Usuario usuario;
    	
    	try {
    		
    		usuario = usuarioService.getUsuario(id);
    		
    	} catch(RuntimeException e) {
    		return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("El usuario no existe");
    	}
    	
    	// Load file as Resource
    	fileStorageService.setValueForLocation(fileStorageProperties.getPathBanner(), "");
        Resource resource = fileStorageService.loadFileAsResource(usuario.getBanner());
        
        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    
    @GetMapping(("/obtenerAvatar"))
    public ResponseEntity<?> obtenerAvatar(@RequestParam String id, HttpServletRequest request) {
    	if(id.length()<1) {
    		return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Debe enviar el parametro ID");
    	}
    	
    	Usuario usuario;
    	
    	try {
    		
    		usuario = usuarioService.getUsuario(id);
    		
    	} catch(RuntimeException e) {
    		return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("El usuario no existe");
    	}
    	
    	// Load file as Resource
    	fileStorageService.setValueForLocation(fileStorageProperties.getPathAvatar(), "");
        Resource resource = fileStorageService.loadFileAsResource(usuario.getAvatar());
        
        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    
    public boolean saveFileUpdateUser(String pathFile, MultipartFile fileRequest, String origin ) {
    	
    	final Authentication authentication = authenticationFacade.getAuthentication();
    	String userId = usuarioService.getUserIdByName(authentication.getName());

        fileStorageService.setValueForLocation(pathFile, userId);

        String fileName = fileStorageService.storeFile(fileRequest);

        logger.info(String.format("Archivo es: %s y el nombre nuevo es : %s", fileRequest, fileName));
        
   
        final Usuario usuarioUpdate = usuarioService.getUsuario(userId);
        if(origin.compareTo("AVATAR")==0) usuarioUpdate.setAvatar(fileName);
        if(origin.compareTo("BANNER")==0) usuarioUpdate.setBanner(fileName);

        return usuarioService.ModificoRegistro(usuarioUpdate);

    }
}