package vbee.bookcmsbackend.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import vbee.bookcmsbackend.collections.Status;

public interface StatusRepository extends MongoRepository<Status, Integer>{

}
