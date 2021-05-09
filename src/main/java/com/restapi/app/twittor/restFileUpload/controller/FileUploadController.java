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
                
    	try {
    		saveFileUpdateUser(fileStorageProperties.getPathAvatar(), avatar, "AVATAR");
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Hubo error copiendo la imagen del avatar");	
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }
    
    @PostMapping("/subirBanner")
    public ResponseEntity<?> uploadBanner(@RequestParam("banner") MultipartFile banner) {        
        try {
        	saveFileUpdateUser(fileStorageProperties.getPathBanner(), banner, "BANNER");	
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Hubo error copiendo la imagen del banner");
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).body("");
        
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
    		
    	} catch(Exception e) {
    		return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
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
    	logger.info("aca1");
    	if(id.length()<1) {
    		return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Debe enviar el parametro ID");
    	}
    	
    	Usuario usuario;
    	logger.info("aca");

    	try {
    		
    		usuario = usuarioService.getUsuario(id);
    		
    	} catch(Exception e) {
        	logger.info("aca3");

    		return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
    	}
    	
    	// Load file as Resource
    	fileStorageService.setValueForLocation(fileStorageProperties.getPathAvatar(), "");
        Resource resource = fileStorageService.loadFileAsResource(usuario.getAvatar());
    	logger.info("aca4");

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

    	logger.info("aca5");

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }
    	logger.info("aca6");

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    
    public boolean saveFileUpdateUser(String pathFile, MultipartFile fileRequest, String origin ) throws Exception {
    	
    	final Authentication authentication = authenticationFacade.getAuthentication();
    	String userId = usuarioService.getUserIdByName(authentication.getName());

        fileStorageService.setValueForLocation(pathFile, userId);

        String fileName = fileStorageService.storeFile(fileRequest);

        logger.info(String.format("Archivo es: %s y el nombre nuevo es : %s", fileRequest, fileName));
        
        Usuario usuarioUpdate;
        try {
            usuarioUpdate = usuarioService.getUsuario(userId);	
        }catch(Exception e) {
        	throw e;
        }
        
        if(origin.compareTo("AVATAR")==0) usuarioUpdate.setAvatar(fileName);
        if(origin.compareTo("BANNER")==0) usuarioUpdate.setBanner(fileName);

        return usuarioService.ModificoRegistro(usuarioUpdate);

    }
}
