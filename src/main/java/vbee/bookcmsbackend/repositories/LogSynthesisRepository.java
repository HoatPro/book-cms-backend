package vbee.bookcmsbackend.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import vbee.bookcmsbackend.collections.LogSynthesis;

public interface LogSynthesisRepository extends MongoRepository<LogSynthesis, String>{

}
