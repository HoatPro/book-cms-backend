package vbee.bookcmsbackend.daos;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import vbee.bookcmsbackend.collections.Feature;
import vbee.bookcmsbackend.collections.Role;

@Repository
public class RoleDao implements IRoleDao {
	private final MongoTemplate mongoTemplate;

	@Autowired
	RoleDao(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	private static final Logger logger = LoggerFactory.getLogger(RoleDao.class);

	@Override
	public List<Role> findAll(String email, String ownerEmail) {
		Query query = new Query();
		List<Role> roles = mongoTemplate.find(query, Role.class);
		for (Role role : roles) {
			if (role.getIsOwner() != null && role.getIsOwner())
				continue;
			List<Feature> features = new ArrayList<>();
			if(role.getFeatureIds()!=null) {
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
		}

		return roles;
	}

	@Override
	public Role findById(String roleId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(roleId));
		return mongoTemplate.findOne(query, Role.class);
	}

}
