package vbee.bookcmsbackend.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import vbee.bookcmsbackend.collections.User;

public interface UserRepository extends MongoRepository<User, String>{

}
