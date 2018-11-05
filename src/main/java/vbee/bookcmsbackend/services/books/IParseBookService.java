package vbee.bookcmsbackend.services.books;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import vbee.bookcmsbackend.collections.Book;
import vbee.bookcmsbackend.collections.Chapter;
import vbee.bookcmsbackend.models.ParagraphStyle;

public interface IParseBookService {

	List<Book> parseExcelFile(FileInputStream excelFile);

	List<ParagraphStyle> readDocx(FileInputStream input) throws IOException;

	List<Chapter> getBookContent(List<ParagraphStyle> listParaStyle, String bookId, String email, String ownerEmail, Boolean isCreate);

}
