package vbee.bookcmsbackend.models;

import java.util.List;

import vbee.bookcmsbackend.collections.Book;
import vbee.bookcmsbackend.collections.Chapter;
import vbee.bookcmsbackend.config.ConfigProperties;
import vbee.bookcmsbackend.repositories.BookRepository;
import vbee.bookcmsbackend.repositories.ChapterRepository;

public class BookEntity {

	private Book book;
	private String voice;
	private Integer bitRate;
	private List<Chapter> chapters;
	private ConfigProperties configProperties;
	private ChapterRepository chapterRepository;
	private BookRepository bookRepository;

	public BookEntity() {
	}

	public BookEntity(Book book, List<Chapter> chapters, String voice, Integer bitRate) {
		this.book = book;
		this.voice = voice;
		this.bitRate = bitRate;
		this.setChapters(chapters);
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public String getVoice() {
		return voice;
	}

	public void setVoice(String voice) {
		this.voice = voice;
	}

	public Integer getBitRate() {
		return bitRate;
	}

	public void setBitRate(Integer bitRate) {
		this.bitRate = bitRate;
	}

	public List<Chapter> getChapters() {
		return chapters;
	}

	public void setChapters(List<Chapter> chapters) {
		this.chapters = chapters;
	}

	public ConfigProperties getConfigProperties() {
		return configProperties;
	}

	public void setConfigProperties(ConfigProperties configProperties) {
		this.configProperties = configProperties;
	}

	public ChapterRepository getChapterRepository() {
		return chapterRepository;
	}

	public void setChapterRepository(ChapterRepository chapterRepository) {
		this.chapterRepository = chapterRepository;
	}

	public BookRepository getBookRepository() {
		return bookRepository;
	}

	public void setBookRepository(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

}
