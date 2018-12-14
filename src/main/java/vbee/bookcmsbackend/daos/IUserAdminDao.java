package vbee.bookcmsbackend.daos;

import java.util.List;

import vbee.bookcmsbackend.collections.User;
import vbee.bookcmsbackend.collections.UserAdmin;
import vbee.bookcmsbackend.models.Item;

public interface IUserAdminDao {

	Item findAll(String keyword, Integer page, Integer size, String email);

	User findById(List<String> userId);

	UserAdmin findById(String userId);

}
