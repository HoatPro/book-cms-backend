package vbee.bookcmsbackend.services.books;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vbee.bookcmsbackend.authorization.IAuthorizationService;
import vbee.bookcmsbackend.collections.Book;
import vbee.bookcmsbackend.collections.Chapter;
import vbee.bookcmsbackend.config.APIConstant;
import vbee.bookcmsbackend.config.AppConstant;
import vbee.bookcmsbackend.config.ConfigProperties;
import vbee.bookcmsbackend.models.BookEntity;
import vbee.bookcmsbackend.repositories.BookRepository;
import vbee.bookcmsbackend.repositories.ChapterRepository;
import vbee.bookcmsbackend.thread.NormalizationBookQueue;

@Service
public class NormalizationService implements INormaliztionService {

	@Autowired
	IBookService bookService;

	@Autowired
	IAuthorizationService authorizationService;

	@Autowired
	IChapterService chapterService;

	@Autowired
	BookRepository bookRepository;

	@Autowired
	ChapterRepository chapterRepository;

	@Autowired
	ConfigProperties configProperties;

	@Override
	public Object normalizationBook(String bookId, String email, String ownerEmail) {
		if (ownerEmail == null || ownerEmail.isEmpty())
			return null;
		Integer permission = authorizationService.checkPermission(email, APIConstant.NORMALIZATION_BOOK_FEATURE_API);
		if (permission == AppConstant.PERMISSION_UNDEFINED)
			return null;
		Book book = bookService.findById(bookId);
		if (book == null)
			return null;
		List<Chapter> chapters = chapterService.findByBookId(bookId);
		for (Chapter chapter : chapters) {
			chapter.setStatusId(AppConstant.NORMALIZING);
			chapterRepository.save(chapter);
		}
		book.setStatusId(AppConstant.NORMALIZING);
		bookRepository.save(book);
		BookEntity bookEntity = new BookEntity();
		bookEntity.setBook(book);
		bookEntity.setChapters(chapters);
		bookEntity.setConfigProperties(configProperties);
		bookEntity.setBookRepository(bookRepository);
		bookEntity.setChapterRepository(chapterRepository);
		NormalizationBookQueue.push(bookEntity);
		NormalizationBookQueue.run();
		return Boolean.TRUE;
	}

}
