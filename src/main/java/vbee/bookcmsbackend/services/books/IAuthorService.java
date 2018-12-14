package vbee.bookcmsbackend.services.books;

import java.util.List;
import vbee.bookcmsbackend.collections.Author;
import vbee.bookcmsbackend.models.Item;

import vbee.bookcmsbackend.collections.Author;

public interface IAuthorService {

	List<Author> findByAuthorIds(List<String> authorIds);

	Author findById(String authorId);

	Item findAll(String keyword, Integer page, Integer size, String fields, String sort, String email, String ownerBy);

	Author findById(String authorId, String email, String ownerBy);

	Object create(Author newAuthor, String email, String ownerBy);

	Object delete(String authorId, String email, String ownerBy);

	Object update(String authorId, Author author, String email, String ownerBy);

}
