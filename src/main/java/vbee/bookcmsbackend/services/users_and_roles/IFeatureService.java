package vbee.bookcmsbackend.services.users_and_roles;

import java.util.List;

import vbee.bookcmsbackend.collections.Feature;
import vbee.bookcmsbackend.collections.User;

public interface IFeatureService {

	List<Feature> findByBackendKey(String apiKey);

	List<Feature> findAll(String email, String ownerBy);

	Feature findById(String featureId);

	Object create(Feature newFeature);

	Object update(String featureId, Feature existFeature);

	Object delete(String featureId);

	List<Feature> checkPermission(User user, String frontendKey);

}
