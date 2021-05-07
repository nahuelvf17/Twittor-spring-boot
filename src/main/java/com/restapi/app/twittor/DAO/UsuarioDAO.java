package com.restapi.app.twittor.DAO;

import com.restapi.app.twittor.Entity.Usuario;
import com.restapi.app.twittor.securityJwt.JwtProvider;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;

@Component
public class UsuarioDAO implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final static Logger logger = LoggerFactory.getLogger(UsuarioDAO.class);

	
	@Autowired
	private MongoOperations mongoOperations;
	
	
	@Autowired
    private UsuarioRepository repository;

    public Collection<Usuario> getUsuarios(){
        return repository.findAll();
    }

    public Usuario getUsuario(String id){
        final Usuario usuario = repository.findById(id).orElse(null);
        if(usuario==null) {
        	throw new RuntimeException();
        }
        usuario.setPassword(null);
        return usuario;
    }
    
    public Usuario createUsuario(Usuario usuario) {
    	return repository.save(usuario);
    }
    
    public Usuario findUsuarioByEmail(String email) {
    	logger.info("aca viene buscar por email");
    	logger.info(email);
    	return repository.getUsuarioByEmail(email);
    }
    
    public Usuario modificoRegistro(Usuario usuarioUpdate, String usuarioId) {
    	
    	Document document = new Document();
    	mongoOperations.getConverter().write(usuarioUpdate, document);
    	Update update = new Update();
    	document.forEach(update::set);
    	
    	//build query
    	Query query = new Query(Criteria.where("_id").is(usuarioId));
    	
    	mongoOperations.upsert(query, update, "usuarios");
    	
    	return repository.findById(usuarioId).get();
    }
    
    public Usuario modificoRegistro(Usuario usuarioUpdate) {
    	
    	return repository.save(usuarioUpdate);
    }
}
