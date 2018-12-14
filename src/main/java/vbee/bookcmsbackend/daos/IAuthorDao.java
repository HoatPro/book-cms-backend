package vbee.bookcmsbackend.daos;

import vbee.bookcmsbackend.collections.Author;
import vbee.bookcmsbackend.models.Item;

public interface IAuthorDao {
	Item findAll(String keyword, Integer page, Integer size, String fields, String sort, String email,
			String ownerEmail);

	Author findById(String authorId, String email, String ownerEmail);

}
