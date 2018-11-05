package vbee.bookcmsbackend.services.books;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vbee.bookcmsbackend.collections.Author;
import vbee.bookcmsbackend.repositories.AuthorRepository;

@Service
public class AuthorService implements IAuthorService {

	@Autowired
	AuthorRepository authorRepository;

	@Override
	public List<Author> findByAuthorIds(List<String> authorIds) {
		return authorRepository.findByAuthorIds(authorIds);
	}

	@Override
	public Author findById(String authorId) {
		Optional<Author> optional = authorRepository.findById(authorId);
		if (optional.isPresent())
			return optional.get();
		return null;
	}

}
