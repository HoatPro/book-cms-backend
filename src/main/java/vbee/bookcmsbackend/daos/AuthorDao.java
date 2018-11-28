package vbee.bookcmsbackend.daos;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import vbee.bookcmsbackend.collections.Book;
import vbee.bookcmsbackend.collections.Author;
import vbee.bookcmsbackend.models.Item;
import vbee.bookcmsbackend.utils.HanleUltis;

@Repository
public class AuthorDao implements IAuthorDao {
	private final MongoTemplate mongoTemplate;

	@Autowired
	AuthorDao(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	private static final Logger logger = LoggerFactory.getLogger(AuthorDao.class);

	@Override
	public Item findAll(String keyword, Integer page, Integer size, String fields, String sort, String email,
			String ownerEmail) {
		// default page, size
		if (page == null)
			page = 0;
		Query query = new Query();
		if (keyword != null && !keyword.isEmpty())
			query.addCriteria(Criteria.where("name").regex(keyword));
		query.addCriteria(Criteria.where("ownerBy").is(ownerEmail));

		// count
		int totalPages = 1;
		if (size != null) {
			long count = mongoTemplate.count(query, Author.class);
			totalPages = HanleUltis.countTotalPages(Integer.parseInt("" + count), size);
			// paging & sort
			PageRequest pageRequest = null;
			Sort sortQuery = null;
			String property = "";
			if (sort != null && sort.contains("_DESC")) {
				property = sort.replace("_DESC", "");
				sortQuery = new Sort(Direction.DESC, property);
			} else if (sort != null && sort.contains("_ASC")) {
				property = sort.replaceAll("_ASC", "");
				sortQuery = new Sort(Direction.ASC, property);
			} else {
				property = "createdAt";
				sortQuery = new Sort(Direction.DESC, property);
			}
			pageRequest = new PageRequest(page, size, sortQuery);
			query.with(pageRequest);
		}

		// fields
		if (fields != null && !fields.isEmpty()) {
			String[] fieldArray = fields.split(",");
			for (String field : fieldArray) {
				query.fields().include(field.trim());
			}
		}
		List<Author> authors = mongoTemplate.find(query, Author.class);
		return new Item(authors, totalPages);
	}

	@Override
	public Author findById(String authorId, String email, String ownerEmail) {
		Query query = new Query();
		if (email != null && !email.isEmpty())
			query.addCriteria(Criteria.where("createdBy").is(email));
		query.addCriteria(Criteria.where("ownerBy").is(ownerEmail));
		query.addCriteria(Criteria.where("id").is(authorId));
		return mongoTemplate.findOne(query, Author.class);

	}
}
