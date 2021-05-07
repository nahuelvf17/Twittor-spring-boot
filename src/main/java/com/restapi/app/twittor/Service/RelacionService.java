package com.restapi.app.twittor.Service;


import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;


import com.restapi.app.twittor.DAO.RelacionDAO;
import com.restapi.app.twittor.DTO.DevuelvoTweetsSeguidoresDTO;
import com.restapi.app.twittor.DTO.UserDTO;
import com.restapi.app.twittor.Entity.Relacion;
import com.restapi.app.twittor.Entity.Usuario;

@Service
public class RelacionService {
	
	private final static Logger logger = LoggerFactory.getLogger(RelacionService.class);

	
	@Autowired
	private RelacionDAO relacionDAO;
	
	@Autowired
    private MongoOperations mongoOperation;
	
	public boolean createRelation(Relacion relation) {
    	return relacionDAO.createRelation(relation)!=null;
    }
	
	public Relacion findRelation(String userId, String userRelatedId) {
    	
		return relacionDAO.findRelation(userId, userRelatedId);
    }
	
	public Relacion deleteRelation(String userId, String userRelatedId) {
    	
		return relacionDAO.deleteRelation(userId, userRelatedId);
    }
	
	
	public List<DevuelvoTweetsSeguidoresDTO> getTweetFollowers(final String userId, final String pagina){
		
		Integer skip = (Integer.valueOf(pagina) - 1) * 20;

		LookupOperation lookupOperation = LookupOperation.newLookup().
	            from("tweet").
	            localField("usuariorelacionid").
	            foreignField("userid").
	            as("tweet");
		
		AggregationOperation match = Aggregation.match(Criteria.where("usuarioid").is(userId));
		
		AggregationOperation unwind =  Aggregation.unwind("tweet");
		
		AggregationOperation page = Aggregation.skip(skip);
		
		AggregationOperation limit = Aggregation.limit(20);

        SortOperation sortOperation = Aggregation.sort(Sort.by(Sort.Direction.DESC, "fecha"));
		
        
		Aggregation aggregation = Aggregation.newAggregation(match, lookupOperation, unwind, page, limit, sortOperation);
		AggregationResults<DevuelvoTweetsSeguidoresDTO> results = mongoOperation.aggregate(aggregation, "relacion", DevuelvoTweetsSeguidoresDTO.class);		
		
		return results.getMappedResults();  
		
	}
	
	public List<UserDTO> getAllUser(String userId, String tipo, Integer pageValue, String search){
		
		Integer skip = (pageValue - 1) * 20;
		
		AggregationOperation match = Aggregation.match(Criteria.where("nombre").regex("(?i)" + search, "i"));
				
		AggregationOperation page  = Aggregation.skip(skip);
		
		AggregationOperation limit = Aggregation.limit(20);		
        
		Aggregation aggregation = Aggregation.newAggregation(match, page, limit);
		
		logger.info(aggregation.toString());
		AggregationResults<Usuario> results = mongoOperation.aggregate(aggregation, "usuarios", Usuario.class);		
		
		final List<Usuario> listUsers =  results.getMappedResults();
		final List<UserDTO> listUserDto= new ArrayList<>();
		
		logger.info("aca llego depsyed del agregation");
		logger.info(listUsers.toString());

		
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
			logger.info("aca llego y tengo datos");
			logger.info(user.toString());
			if(include) listUserDto.add(modelMapper.map(user, UserDTO.class));
		
		});
		
		logger.info("Fin");
		
		logger.info(listUserDto.toString());
		return listUserDto;
	}
	
	private static String NEW_TYPE = "new";
	private static String FOLLOW_TYPE = "follow";

}
