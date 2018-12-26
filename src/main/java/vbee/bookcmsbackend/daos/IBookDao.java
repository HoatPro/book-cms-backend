package vbee.bookcmsbackend.daos;

import vbee.bookcmsbackend.collections.Book;
import vbee.bookcmsbackend.models.Item;

public interface IBookDao {



	Book findById(String bookId, String email, String ownerEmail);

	boolean checkFileNameExist(String fileName, String ownerEmail);

	Item findAllBooks(String categoryId, String authorId, String statusIds, String keyword, Integer page, Integer size,
			String fields, String sort, String createdByEmail, String ownerByEmail);

}
