package vbee.bookcmsbackend.services.users_and_roles;

import java.util.List;

import vbee.bookcmsbackend.collections.User;
import vbee.bookcmsbackend.collections.UserAdmin;
import vbee.bookcmsbackend.models.Item;

public interface IUserAdminService {

	Item findAll(String keyword, Integer page, Integer size, String email);

	List<User> findByUserIds(List<String> userId);

	UserAdmin findByEmail(String email);

	UserAdmin findById(String userId);

	Object create(UserAdmin newUserAdmin);

}
