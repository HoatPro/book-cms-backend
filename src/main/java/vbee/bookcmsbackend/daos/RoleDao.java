package vbee.bookcmsbackend.daos;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import vbee.bookcmsbackend.collections.Feature;
import vbee.bookcmsbackend.collections.Role;
import vbee.bookcmsbackend.models.Item;
import vbee.bookcmsbackend.utils.HanleUltis;

@Repository
public class RoleDao implements IRoleDao{
	private final MongoTemplate mongoTemplate;

	@Autowired
	RoleDao(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	private static final Logger logger = LoggerFactory.getLogger(RoleDao.class);

	@Override
	public List<Role> findAll( String email, String ownerEmail) {
		Query query = new Query();
		List<Role> roles = mongoTemplate.find(query, Role.class);
		for (Role role : roles) {
			if (role.getIsOwner() != null && role.getIsOwner()) continue;
			List<Feature> features = new ArrayList<>();
			for (String featureId : role.getFeatureIds()) {
				Feature feature = mongoTemplate.findById(featureId, Feature.class);
				if (feature != null) {
					feature.setFrontendKey(null);
					feature.setBackendKey(null);
					features.add(feature);
				}
			}
			role.setFeatureIds(null);
			role.setFeatures(features);
		}
		
		
		return  roles;
	}
	


}
