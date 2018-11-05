package vbee.bookcmsbackend.services.books;

import java.util.List;

import vbee.bookcmsbackend.collections.Author;

public interface IAuthorService {

	List<Author> findByAuthorIds(List<String> authorIds);

	Author findById(String authorId);

}
