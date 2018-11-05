package vbee.bookcmsbackend.services.books;

import java.util.List;

import vbee.bookcmsbackend.collections.Chapter;

public interface IChapterService {

	List<Chapter> findByBookId(String bookId, String email, String ownerBy);

	Object createByBookId(String bookId, String email, String ownerBy, Chapter chapter);

	Object updateByBookId(String bookId, String email, String ownerBy, Chapter chapter);

	Object deleteByBookId(String bookId, String email, String ownerBy, String chapterId);

	Chapter findById(String chapterId);

	Object deleteChaptersByBookId(String bookId, String email, String ownerBy, List<String> chapterIds);

	void createChapters(List<Chapter> chapters);

	Chapter createChapter(Chapter chapter, String email, String ownerEmail, String bookId);

	List<Chapter> findByBookId(String bookId);

}
