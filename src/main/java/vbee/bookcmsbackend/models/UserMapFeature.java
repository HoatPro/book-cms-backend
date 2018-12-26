package vbee.bookcmsbackend.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserMapFeature {

	public static Map<String, List<String>> mapUserFeature = new HashMap<>();
	
	public static void loadUserFeauture (String email, List<String> featureIds) {
		mapUserFeature.put(email, featureIds);
	}
	
	public static List<String> getUserFeature(String email) {
		if (mapUserFeature.containsKey(email)) {
			return mapUserFeature.get(email);
		}
		return null;
	}

	public static void removeUser(String email) {
		if(mapUserFeature.containsKey(email)) {
			mapUserFeature.remove(email);
		}
	}
}
