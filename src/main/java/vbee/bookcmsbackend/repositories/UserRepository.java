package vbee.bookcmsbackend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import vbee.bookcmsbackend.collections.Author;
import vbee.bookcmsbackend.collections.User;

public interface UserRepository extends MongoRepository<User, String>{


   @Query(value = "{ _id: { $in: ?0 } }")
	List<User> findByUserIds(List<String> UserId);
	
    Optional<User> findByEmail(String email);
	User findByEmailAndOwnerBy(String email, String ownerEmail);

	


}
