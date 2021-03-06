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
import io.swagger.annotations.ApiOperation;

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
	
	@ApiOperation(value = "Upload avatar", notes = "Upload avatar and update user with url file")
    @PostMapping(value= {"/subirAvatar"})
    public ResponseEntity<?> uploadAvatar(@RequestParam("avatar") MultipartFile avatar) {
                
    	try {
    		saveFileUpdateUser(fileStorageProperties.getPathAvatar(), avatar, "AVATAR");
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Hubo error copiendo la imagen del avatar");	
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }
    
	@ApiOperation(value = "Upload banner", notes = "Upload banner and update user with url file")
    @PostMapping("/subirBanner")
    public ResponseEntity<?> uploadBanner(@RequestParam("banner") MultipartFile banner) {        
        try {
        	saveFileUpdateUser(fileStorageProperties.getPathBanner(), banner, "BANNER");	
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Hubo error copiendo la imagen del banner");
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).body("");
        
    }

	@ApiOperation(value = "Download banner", notes = "Download banner with id user")
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
    
	@ApiOperation(value = "Download avatar", notes = "Download avatar with id user")
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
    		
    	} catch(Exception e) {

    		return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
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
    
    public boolean saveFileUpdateUser(String pathFile, MultipartFile fileRequest, String origin ) throws Exception {
    	
    	final Authentication authentication = authenticationFacade.getAuthentication();
    	String userId = usuarioService.getUserIdByName(authentication.getName());

        fileStorageService.setValueForLocation(pathFile, userId);

        String fileName = fileStorageService.storeFile(fileRequest);
        
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
