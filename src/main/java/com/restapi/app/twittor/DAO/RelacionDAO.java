package com.restapi.app.twittor.DAO;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.restapi.app.twittor.DTO.DevuelvoTweetsSeguidoresDTO;
import com.restapi.app.twittor.Entity.Relacion;
import com.restapi.app.twittor.Entity.Tweet;

@Component
public class RelacionDAO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8524513823855301836L;
	
	@Autowired
	private RelacionRepository relacionRepository;
	
	public Relacion createRelation(Relacion relation) {
		return relacionRepository.save(relation);
	}

	public Relacion findRelation(String userId, String userRelatedId) {
		return relacionRepository.getRelation(userId, userRelatedId);
	}
	
	public Relacion deleteRelation(String userId, String userRelatedId) {
		return relacionRepository.deleteRelation(userId, userRelatedId);
	}
	
	public List<DevuelvoTweetsSeguidoresDTO> getTweetFollowers(String userId, String pagina){
		return getTweetFollowers(userId, pagina);
	}
}
