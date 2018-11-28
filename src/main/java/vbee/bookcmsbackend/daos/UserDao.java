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

import vbee.bookcmsbackend.collections.Author;
import vbee.bookcmsbackend.collections.User;
import vbee.bookcmsbackend.models.Item;
import vbee.bookcmsbackend.utils.HanleUltis;

@Repository
public class UserDao implements IUserDao{
	private final MongoTemplate mongoTemplate;

	@Autowired
	UserDao(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

	@Override
	public Item findAll(String keyword, Integer page, Integer size, String email, String ownerEmail) {
		// default page, size
		if (page == null)
			page = 0;
		Query query = new Query();
		if (keyword != null && !keyword.isEmpty())
			query.addCriteria(Criteria.where("email").regex(keyword));
		query.addCriteria(Criteria.where("ownerBy").is(ownerEmail));
		
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
		return new Item(users, totalPages);
	}
	
	@Override
	public User findById(String userId, String email, String ownerEmail) {
//		Query query = new Query();
//		if (email != null && !email.isEmpty())
//			query.addCriteria(Criteria.where("email").is(email));
//		query.addCriteria(Criteria.where("ownerBy").is(ownerEmail));
//		query.addCriteria(Criteria.where("id").is(userId));
//		System.out.println(query);
		return mongoTemplate.findById(userId, User.class);
	}

}
