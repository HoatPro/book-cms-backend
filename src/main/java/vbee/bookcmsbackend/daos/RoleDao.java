package vbee.bookcmsbackend.daos;

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
	public Item findAll( String email, String ownerEmail) {
		Query query = new Query();

		List<Role> roles = mongoTemplate.find(query, Role.class);
		System.out.println("query");
		return new Item(roles, null);
	}
	


}
