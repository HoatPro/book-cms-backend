package vbee.bookcmsbackend.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import vbee.bookcmsbackend.collections.Book;


public interface BookRepository extends MongoRepository<Book, String>{

	@Query(value = "{ fileName: ?0, ownerBy: ?1 }")
	Book checkExist(String fileName, String ownerEmail);

}
