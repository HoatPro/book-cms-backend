package vbee.bookcmsbackend.services.users_and_roles;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vbee.bookcmsbackend.authorization.IAuthorizationService;
import vbee.bookcmsbackend.collections.Feature;
import vbee.bookcmsbackend.config.APIConstant;
import vbee.bookcmsbackend.config.AppConstant;
import vbee.bookcmsbackend.daos.IFeatureDao;
import vbee.bookcmsbackend.repositories.FeatureRepository;

@Service
public class FeatureService implements IFeatureService {
	@Autowired
	IAuthorizationService authorizationService;

	@Autowired
	FeatureRepository featureRepository;

	@Autowired
	IFeatureDao featureDao;

	@Override
	public List<Feature> findByBackendKey(String apiKey) {
		return featureRepository.findByBackendKey(apiKey);
	}

	@Override
	public Feature findById(String featureId) {
		Optional<Feature> optional = featureRepository.findById(featureId);
		if (optional.isPresent())
			return optional.get();
		return null;
	}

	@Override
	public List<Feature> findAll(String email, String ownerBy) {
		if (ownerBy == null || ownerBy.isEmpty())
			return null;
		Integer permission = authorizationService.checkPermission(email, APIConstant.ROLE_USER_FEATURE_API);
		if (permission == AppConstant.PERMISSION_UNDEFINED)
			return null;
		else if (permission == AppConstant.PERMISSION_ALL_UNIT)
			email = null;
		return featureDao.findAll(email, ownerBy);
	}

	@Override
	public Object create(Feature newFeature) {
		if (newFeature.getDisplayName() == null || newFeature.getDisplayName().isEmpty()) {
			return " Tên Feature không được để trống!! ";
		}
		Feature featureExist = featureRepository.findByDisplayName(newFeature.getDisplayName());
		if (featureExist != null)
			return "Feature đã tồn tại";
		if (newFeature.getFrontendKey() == null || newFeature.getFrontendKey().isEmpty()) {
			return " Tên Key Feature không được để trống!! ";
		}
		if (newFeature.getBackendKey() == null || newFeature.getBackendKey().isEmpty()) {
			return " Tên Backend Feature không được để trống!! ";
		}
		newFeature.setCreatedAt(new Date());
		return featureRepository.save(newFeature);
	}

	@Override
	public Object update(String featureId, Feature feature) {
		Feature featureExist = featureDao.findById(featureId);
		if (featureExist == null)
			return null;
		return updateFeature(feature, featureExist);
	}

	private Object updateFeature(Feature feature, Feature featureExist) {
		if (feature.getDisplayName() != null && !feature.getDisplayName().isEmpty()
				&& !feature.getDisplayName().equals(featureExist.getDisplayName())) {
			Feature featureCheck = featureRepository.findByDisplayName(feature.getDisplayName());
			if (featureCheck != null)
				return "Tên Feature đã tồn tại. Vui lòng thử lại";
			featureExist.setDisplayName(feature.getDisplayName());
		}
		if (feature.getFrontendKey() != null && !feature.getFrontendKey().isEmpty()) {
			featureExist.setFrontendKey(feature.getFrontendKey());
		}
		if (feature.getBackendKey() != null && !feature.getBackendKey().isEmpty()) {
			featureExist.setBackendKey(feature.getBackendKey());
		}
		if (feature.isAll()) {
			featureExist.setAll(feature.isAll());
		}
		feature.setUpdatedAt(new Date());
		return featureRepository.save(featureExist);
	}

	@Override
	public Object delete(String featureId) {
		Feature featureExist = featureDao.findById(featureId);
		if (featureExist == null)
			return null;
		featureRepository.delete(featureExist);
		return Boolean.TRUE;
	}

}
