package vbee.bookcmsbackend.daos;

import vbee.bookcmsbackend.collections.Category;
import vbee.bookcmsbackend.models.Item;

public interface ICategoryDao {

	Item findAll(String keyword, Integer page, Integer size, String fields, String sort, String email,
			String ownerEmail);

	Category findById(String categoryId, String email, String ownerEmail);

}
