package com.restapi.app.twittor.DAO;

import com.restapi.app.twittor.Entity.Usuario;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {
	
    @Query(value="{ 'email' : ?0 }")
    public Usuario getUsuarioByEmail(final String email);
    	
}
