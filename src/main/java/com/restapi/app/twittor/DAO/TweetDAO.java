package com.restapi.app.twittor.DAO;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.restapi.app.twittor.Controller.TweetController;
import com.restapi.app.twittor.Entity.Tweet;

@Component
public class TweetDAO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1957569239062021555L;
	
	@Autowired
	TweetRepository tweetRepository;
	
	@Autowired
	private MongoOperations mongoOperations;
	
	public Tweet createTweet(String usuarioId, Tweet tweet) {
		// set values
		final LocalDateTime localDateTime  = LocalDateTime.now();
		tweet.setUserid(usuarioId);
		tweet.setFecha(Date.from(localDateTime.toInstant(ZoneOffset.UTC)));
				
		return tweetRepository.save(tweet);
	}
	
	public void deleteTweet(String tweetId) {
		tweetRepository.deleteById(tweetId);
		return;
	}
	
	public Collection<Tweet> findTweets(String usuarioId, String pagina){
		Query query = new Query();
	    query.addCriteria(Criteria.where("userid").is(usuarioId));
	    query.limit(20);
	    query.skip((Integer.valueOf(pagina)-1)*20);
	    query.with(Sort.by(Sort.Order.desc("fecha")));
	    
	    return mongoOperations.find(query, Tweet.class);
	}
	
    private final static Logger logger = LoggerFactory.getLogger(TweetDAO.class);

}

