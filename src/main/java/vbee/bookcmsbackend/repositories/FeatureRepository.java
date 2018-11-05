package vbee.bookcmsbackend.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import vbee.bookcmsbackend.collections.Feature;

public interface FeatureRepository extends MongoRepository<Feature, String>{

	List<Feature> findByBackendKey(String apiKey);

}
