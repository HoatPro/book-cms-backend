package vbee.bookcmsbackend.daos;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;


import vbee.bookcmsbackend.collections.Voice;
import vbee.bookcmsbackend.models.Item;
import vbee.bookcmsbackend.utils.HanleUltis;
@Repository
public class VoiceDao implements IVoiceDao {
	private final MongoTemplate mongoTemplate;

	@Autowired
	VoiceDao(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	private static final Logger logger = LoggerFactory.getLogger(Voice.class);

	@Override
	public List<Voice> findAll(String email, String ownerBy) {
		Query query = new Query();
		List<Voice> voices = mongoTemplate.find(query, Voice.class);
		return voices;
	}
	
	

}
