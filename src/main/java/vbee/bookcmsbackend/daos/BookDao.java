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

import vbee.bookcmsbackend.collections.Book;
import vbee.bookcmsbackend.models.Item;
import vbee.bookcmsbackend.services.books.IAuthorService;
import vbee.bookcmsbackend.services.books.ICategoryService;
import vbee.bookcmsbackend.services.common.IStatusService;
import vbee.bookcmsbackend.utils.HanleUltis;

@Repository
public class BookDao implements IBookDao {

	private final MongoTemplate mongoTemplate;

	@Autowired
	BookDao(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Autowired
	ICategoryService categoryService;

	@Autowired
	IAuthorService authorService;

	@Autowired
	IStatusService statusService;

	private static final Logger logger = LoggerFactory.getLogger(BookDao.class);

	@Override
	public Item findAllBooks(String categoryId, String statusIds, String keyword, Integer page, Integer size,
			String fields, String sort, String createdByEmail, String ownerByEmail) {
		// defaul page, size
		if (page == null)
			page = 0;
		if (size == null)
			size = 10;
		Criteria criteriaFinal = new Criteria();
		List<Criteria> criterias = new ArrayList<Criteria>();

		// categoryIds
		if (categoryId != null && !categoryId.isEmpty()) {
			criterias.add(Criteria.where("categoryIds").all(categoryId.trim()));
		}

		// statusIds
		if (statusIds != null && !statusIds.isEmpty()) {
			Criteria criteriaOrStatus = new Criteria();
			List<Criteria> criteriaOrStatusList = new ArrayList<Criteria>();
			String[] statusRaw = statusIds.trim().split(",");
			for (String statusId : statusRaw) {
				try {
					criteriaOrStatusList.add(Criteria.where("statusId").is(Integer.parseInt(statusId)));
				} catch (NumberFormatException e) {
					logger.info("Parse statusId error: " + e.getLocalizedMessage());
					// ignore
				}

			}
			criteriaOrStatus = criteriaOrStatus
					.orOperator(criteriaOrStatusList.toArray(new Criteria[criteriaOrStatusList.size()]));
			criterias.add(criteriaOrStatus);
		}

		// keyword search
		if (keyword != null && !keyword.isEmpty()) {
			keyword = keyword.trim();
			Criteria criteriaOrSearchField = new Criteria();
			List<Criteria> criteriaOrSearchList = new ArrayList<Criteria>();
			criteriaOrSearchList.add(Criteria.where("title").regex(keyword));
			criteriaOrSearchList.add(Criteria.where("publicYear").regex(keyword));
			criteriaOrSearchList.add(Criteria.where("publicMonth").regex(keyword));
			criteriaOrSearchField = criteriaOrSearchField
					.orOperator(criteriaOrSearchList.toArray(new Criteria[criteriaOrSearchList.size()]));
			criterias.add(criteriaOrSearchField);
		}
		// createdBy, ownerBy
		if (createdByEmail != null)
			criterias.add(Criteria.where("createdBy").is(createdByEmail));
		// ownerBy email
		criterias.add(Criteria.where("ownerBy").is(ownerByEmail));
		Query query = new Query();
		if (!criterias.isEmpty()) {
			criteriaFinal = criteriaFinal.andOperator(criterias.toArray(new Criteria[criterias.size()]));
			query.addCriteria(criteriaFinal);
		}
		// count
		long count = mongoTemplate.count(query, Book.class);
		int totalPages = HanleUltis.countTotalPages(Integer.parseInt("" + count), size);
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
			property = "publicYear";
			sortQuery = new Sort(Direction.DESC, property);
		}
		pageRequest = new PageRequest(page, size, sortQuery);
		query.with(pageRequest);

		// fields
		if (fields != null && !fields.isEmpty()) {
			String[] fieldArray = fields.split(",");
			for (String field : fieldArray) {
				switch (field.trim()) {
				case "categories":
					query.fields().include("categoryIds");
					break;
				case "authors":
					query.fields().include("authorIds");
					break;
				case "status":
					query.fields().include("statusId");
					break;
				default:
					query.fields().include(field.trim());
				}
			}
		}
		List<Book> books = mongoTemplate.find(query, Book.class);
		for (Book book : books) {
			if (fields == null || fields.isEmpty() || fields.contains("categories")) {
				book.setCategories(categoryService.findByCategoryIds(book.getCategoryIds()));
				book.setCategoryIds(null);
			}
			if (fields == null || fields.isEmpty() || fields.contains("authors")) {
				book.setAuthors(authorService.findByAuthorIds(book.getAuthorIds()));
				book.setAuthorIds(null);
			}
			if (fields == null || fields.isEmpty() || fields.contains("status")) {
				book.setStatus(statusService.findById(book.getStatusId()));
				book.setStatusId(null);
			}
		}
		return new Item(books, totalPages);
	}

	@Override
	public Book findById(String bookId, String email, String ownerEmail) {
		Query query = new Query();
		if (email != null)
			query.addCriteria(Criteria.where("createdBy").is(email));
		query.addCriteria(Criteria.where("ownerBy").is(ownerEmail));
		query.addCriteria(Criteria.where("id").is(bookId));
		Book book = mongoTemplate.findOne(query, Book.class);
		book.setCategories(categoryService.findByCategoryIds(book.getCategoryIds()));
		book.setAuthors(authorService.findByAuthorIds(book.getAuthorIds()));
		book.setStatus(statusService.findById(book.getStatusId()));
		return book;
	}

	@Override
	public boolean checkFileNameExist(String fileName, String ownerEmail) {
		Query query = new Query();
		query.addCriteria(Criteria.where("fileName").is(fileName));
		query.addCriteria(Criteria.where("ownerBy").is(ownerEmail));
		query.limit(1);
		List<Book> books = mongoTemplate.find(query, Book.class);
		if (books.size() == 1)
			return Boolean.TRUE;
		return Boolean.FALSE;
	}

}
