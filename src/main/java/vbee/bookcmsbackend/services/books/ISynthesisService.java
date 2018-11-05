package vbee.bookcmsbackend.services.books;

public interface ISynthesisService {

	Object synthesizeBook(String bookId, String email, String ownerBy, String voice, Integer bitRate);

}
