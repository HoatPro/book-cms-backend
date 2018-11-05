package vbee.bookcmsbackend.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import vbee.bookcmsbackend.collections.Category;

public interface CategoryRepository extends MongoRepository<Category, String> {

	@Query(value = "{ _id: { $in: ?0 } }", fields = "{ 'name': 1, 'slug': 1 }")
	List<Category> findByCategoryIds(List<String> categoryIds);

	Category findByNameAndOwnerBy(String name, String ownerEmail);

}
