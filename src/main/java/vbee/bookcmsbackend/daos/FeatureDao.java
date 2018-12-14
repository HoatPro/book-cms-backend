package vbee.bookcmsbackend.daos;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import vbee.bookcmsbackend.collections.Feature;

@Repository
public class FeatureDao implements IFeatureDao {
	private final MongoTemplate mongoTemplate;

	@Autowired
	FeatureDao(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	private static final Logger logger = LoggerFactory.getLogger(FeatureDao.class);

	@Override
	public List<Feature> findAll(String email, String ownerEmail) {
		Query query = new Query();
		List<Feature> features = mongoTemplate.find(query, Feature.class);
		return features;
	}

	@Override
	public Feature findById(String featureId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(featureId));
		return mongoTemplate.findOne(query, Feature.class);
	}

}
