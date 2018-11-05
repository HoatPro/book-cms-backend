package vbee.bookcmsbackend.services.users_and_roles;

import vbee.bookcmsbackend.collections.User;

public interface IUserService {

	User findByEmail(String email);

	User create(User user);

	void loadAllUserFeatures();

}
