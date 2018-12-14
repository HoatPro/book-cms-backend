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

import vbee.bookcmsbackend.collections.Feature;
import vbee.bookcmsbackend.collections.Page;
import vbee.bookcmsbackend.collections.Role;
import vbee.bookcmsbackend.collections.User;
import vbee.bookcmsbackend.collections.UserAdmin;
import vbee.bookcmsbackend.models.Item;
import vbee.bookcmsbackend.utils.HanleUltis;
@Repository
public class UserAdminDao implements IUserAdminDao {
	private final MongoTemplate mongoTemplate;

	@Autowired
	UserAdminDao(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	private static final Logger logger = LoggerFactory.getLogger(UserAdminDao.class);
	
	@Override
	public Item findAll(String keyword, Integer page, Integer size, String email) {
		// default page, size
		if (page == null)
			page = 0;
		Query query = new Query();
		if (keyword != null && !keyword.isEmpty())
			query.addCriteria(Criteria.where("email").regex(keyword));

		// count
		int totalPages = 1;
		if (size != null) {
			long count = mongoTemplate.count(query, User.class);
			totalPages = HanleUltis.countTotalPages(Integer.parseInt("" + count), size);
			// paging & sort
			PageRequest pageRequest = null;
			pageRequest = new PageRequest(page, size);
			query.with(pageRequest);
		}
		List<User> users = mongoTemplate.find(query, User.class);
		for (User user : users) {
			List<Role> roles = new ArrayList<>();
			for (String roleId : user.getRoleIds()) {
				Role role = mongoTemplate.findById(roleId, Role.class);
				if (role != null) {
					List<Feature> features = new ArrayList<>();
					if (role.getFeatureIds() != null) {
						for (String featureId : role.getFeatureIds()) {
							Feature feature = mongoTemplate.findById(featureId, Feature.class);
							if (feature != null) {
								feature.setFrontendKey(null);
								feature.setBackendKey(null);
								features.add(feature);
							}
						}
					}
					role.setFeatureIds(null);
//     				role.setFeatures(features);
					roles.add(role);
				}
			}
			user.setRoleIds(null);
			user.setRoles(roles);
		}

		return new Item(users, totalPages);
	}

	@Override
	public User findById(List<String> userId) {
		User user = mongoTemplate.findById(userId, User.class);
		List<Role> roles = new ArrayList<>();
		for (String roleId : user.getRoleIds()) {
			Role role = mongoTemplate.findById(roleId, Role.class);
			if (role != null) {
				List<Feature> features = new ArrayList<>();
				if (role.getFeatureIds() != null) {
					for (String featureId : role.getFeatureIds()) {
						Feature feature = mongoTemplate.findById(featureId, Feature.class);
						if (feature != null) {
							feature.setFrontendKey(null);
							feature.setBackendKey(null);
							features.add(feature);
						}
					}
				}
				role.setFeatureIds(null);
				role.setFeatures(features);
				roles.add(role);
			}
		}
		user.setRoleIds(null);
		user.setRoles(roles);
		return user;
	}

	@Override
	public UserAdmin findById(String userId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(userId));
		return mongoTemplate.findOne(query, UserAdmin.class);
	}
	

}
