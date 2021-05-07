package com.restapi.app.twittor.DAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Component;

import com.restapi.app.twittor.DTO.DevuelvoTweetsSeguidoresDTO;
import com.restapi.app.twittor.DTO.UserDTO;
import com.restapi.app.twittor.Entity.Relacion;
import com.restapi.app.twittor.Entity.Tweet;
import com.restapi.app.twittor.Entity.Usuario;

@Component
public class RelacionDAO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8524513823855301836L;
	
	@Autowired
	private RelacionRepository relacionRepository;
	
	@Autowired
    private MongoOperations mongoOperation;
	
	
	
	
	public Relacion createRelation(Relacion relation) {
		return relacionRepository.save(relation);
	}

	public Relacion findRelation(String userId, String userRelatedId) {
		return relacionRepository.getRelation(userId, userRelatedId);
	}
	
	public Relacion deleteRelation(String userId, String userRelatedId) {
		return relacionRepository.deleteRelation(userId, userRelatedId);
	}
	
	public List<UserDTO> getAllUser(Aggregation aggregation, String userId, String tipo) throws Exception{
		
		
		if(!tipo.equals(FOLLOW_TYPE) && !tipo.equals(NEW_TYPE)){
			throw new Exception("Debe enviar un tipo valido para la busqueda");
		}
		
		
		AggregationResults<Usuario> results = mongoOperation.aggregate(aggregation, "usuarios", Usuario.class);		
		
		final List<Usuario> listUsers =  results.getMappedResults();
		final List<UserDTO> listUserDto= new ArrayList<>();

		ModelMapper modelMapper = new ModelMapper();
		
		listUsers.forEach(user->{
			//final Relacion relacion = new Relacion(userId, user.getId());
			Relacion relacion = findRelation(userId, user.getId());
			boolean located, include;

			include = false;
			located = relacion!=null;
			
			if( tipo.equals(NEW_TYPE) && !located) {
				include = true;
			}
			
			if( tipo.equals(FOLLOW_TYPE) && located) {
				include = true;
			}
			
			if(userId.equals(user.getId())) {
				include = false;
			}

			if(include) listUserDto.add(modelMapper.map(user, UserDTO.class));
		
		});
		
		return listUserDto;
	}
	
	private static String NEW_TYPE = "new";
	private static String FOLLOW_TYPE = "follow";

}
