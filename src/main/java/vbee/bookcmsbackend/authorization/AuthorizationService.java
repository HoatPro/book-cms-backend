package vbee.bookcmsbackend.authorization;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vbee.bookcmsbackend.collections.Feature;
import vbee.bookcmsbackend.config.AppConstant;
import vbee.bookcmsbackend.models.UserMapFeature;
import vbee.bookcmsbackend.services.users_and_roles.IFeatureService;

@Service
public class AuthorizationService implements IAuthorizationService {

	@Autowired
	IFeatureService featureService;

	@Override
	public Integer checkPermission(String email, String apiKey) {
		List<Feature> apiFeatures = featureService.findByBackendKey(apiKey);
		List<String> userFeatures = UserMapFeature.getUserFeature(email);
		int count = 0;
		// case owner
		if (userFeatures.isEmpty()) {
			return AppConstant.PERMISSION_ALL_UNIT;
		}
		// case normal role
		for (Feature feature : apiFeatures) {
			if (userFeatures.contains(feature.getId())) {
				count++;
				if (feature.isAll()) {
					return AppConstant.PERMISSION_ALL_UNIT;
				}
			}
		}
		return count == 0 ? AppConstant.PERMISSION_UNDEFINED : AppConstant.PERMISSION_ITSEFT_UNIT;
	}

}
