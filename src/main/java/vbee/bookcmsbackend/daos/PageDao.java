package vbee.bookcmsbackend.daos;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import vbee.bookcmsbackend.collections.Page;

@Repository
public class PageDao implements IPageDao {
	private final MongoTemplate mongoTemplate;

	@Autowired
	PageDao(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	private static final Logger logger = LoggerFactory.getLogger(PageDao.class);

	@Override
	public List<Page> findAll(String email, String ownerBy) {
		Query query = new Query();
		List<Page> pages = mongoTemplate.find(query, Page.class);
		return pages;
	}

	@Override
	public Page findById(String pageId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(pageId));
		return mongoTemplate.findOne(query, Page.class);
	}
}
