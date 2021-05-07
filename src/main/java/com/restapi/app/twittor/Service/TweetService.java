package com.restapi.app.twittor.Service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restapi.app.twittor.DAO.TweetDAO;
import com.restapi.app.twittor.Entity.Tweet;

@Service
public class TweetService {
    
	@Autowired
    private TweetDAO tweetDAO;
	
	public Tweet graboTweet(String usuarioId, Tweet tweet) {
    	return tweetDAO.createTweet(usuarioId, tweet);
    }
	
	public void borroTweet(String tweetId) {
    	tweetDAO.deleteTweet(tweetId);
    	return;
    }
	
	public Collection<Tweet> leoTweets(String usuarioId, String pagina){
		return tweetDAO.findTweets(usuarioId, pagina);
	}
}
