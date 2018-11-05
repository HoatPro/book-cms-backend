package vbee.bookcmsbackend.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import vbee.bookcmsbackend.collections.Voice;

public interface VoiceRepository extends MongoRepository<Voice, String>{

}
