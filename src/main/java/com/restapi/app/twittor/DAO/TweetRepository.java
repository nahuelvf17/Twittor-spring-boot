package com.restapi.app.twittor.DAO;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.restapi.app.twittor.Entity.Tweet;

public interface TweetRepository extends MongoRepository<Tweet, String> {

}
