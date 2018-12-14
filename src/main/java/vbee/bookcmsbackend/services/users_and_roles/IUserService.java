package vbee.bookcmsbackend.services.users_and_roles;

import java.util.List;

import vbee.bookcmsbackend.collections.User;
import vbee.bookcmsbackend.models.Item;

public interface IUserService {

	User findByEmail(String email);

	Object create(User newUser, String email, String ownerEmail);

	Object delete(String userId, String email, String ownerBy);

	Item findAll(String keyword, Integer page, Integer size, String email, String ownerBy);

	User findById(String userId, String email, String ownerBy);

	List<User> findByUserIds(List<String> userIds);

	void loadAllUserFeatures();

	Object update(String userId, User existUser, String email, String ownerBy);

}
