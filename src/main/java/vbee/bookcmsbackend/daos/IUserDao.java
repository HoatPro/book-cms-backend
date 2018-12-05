package vbee.bookcmsbackend.daos;

import vbee.bookcmsbackend.collections.User;
import vbee.bookcmsbackend.models.Item;

public interface IUserDao {

	Item findAll(String keyword, Integer page, Integer size, String email, String ownerEmail);

	User findById(String userId);

}
