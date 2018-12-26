package vbee.bookcmsbackend.services.books;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vbee.bookcmsbackend.authorization.IAuthorizationService;
import vbee.bookcmsbackend.collections.Book;
import vbee.bookcmsbackend.collections.Chapter;
import vbee.bookcmsbackend.collections.ChapterVoice;
import vbee.bookcmsbackend.collections.Voice;
import vbee.bookcmsbackend.config.APIConstant;
import vbee.bookcmsbackend.config.AppConstant;
import vbee.bookcmsbackend.config.ConfigProperties;
import vbee.bookcmsbackend.models.BookEntity;
import vbee.bookcmsbackend.repositories.BookRepository;
import vbee.bookcmsbackend.repositories.ChapterRepository;
import vbee.bookcmsbackend.services.common.IVoiceService;
import vbee.bookcmsbackend.services.common.VoiceService;
import vbee.bookcmsbackend.thread.SynthesisBookQueue;

@Service
public class SynthesisService implements ISynthesisService {

	private static final Logger logger = LoggerFactory.getLogger(SynthesisService.class);

	@Autowired
	IBookService bookService;

	@Autowired
	IAuthorizationService authorizationService;

	@Autowired
	IChapterService chapterService;

	@Autowired
	IVoiceService voiceService;

	@Autowired
	BookRepository bookRepository;

	@Autowired
	ChapterRepository chapterRepository;

	@Autowired
	ConfigProperties configProperties;

	@Autowired
	IChapterVoiceService chapterVoiceService;

	@Override
	public Object synthesizeBook(String bookId, String email, String ownerEmail, String voice, Integer bitRate) {
		if (ownerEmail == null || ownerEmail.isEmpty())
			return null;
		Integer permission = authorizationService.checkPermission(email, APIConstant.SYNTHESIS_BOOK_FEATURE_API);
		if (permission == AppConstant.PERMISSION_UNDEFINED)
			return null;
		Book book = bookService.findById(bookId);
		if (book == null)
			return null;
		List<Chapter> chapters = chapterService.findByBookId(bookId);
		for (Chapter chapter : chapters) {
			chapter.setStatusId(AppConstant.SYNTHESIZING);
			chapterRepository.save(chapter);
		}
		book.setStatusId(AppConstant.SYNTHESIZING);
		bookRepository.save(book);
		BookEntity bookEntity = new BookEntity(book, chapters, voice, bitRate);
		bookEntity.setConfigProperties(configProperties);
		bookEntity.setBookRepository(bookRepository);
		bookEntity.setChapterRepository(chapterRepository);
		SynthesisBookQueue.push(bookEntity);
		SynthesisBookQueue.run();
		return Boolean.TRUE;
	}

	@Override
	public Object synthesizeStatusBook(String bookId, String email, String ownerBy) {
		Book book = bookService.findById(bookId);
		if (book == null)
			return null;
		JSONObject result = new JSONObject();
		JSONArray voiceArray = new JSONArray();
		List<Voice> voices = voiceService.findAll();
		List<Chapter> chapters = chapterService.findByBookId(bookId);
		result.put("totalChapters", chapters.size());
		for (Voice voice : voices) {
			JSONObject voiceJSON = new JSONObject();
			JSONArray chapterVoices = new JSONArray();
			for (Chapter chapter : chapters) {
				ChapterVoice chapterVoice = chapterVoiceService.findByVoiceIdAndChapterIdLatest(voice.getValue(),
						chapter.getId());
				if (chapterVoice != null)
					chapterVoices.add(chapterVoice);
			}
			voiceJSON.put("voice", voice);
			voiceJSON.put("chapterVoices", chapterVoices);
			voiceJSON.put("totalChapterSynthesized", chapterVoices.size());
			voiceArray.add(voiceJSON);
		}
		result.put("voices", voiceArray);
		return result;
	}

}
