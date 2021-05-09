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
import com.restapi.app.twittor.security.AuthenticationFacade;

@Service
public class RelacionService {
	
	private final static Logger logger = LoggerFactory.getLogger(RelacionService.class);

	
	@Autowired
	private RelacionDAO relacionDAO;
	
	@Autowired
    private MongoOperations mongoOperation;
	
	@Autowired
	AuthenticationFacade authenticationFacade;
	
	@Autowired
	UsuarioService usuarioService; 
	
	public boolean createRelation(String userRelatedId) throws Exception {
    	
		if(userRelatedId.isEmpty()) {
			throw new Exception("Debe enviar el parametro ID");
		}
		
		
		final String userId = usuarioService.getUserIdByName(authenticationFacade.getAuthentication().getName());
		
		final Relacion relation = new Relacion(userId, userRelatedId);
		
		return relacionDAO.createRelation(relation)!=null;
    }
	
	public Relacion findRelation(String userRelatedId) throws Exception {
    	
		if(userRelatedId.isEmpty()) {
			throw new Exception("Debe enviar el parametro ID");
		}
		
		final String userId = usuarioService.getUserIdByName(authenticationFacade.getAuthentication().getName());

		
		return relacionDAO.findRelation(userId, userRelatedId);
    }
	
	public Relacion findRelation(String userId, String userRelatedId) {
    			
		return relacionDAO.findRelation(userId, userRelatedId);
    }
	
	public Relacion deleteRelation(String userRelatedId) throws Exception {	
		if(userRelatedId.isEmpty()) {
			throw new Exception("Debe enviar el parametro ID");
		}
		
		final String userId = usuarioService.getUserIdByName(authenticationFacade.getAuthentication().getName());

		final Relacion deletedRelation = relacionDAO.deleteRelation(userId, userRelatedId);
		
		if(deletedRelation==null) {
			throw new Exception("Hubo un problema al eliminar");
		}
		
		return deletedRelation;
    }
	
	
	public List<DevuelvoTweetsSeguidoresDTO> getTweetFollowers(final String page) throws Exception{
		
		
		Integer pageInt;
		
		try {
			pageInt = Integer.valueOf(page);	
		}catch(Exception e){
			throw new Exception("Debe enviar el parametro PAGINA como un entero");
		}
		
		if(pageInt<=0) throw new Exception("El parametro pagina debe ser mayor a 0");
		
		
		final String userId = usuarioService.getUserIdByName(authenticationFacade.getAuthentication().getName());
		
		Integer skip = (pageInt - 1) * 20;

		LookupOperation lookupOperation = LookupOperation.newLookup().
	            from("tweet").
	            localField("usuariorelacionid").
	            foreignField("userid").
	            as("tweet");
		
		AggregationOperation match = Aggregation.match(Criteria.where("usuarioid").is(userId));
		
		AggregationOperation unwind =  Aggregation.unwind("tweet");
		
		AggregationOperation pageOper = Aggregation.skip(skip);
		
		AggregationOperation limit = Aggregation.limit(20);

        SortOperation sortOperation = Aggregation.sort(Sort.by(Sort.Direction.DESC, "fecha"));
		
        
		Aggregation aggregation = Aggregation.newAggregation(match, lookupOperation, unwind, pageOper, limit, sortOperation);
		AggregationResults<DevuelvoTweetsSeguidoresDTO> results = mongoOperation.aggregate(aggregation, "relacion", DevuelvoTweetsSeguidoresDTO.class);		
		
		return results.getMappedResults();  
		
	}
	
	public List<UserDTO> getAllUser(String tipo, String page, String search) throws Exception{
		Integer pageInt;
		
		try {
			pageInt = Integer.valueOf(page);	
		}catch(Exception e){
			throw new Exception("Debe enviar el parametro PAGINA como un entero");
		}
		
		if(pageInt <=0 ) {
			throw new Exception("Debe enviar el parametro PAGINA como entero mayor a 0");
		}
		
		
		final String userId = usuarioService.getUserIdByName(authenticationFacade.getAuthentication().getName());
		
		Integer skip = (pageInt - 1) * 20;
		
		AggregationOperation match = Aggregation.match(Criteria.where("nombre").regex("(?i)" + search, "i"));
				
		AggregationOperation pageOper = Aggregation.skip(skip);
		
		AggregationOperation limit = Aggregation.limit(20);		
        
		Aggregation aggregation = Aggregation.newAggregation(match, pageOper, limit);
		
		 List<UserDTO> resultAllUser;
		
		 try {
			 resultAllUser = relacionDAO.getAllUser(aggregation, userId, tipo);
		 }catch(Exception e) {
			 throw e;
		 }
		 
		 return resultAllUser;
	}
	
	
}
