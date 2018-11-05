package vbee.bookcmsbackend.services.books;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import vbee.bookcmsbackend.authorization.IAuthorizationService;
import vbee.bookcmsbackend.collections.Author;
import vbee.bookcmsbackend.collections.Book;
import vbee.bookcmsbackend.collections.Category;
import vbee.bookcmsbackend.collections.Chapter;
import vbee.bookcmsbackend.collections.ChapterVoice;
import vbee.bookcmsbackend.collections.Voice;
import vbee.bookcmsbackend.config.APIConstant;
import vbee.bookcmsbackend.config.AppConstant;
import vbee.bookcmsbackend.daos.IBookDao;
import vbee.bookcmsbackend.models.Item;
import vbee.bookcmsbackend.models.MSWord;
import vbee.bookcmsbackend.models.ParagraphStyle;
import vbee.bookcmsbackend.repositories.BookRepository;
import vbee.bookcmsbackend.services.common.IVoiceService;
import vbee.bookcmsbackend.utils.SlugUtils;

@Service
public class BookService implements IBookService {

	private static final Logger logger = LoggerFactory.getLogger(BookService.class);

	@Autowired
	IBookDao bookDao;

	@Autowired
	IAuthorizationService authorizationService;

	@Autowired
	BookRepository bookRepository;

	@Autowired
	IParseBookService parseBookService;

	@Autowired
	IChapterService chapterService;

	@Autowired
	ICategoryService categoryService;

	@Autowired
	IAuthorService authorService;

	@Autowired
	IVoiceService voiceService;

	@Autowired
	IChapterVoiceService chapterVoiceService;

	@Override
	public Item findAll(String categoryId, String statusIds, String keyword, Integer page, Integer size, String fields,
			String sort, String email, String ownerEmail) {
		if (ownerEmail == null || ownerEmail.isEmpty())
			return null;
		Integer permission = authorizationService.checkPermission(email, APIConstant.FIND_BOOK_FEATURE_API);
		if (permission == AppConstant.PERMISSION_UNDEFINED)
			return null;
		else if (permission == AppConstant.PERMISSION_ALL_UNIT)
			email = null;
		return bookDao.findAllBooks(categoryId, statusIds, keyword, page, size, fields, sort, email, ownerEmail);
	}

	@Override
	public Book findById(String bookId) {
		Optional<Book> optional = bookRepository.findById(bookId);
		if (optional.isPresent())
			return optional.get();
		return null;
	}

	@Override
	public Book findById(String bookId, String email, String ownerEmail) {
		if (ownerEmail == null || ownerEmail.isEmpty())
			return null;
		Integer permission = authorizationService.checkPermission(email, APIConstant.FIND_BOOK_FEATURE_API);
		if (permission == AppConstant.PERMISSION_UNDEFINED)
			return null;
		else if (permission == AppConstant.PERMISSION_ALL_UNIT) {
			email = null;
		}

		Book book = bookDao.findById(bookId, email, ownerEmail);
		book.setCategoryIds(null);
		book.setAuthorIds(null);
		book.setStatusId(null);
		return book;
	}

	@Override
	public Object create(Book newBook, String email, String ownerEmail) {
		if (ownerEmail == null || ownerEmail.isEmpty())
			return null;
		Integer permission = authorizationService.checkPermission(email, APIConstant.CREATE_BOOK_FEATURE_API);
		if (permission == AppConstant.PERMISSION_UNDEFINED)
			return null;
		if (newBook.getChapters() == null && newBook.getChapters().isEmpty()) {
			return "Sách chưa có nội dung. Vui lòng thêm nội dung và thử lại.";
		}
		if (newBook.getFileName() != null && !newBook.getFileName().isEmpty()) {
			Book bookExist = bookRepository.checkExist(newBook.getFileName(), ownerEmail);
			if (bookExist != null)
				return "fileName của sách đã tồn tại. Vui lòng đổi fileName khác";
		}
		newBook.setStatusId(AppConstant.HAS_CONTENT);
		return createBook(newBook, email, ownerEmail);
	}

	private Book createBook(Book newBook, String email, String ownerEmail) {
		// set categories, authors
		List<String> categoryIds = new ArrayList<>();
		if (newBook.getCategories() != null && !newBook.getCategories().isEmpty()) {
			for (Category category : newBook.getCategories()) {
				Category categoryExist = categoryService.findById(category.getId());
				if (categoryExist != null) {
					categoryIds.add(categoryExist.getId());
				}
			}
		}
		newBook.setCategoryIds(categoryIds);
		List<String> authorIds = new ArrayList<>();
		if (newBook.getAuthors() != null && !newBook.getAuthors().isEmpty()) {
			for (Author author : newBook.getAuthors()) {
				Author authorExist = authorService.findById(author.getId());
				if (authorExist != null) {
					authorIds.add(authorExist.getId());
				}
			}
		}
		newBook.setAuthorIds(authorIds);
		// set slug time, user
		newBook.setCreatedAt(new Date());
		newBook.setCreatedBy(email);
		newBook.setOwnerBy(ownerEmail);
		newBook = bookRepository.save(newBook);
		String slug = SlugUtils.makeSlugByNameAndObjectId(newBook.getTitle(), newBook.getId());
		newBook.setSlug(slug);
		newBook = bookRepository.save(newBook);
		// insert chapters
		if (newBook.getChapters() != null && !newBook.getChapters().isEmpty()) {
			for (Chapter chapter : newBook.getChapters()) {
				if (chapter.getOrderNo() == null)
					continue;
				chapterService.createChapter(chapter, email, ownerEmail, newBook.getId());
			}
		}
		return newBook;
	}

	@Override
	public Object update(String bookId, Book book, String email, String ownerEmail) {
		if (ownerEmail == null || ownerEmail.isEmpty())
			return null;
		String tempEmail = email;
		Integer permission = authorizationService.checkPermission(email, APIConstant.UPDATE_BOOK_FEATURE_API);
		if (permission == AppConstant.PERMISSION_UNDEFINED)
			return null;
		else if (permission == AppConstant.PERMISSION_ALL_UNIT) {
			tempEmail = null;
		}
		Book bookExist = bookDao.findById(bookId, tempEmail, ownerEmail);
		if (bookExist == null)
			return null;
		return updateBook(book, bookExist, email);
	}

	private Book updateBook(Book book, Book bookExist, String email) {
		// set field
		if (book.getAuthors() != null) {
			List<String> authorIds = new ArrayList<>();
			for (Author author : book.getAuthors()) {
				Author authorExist = authorService.findById(author.getId());
				if (authorExist != null) {
					authorIds.add(authorExist.getId());
				}
			}
			bookExist.setAuthorIds(authorIds);
		}

		if (book.getCategories() != null) {
			List<String> categoryIds = new ArrayList<>();
			for (Category category : book.getCategories()) {
				Category categoryExist = categoryService.findById(category.getId());
				if (categoryExist != null) {
					categoryIds.add(categoryExist.getId());
				}
			}
			bookExist.setCategoryIds(categoryIds);
		}

		if (book.getImage() != null && !book.getImage().isEmpty())
			bookExist.setImage(book.getImage());
		if (book.getDescription() != null && !book.getDescription().isEmpty())
			bookExist.setDescription(book.getDescription());
		if (book.getPageNumber() != null)
			bookExist.setPageNumber(book.getPageNumber());
		if (book.getPublicMonth() != null)
			bookExist.setPublicMonth(book.getPublicMonth());
		if (book.getPublicYear() != null)
			bookExist.setPublicYear(book.getPublicYear());
		if (book.getPublishingCompany() != null && !book.getPublishingCompany().isEmpty())
			bookExist.setPublishingCompany(book.getPublishingCompany());
		if (book.getTitle() != null && !book.getTitle().isEmpty())
			bookExist.setTitle(book.getTitle());
		if (book.getTranslator() != null && !book.getTranslator().isEmpty())
			bookExist.setTranslator(book.getTranslator());
		// set user, time update
		bookExist.setUpdatedAt(new Date());
		bookExist.setUpdatedBy(email);
		return bookRepository.save(bookExist);
	}

	@Override
	public Object delete(String bookId, String email, String ownerEmail) {
		if (ownerEmail == null || ownerEmail.isEmpty())
			return null;
		Integer permission = authorizationService.checkPermission(email, APIConstant.DELETE_BOOK_FEATURE_API);
		if (permission == AppConstant.PERMISSION_UNDEFINED)
			return null;
		else if (permission == AppConstant.PERMISSION_ALL_UNIT) {
			email = null;
		}
		Book bookExist = bookDao.findById(bookId, email, ownerEmail);
		if (bookExist == null)
			return null;
		bookRepository.delete(bookExist);
		return Boolean.TRUE;
	}

	@Override
	public String saveInfoBooks(List<MultipartFile> files, String email, String ownerEmail) {
		if (ownerEmail == null || ownerEmail.isEmpty())
			return null;
		Integer permission = authorizationService.checkPermission(email, APIConstant.CREATE_BOOK_FEATURE_API);
		if (permission == AppConstant.PERMISSION_UNDEFINED)
			return null;
		FileInputStream excelFile = null;
		try {
			for (MultipartFile file : files) {
				excelFile = (FileInputStream) file.getInputStream();
			}
			if (excelFile == null)
				throw new Exception("excel file NULL");
		} catch (Exception e) {
			logger.info("Exception convert input stream : " + e.getMessage());
			return "Lỗi khi convert file";
		}

		// insert book info
		try {
			int countBookAdded = 0;
			List<Book> books = parseBookService.parseExcelFile(excelFile);
			for (Book newBook : books) {
				if (!checkFileNameExist(newBook.getFileName(), ownerEmail)) {
					countBookAdded++;
					newBook.setStatusId(AppConstant.NO_CONTENT);
					createBook(newBook, email, ownerEmail);
				}
			}
			logger.info("Số sách đã được thêm: " + countBookAdded);
			return "Số sách đã được thêm: " + countBookAdded;
		} catch (Exception e) {
			logger.info("Exception when parse file Excel: " + e.getMessage());
			return "Xảy ra lỗi trong quá trình phân tích file xlsx";
		}

	}

	@Override
	public String saveMultipleBookContent(List<MultipartFile> files, String email, String ownerEmail) {
		if (ownerEmail == null || ownerEmail.isEmpty())
			return null;
		Integer permission = authorizationService.checkPermission(email, APIConstant.CREATE_BOOK_FEATURE_API);
		if (permission == AppConstant.PERMISSION_UNDEFINED)
			return null;
		List<MSWord> msWords = new ArrayList<>();
		try {
			for (MultipartFile file : files) {
				FileInputStream msWordFile = (FileInputStream) file.getInputStream();
				String fileName = file.getOriginalFilename();
				msWords.add(new MSWord(fileName, msWordFile));
			}
		} catch (Exception e) {
			logger.info("Exception convert input stream : " + e.getMessage());
			return "Lỗi khi convert file";
		}
		logger.info("size: " + msWords.size());
		int count = 0;
		for (MSWord msWord : msWords) {
			try {
				Book bookExists = bookRepository.checkExist(msWord.getFileName(), ownerEmail);
				if (bookExists == null || msWord == null || msWord.getFile() == null || msWord.getFileName() == null)
					continue;
				// only get book have not content
				if (bookExists.getStatusId() != AppConstant.NO_CONTENT)
					continue;
				// parse book content
				List<ParagraphStyle> listParaGraph = parseBookService.readDocx(msWord.getFile());
				if (listParaGraph.isEmpty())
					continue;
				List<Chapter> chapters = parseBookService.getBookContent(listParaGraph, bookExists.getId(), email,
						ownerEmail, true);
				if (chapters.isEmpty())
					continue;
				bookExists.setStatusId(AppConstant.HAS_CONTENT);
				bookRepository.save(bookExists);
				chapterService.createChapters(chapters);
				count++;
			} catch (Exception e) {
				// ignore error book
			}
		}
		return "Số sách được thêm nội dung: " + count + " sách.";
	}

	public boolean checkFileNameExist(String fileName, String ownerEmail) {
		return bookDao.checkFileNameExist(fileName, ownerEmail);
	}

	@Override
	public JSONObject saveBookContent(List<MultipartFile> files, String email, String ownerEmail) {
		if (ownerEmail == null || ownerEmail.isEmpty())
			return null;
		Integer permission = authorizationService.checkPermission(email, APIConstant.CREATE_BOOK_FEATURE_API);
		if (permission == AppConstant.PERMISSION_UNDEFINED)
			return null;
		MSWord msWord = null;
		JSONObject response = new JSONObject();
		try {
			for (MultipartFile file : files) {
				FileInputStream msWordFile = (FileInputStream) file.getInputStream();
				String fileName = file.getOriginalFilename();
				msWord = new MSWord(fileName, msWordFile);
			}
		} catch (Exception e) {
			logger.info("Exception convert input stream : " + e.getMessage());
			response.put("error", 1);
			response.put("message", "Lỗi khi lấy file");
			return response;
		}
		try {
			// parse book content
			List<ParagraphStyle> listParaGraph = parseBookService.readDocx(msWord.getFile());
			if (listParaGraph.isEmpty()) {
				response.put("error", 1);
				response.put("message", "Lỗi khi convert file sang đoạn văn");
				return response;
			}
			List<Chapter> chapters = parseBookService.getBookContent(listParaGraph, null, null, null, false);
			if (chapters.isEmpty()) {
				response.put("error", 1);
				response.put("message", "Lỗi khi convert file sang chapters");
				return response;
			}
			if (chapters.size() == 1 && chapters.get(0).getTitle() == null) {
				response.put("content", chapters.get(0).getContent());
				return response;
			}
			response.put("chapters", chapters);
			return response;
		} catch (Exception e) {
			logger.info("error processing book: " + msWord.getFileName());
		}
		response.put("error", 1);
		response.put("message", "Lỗi khi convert file");
		return response;
	}

	@Override
	public String calllbackSynthesis(String bookId, String voiceValue, long startBookTime, Integer bitRate,
			String audioLink, String resultType, String chapterId, String synthesisId) {
		Book book = findById(bookId);
		if (book == null)
			return "book-id không tồn tại.";
		Chapter chapter = chapterService.findById(chapterId);
		if (chapter == null)
			return "chapter-id không tồn tại";
		Voice voice = voiceService.findByValue(voiceValue);
		if (voice == null)
			return "Không hỗ trợ voice: " + voiceValue;
		ChapterVoice chapterVoice = chapterVoiceService.findByVoiceIdAndChapterId(voiceValue, chapterId);
		if (resultType.equals("success")) {
			if (chapterVoice == null) {
				chapterVoice = new ChapterVoice(chapterId, voiceValue, bookId, audioLink);
				chapterVoice.setCreatedAt(new Date());
			} else {
				chapterVoice.setLink(audioLink);
				chapterVoice.setUpdatedAt(new Date());
			}
			chapterVoice.setSynthesisId(synthesisId);
			chapterVoice.setIsSuccess(Boolean.TRUE);
			chapterVoiceService.save(chapterVoice);
			checkBookSynthesized(book, voiceValue, synthesisId);
			return "Lưu thành công voice: " + voiceValue + " --- chapter: " + chapterId + " tổng hợp thành công";
		} else if (resultType.equals("error")) {
			if (chapterVoice == null) {
				chapterVoice = new ChapterVoice(chapterId, voiceValue, bookId, null);
				chapterVoice.setCreatedAt(new Date());
			} else {
				chapterVoice.setUpdatedAt(new Date());
			}
			chapterVoice.setIsSuccess(Boolean.FALSE);
			chapterVoice.setSynthesisId(synthesisId);
			chapterVoiceService.save(chapterVoice);
			checkBookSynthesized(book, voiceValue, synthesisId);
			return "Lưu thành công voice: " + voiceValue + " --- chapter: " + chapterId + " tổng hợp thất bại";
		}
		return "result-type không chính xác.";
	}

	private void checkBookSynthesized(Book book, String voiceValue, String synthesisId) {
		Integer totalChapter = chapterService.findByBookId(book.getId()).size();
		Integer totalChapterVoices = chapterVoiceService.findByBookIdAndVoiceId(book.getId(), voiceValue, synthesisId)
				.size();
		Integer successChapterVoices = chapterVoiceService
				.findByBookIdAndVoiceIdAndSuccess(book.getId(), voiceValue, synthesisId, Boolean.TRUE).size();
		// synthesize done
		if (totalChapterVoices == totalChapter) {
			// synthesize success
			if (successChapterVoices == totalChapter) {
				book.setStatusId(AppConstant.SYNTHESIZED_SUCCESS);
				logger.info("bookId: " + book.getId() + " --- voice: " + voiceValue + " đã tổng hợp thành công");
			} else { // synthesize error
				book.setStatusId(AppConstant.SYNTHESIZED_ERROR);
				logger.info("bookId: " + book.getId() + " --- voice: " + voiceValue
						+ " đã tổng hợp thất bại. Số chương tổng hợp thành công: " + successChapterVoices + "/"
						+ totalChapter);
			}
			bookRepository.save(book);
		}
	}

}
