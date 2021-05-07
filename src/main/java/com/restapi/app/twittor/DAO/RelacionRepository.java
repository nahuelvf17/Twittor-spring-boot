package com.restapi.app.twittor.DAO;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.restapi.app.twittor.Entity.Relacion;

@Repository
public interface RelacionRepository extends MongoRepository<Relacion, String> {
	
	@Query(value="{ 'usuarioid' : ?0, 'usuariorelacionid' : ?1 }")
    public Relacion getRelation(final String userId, final String usuarioRelatedId);
	
	@Query(value="{ 'usuarioid' : ?0, 'usuariorelacionid' : ?1 }", delete = true)
    public Relacion deleteRelation(final String userId, final String usuarioRelatedId);
}
