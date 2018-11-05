package vbee.bookcmsbackend.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import vbee.bookcmsbackend.collections.Page;

public interface PageRepository extends MongoRepository<Page, String>{

}
