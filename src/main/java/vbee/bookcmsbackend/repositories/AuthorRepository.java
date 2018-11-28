package vbee.bookcmsbackend.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import vbee.bookcmsbackend.collections.Author;


public interface AuthorRepository extends MongoRepository<Author, String> {

	@Query(value = " { _id: { $in: ?0 } } ", fields = "{ 'name': 1, 'slug': 1 }")
	List<Author> findByAuthorIds(List<String> authorIds);

	Author findByNameAndOwnerBy(String name, String ownerEmail);
}
