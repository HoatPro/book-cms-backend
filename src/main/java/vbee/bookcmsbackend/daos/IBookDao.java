package vbee.bookcmsbackend.daos;

import vbee.bookcmsbackend.collections.Book;
import vbee.bookcmsbackend.models.Item;

public interface IBookDao {

	Item findAllBooks(String categoryId, String categoryIds, String keyword, Integer page, Integer size, String fields,
			String sort, String createdByEmail, String ownerByEmail);

	Book findById(String bookId, String email, String ownerEmail);

	boolean checkFileNameExist(String fileName, String ownerEmail);

}
