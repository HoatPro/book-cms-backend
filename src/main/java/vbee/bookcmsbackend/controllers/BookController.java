package vbee.bookcmsbackend.controllers;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import vbee.bookcmsbackend.authen.sso.IAuthenSSOService;
import vbee.bookcmsbackend.collections.Book;
import vbee.bookcmsbackend.collections.Chapter;
import vbee.bookcmsbackend.collections.User;
import vbee.bookcmsbackend.models.Item;
import vbee.bookcmsbackend.models.ResponseMessage;
import vbee.bookcmsbackend.services.books.IBookService;
import vbee.bookcmsbackend.services.books.IChapterService;
import vbee.bookcmsbackend.services.books.INormaliztionService;
import vbee.bookcmsbackend.services.books.ISynthesisService;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

	private static final Logger logger = LoggerFactory.getLogger(BookController.class);

	@Autowired
	IAuthenSSOService authenSSOService;

	@Autowired
	IBookService bookService;

	@Autowired
	IChapterService chapterService;

	@Autowired
	ISynthesisService synthesisService;

	@Autowired
	INormaliztionService normalizationService;

	// findAll
	@GetMapping()
	public ResponseEntity<ResponseMessage> getBooks(HttpServletRequest request, Integer page, Integer size,
			String fields, String sort, String statusIds, String categoryId, String keyword) {
		User user = authenSSOService.verify(request);
		if (user == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.UNAUTHORIZED);
		ResponseMessage resMessage = new ResponseMessage();
		Item item = bookService.findAll(categoryId, statusIds, keyword, page, size, fields, sort, user.getEmail(),
				user.getOwnerBy());
		if (item == null) {
			return new ResponseEntity<ResponseMessage>(HttpStatus.FORBIDDEN);
		}
		resMessage.setStatus(1);
		resMessage.setResults(item);
		return ResponseEntity.ok(resMessage);
	}

	// get book detail
	@GetMapping("/{bookId}")
	public ResponseEntity<ResponseMessage> getBook(HttpServletRequest request, @PathVariable String bookId) {
		User user = authenSSOService.verify(request);
		if (user == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.UNAUTHORIZED);
		Book book = bookService.findById(bookId, user.getEmail(), user.getOwnerBy());
		if (book == null) {
			return new ResponseEntity<ResponseMessage>(HttpStatus.FORBIDDEN);
		}
		ResponseMessage resMessage = new ResponseMessage();
		resMessage.setResults(book);
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}

	// creat book
	@PostMapping()
	public ResponseEntity<ResponseMessage> createBook(HttpServletRequest request, @RequestBody Book newBook) {
		User user = authenSSOService.verify(request);
		if (user == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.UNAUTHORIZED);
		Object response = bookService.create(newBook, user.getEmail(), user.getOwnerBy());
		ResponseMessage resMessage = new ResponseMessage();
		if (response == null) {
			return new ResponseEntity<ResponseMessage>(HttpStatus.FORBIDDEN);
		} else if (response instanceof String) {
			resMessage.setMessage((String) response);
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}

	// update book
	@CrossOrigin
	@PutMapping("/{bookId}")
	public ResponseEntity<ResponseMessage> updateBook(HttpServletRequest request, @PathVariable String bookId,
			@RequestBody Book existBook) {
		User user = authenSSOService.verify(request);
		if (user == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.UNAUTHORIZED);
		Object response = bookService.update(bookId, existBook, user.getEmail(), user.getOwnerBy());
		ResponseMessage resMessage = new ResponseMessage();
		if (response == null) {
			return new ResponseEntity<ResponseMessage>(HttpStatus.FORBIDDEN);
		} else if (response instanceof String) {
			resMessage.setMessage((String) response);
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}

	// delete book
	@DeleteMapping("/{bookId}")
	public ResponseEntity<ResponseMessage> deleteBook(HttpServletRequest request, @PathVariable String bookId) {
		User user = authenSSOService.verify(request);
		if (user == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.UNAUTHORIZED);
		Object response = bookService.delete(bookId, user.getEmail(), user.getOwnerBy());
		ResponseMessage resMessage = new ResponseMessage();
		if (response == null) {
			return new ResponseEntity<ResponseMessage>(HttpStatus.FORBIDDEN);
		} else if (response instanceof String) {
			resMessage.setMessage((String) response);
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}

	// upload book info
	@PostMapping("/upload-info")
	public ResponseEntity<ResponseMessage> uploadFileBooksInfo(@RequestParam("file") MultipartFile uploadfile,
			HttpServletRequest request) {
		User user = authenSSOService.verify(request);
		if (user == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.UNAUTHORIZED);
		ResponseMessage resMessage = new ResponseMessage();
		if (uploadfile.isEmpty()) {
			resMessage.setStatus(0);
			resMessage.setMessage("Bạn chưa chọn file !!!");
			return new ResponseEntity<ResponseMessage>(resMessage, HttpStatus.OK);
		}
		String responseMessage = bookService.saveInfoBooks(Arrays.asList(uploadfile), user.getEmail(),
				user.getOwnerBy());
		if (responseMessage == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.FORBIDDEN);
		resMessage.setStatus(1);
		resMessage.setMessage(responseMessage);
		return new ResponseEntity<ResponseMessage>(resMessage, HttpStatus.OK);

	}

	// upload multiple book content
	@PostMapping("/upload-multiple-book-content")
	public ResponseEntity<ResponseMessage> uploadFileBooksContent(@RequestParam("file") MultipartFile uploadfile,
			HttpServletRequest request) {
		User user = authenSSOService.verify(request);
		if (user == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.UNAUTHORIZED);
		ResponseMessage resMessage = new ResponseMessage();
		if (uploadfile.isEmpty()) {
			resMessage.setStatus(0);
			resMessage.setMessage("Bạn chưa chọn file !!!");
			return new ResponseEntity<ResponseMessage>(resMessage, HttpStatus.OK);
		}
		String responseMessage = bookService.saveMultipleBookContent(Arrays.asList(uploadfile), user.getEmail(),
				user.getOwnerBy());
		if (responseMessage == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.FORBIDDEN);

		resMessage.setStatus(1);
		resMessage.setMessage(responseMessage);
		return new ResponseEntity<ResponseMessage>(resMessage, HttpStatus.OK);
	}

	// upload a book content
	@PostMapping("/upload-book-content")
	public ResponseEntity<ResponseMessage> uploadFileBookContent(@RequestParam("file") MultipartFile uploadfile,
			HttpServletRequest request) {
		User user = authenSSOService.verify(request);
		if (user == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.UNAUTHORIZED);
		ResponseMessage resMessage = new ResponseMessage();
		if (uploadfile.isEmpty()) {
			resMessage.setStatus(0);
			resMessage.setMessage("Bạn chưa chọn file !!!");
			return new ResponseEntity<ResponseMessage>(resMessage, HttpStatus.OK);
		}
		JSONObject response = bookService.saveBookContent(Arrays.asList(uploadfile), user.getEmail(),
				user.getOwnerBy());
		if (response == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.FORBIDDEN);

		resMessage.setStatus(1);
		resMessage.setResults(response);
		return new ResponseEntity<ResponseMessage>(resMessage, HttpStatus.OK);
	}

	// get chapters by bookId
	@GetMapping("/{bookId}/chapters")
	public ResponseEntity<ResponseMessage> getChaptersByBookId(HttpServletRequest request,
			@PathVariable String bookId) {
		User user = authenSSOService.verify(request);
		if (user == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.UNAUTHORIZED);
		List<Chapter> chapters = chapterService.findByBookId(bookId, user.getEmail(), user.getOwnerBy());
		if (chapters == null) {
			return new ResponseEntity<ResponseMessage>(HttpStatus.FORBIDDEN);
		}
		ResponseMessage resMessage = new ResponseMessage();
		resMessage.setResults(chapters);
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}

	// create chapter by bookId
	@PostMapping("/{bookId}/chapters")
	public ResponseEntity<ResponseMessage> createChapterByBookId(HttpServletRequest request,
			@PathVariable String bookId, @RequestBody @Valid Chapter chapter) {
		User user = authenSSOService.verify(request);
		if (user == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.UNAUTHORIZED);
		Object response = chapterService.createByBookId(bookId, user.getEmail(), user.getOwnerBy(), chapter);
		ResponseMessage resMessage = new ResponseMessage();
		if (response == null) {
			return new ResponseEntity<ResponseMessage>(HttpStatus.FORBIDDEN);
		} else if (response instanceof String) {
			resMessage.setMessage((String) response);
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}

	// update chapter by bookId
	@CrossOrigin
	@PutMapping("/{bookId}/chapters/{chapterId}")
	public ResponseEntity<ResponseMessage> updateChapterByBookId(HttpServletRequest request,
			@PathVariable String bookId, @PathVariable String chapterId, @RequestBody Chapter chapter) {
		User user = authenSSOService.verify(request);
		if (user == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.UNAUTHORIZED);
		chapter.setId(chapterId);
		Object response = chapterService.updateByBookId(bookId, user.getEmail(), user.getOwnerBy(), chapter);
		ResponseMessage resMessage = new ResponseMessage();
		if (response == null) {
			return new ResponseEntity<ResponseMessage>(HttpStatus.FORBIDDEN);
		} else if (response instanceof String) {
			resMessage.setMessage((String) response);
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}

	// delete chapter by bookId
	@CrossOrigin
	@DeleteMapping("/{bookId}/chapters/{chapterId}")
	public ResponseEntity<ResponseMessage> deleteChapterByBookId(HttpServletRequest request,
			@PathVariable String bookId, @PathVariable String chapterId) {
		User user = authenSSOService.verify(request);
		if (user == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.UNAUTHORIZED);
		Object response = chapterService.deleteByBookId(bookId, user.getEmail(), user.getOwnerBy(), chapterId);
		ResponseMessage resMessage = new ResponseMessage();
		if (response == null) {
			return new ResponseEntity<ResponseMessage>(HttpStatus.FORBIDDEN);
		} else if (response instanceof String) {
			resMessage.setMessage((String) response);
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}

	// delete chapters by bookId
	@CrossOrigin
	@DeleteMapping("/{bookId}/chapters")
	public ResponseEntity<ResponseMessage> deleteChaptersByBookId(HttpServletRequest request,
			@PathVariable String bookId, @RequestBody List<String> chapterIds) {
		User user = authenSSOService.verify(request);
		if (user == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.UNAUTHORIZED);
		Object response = chapterService.deleteChaptersByBookId(bookId, user.getEmail(), user.getOwnerBy(), chapterIds);
		ResponseMessage resMessage = new ResponseMessage();
		if (response == null) {
			return new ResponseEntity<ResponseMessage>(HttpStatus.FORBIDDEN);
		} else if (response instanceof String) {
			resMessage.setMessage((String) response);
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}

	// synthesis book
	@GetMapping("/{bookId}/synthesis")
	public ResponseEntity<ResponseMessage> synthesize(HttpServletRequest request, @PathVariable String bookId,
			@RequestParam String voice, @RequestParam Integer bitRate) {
		User user = authenSSOService.verify(request);
		if (user == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.UNAUTHORIZED);
		Object response = synthesisService.synthesizeBook(bookId, user.getEmail(), user.getOwnerBy(), voice, bitRate);
		ResponseMessage resMessage = new ResponseMessage();
		if (response == null) {
			return new ResponseEntity<ResponseMessage>(HttpStatus.FORBIDDEN);
		} else if (response instanceof String) {
			resMessage.setMessage((String) response);
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}

	// callback synthesis book
	@GetMapping("/{bookId}/synthesis/callback")
	public ResponseEntity<ResponseMessage> synthesisCallback(@PathVariable String bookId,
			@RequestParam("voice") String voiceValue,
			@RequestParam("chapter-id") String chapterId,
			@RequestParam(name = "startBookTime", required = false) long startBookTime,
			@RequestParam("bit-rate") Integer bitRate,
			@RequestParam(name = "url-audio", required = false) String audioLink,
			@RequestParam("result-type") String resultType,
			@RequestParam("synthesis-id") String synthesisId) {
		ResponseMessage resMessage = new ResponseMessage();
		String response = bookService.calllbackSynthesis(bookId, voiceValue, startBookTime, bitRate, audioLink, resultType, chapterId, synthesisId);
		logger.info(response);
		resMessage.setMessage(response);
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}

	// normalization book
	@GetMapping("/{bookId}/normalization")
	public ResponseEntity<ResponseMessage> normalization(HttpServletRequest request, @PathVariable String bookId) {
		User user = authenSSOService.verify(request);
		if (user == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.UNAUTHORIZED);
		Object response = normalizationService.normalizationBook(bookId, user.getEmail(), user.getOwnerBy());
		ResponseMessage resMessage = new ResponseMessage();
		if (response == null) {
			return new ResponseEntity<ResponseMessage>(HttpStatus.FORBIDDEN);
		} else if (response instanceof String) {
			resMessage.setMessage((String) response);
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}
}
