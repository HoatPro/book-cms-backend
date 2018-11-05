package vbee.bookcmsbackend.services.users_and_roles;

import java.util.List;

import vbee.bookcmsbackend.collections.Feature;

public interface IFeatureService {

	List<Feature> findByBackendKey(String apiKey);

}
