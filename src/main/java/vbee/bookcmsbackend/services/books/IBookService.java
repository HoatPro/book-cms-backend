package vbee.bookcmsbackend.services.books;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import vbee.bookcmsbackend.collections.Book;
import vbee.bookcmsbackend.models.Item;

public interface IBookService {

	Item findAll(String categoryId, String statusIds, String keyword, Integer page, Integer size, String fields,
			String sort, String email, String ownerEmail);

	Book findById(String bookId);

	Book findById(String bookId, String email, String ownerBy);

	Object create(Book newBook, String email, String ownerBy);

	Object update(String bookId, Book existBook, String email, String ownerBy);

	Object delete(String bookId, String email, String ownerBy);

	String saveInfoBooks(List<MultipartFile> asList, String email, String ownerBy);

	String saveMultipleBookContent(List<MultipartFile> files, String email, String ownerEmail);

	JSONObject saveBookContent(List<MultipartFile> asList, String email, String ownerBy);

	String calllbackSynthesis(String bookId, String voiceValue, long startBookTime, Integer bitRate, String audioLink,
			String resultType, String chapterId, String synthesisId);

}
