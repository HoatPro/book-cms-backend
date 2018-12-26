package vbee.bookcmsbackend.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import vbee.bookcmsbackend.collections.Feature;

public interface FeatureRepository extends MongoRepository<Feature, String> {

	List<Feature> findByBackendKey(String apiKey);

	Feature findByDisplayName(String displayName);
	
	@Query(value = " { _id: { $in: ?0 } }")
	List<Feature> findByFeatureIds(List<String> featureIds);

}
