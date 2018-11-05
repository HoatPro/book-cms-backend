package vbee.bookcmsbackend.services.users_and_roles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vbee.bookcmsbackend.collections.Feature;
import vbee.bookcmsbackend.repositories.FeatureRepository;

@Service
public class FeatureService implements IFeatureService {

	@Autowired
	FeatureRepository featureRepository;

	@Override
	public List<Feature> findByBackendKey(String apiKey) {
		return featureRepository.findByBackendKey(apiKey);
	}

}
