package vbee.bookcmsbackend.daos;

import java.util.List;

import vbee.bookcmsbackend.collections.Feature;

public interface IFeatureDao {

	List<Feature> findAll(String email, String ownerEmail);

	Feature findById(String featureId);

}
