package vbee.bookcmsbackend.controllers;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import vbee.bookcmsbackend.authen.sso.IAuthenSSOService;
import vbee.bookcmsbackend.collections.Book;
import vbee.bookcmsbackend.collections.Author;
import vbee.bookcmsbackend.collections.User;
import vbee.bookcmsbackend.models.Item;
import vbee.bookcmsbackend.models.ResponseMessage;
import vbee.bookcmsbackend.services.books.IAuthorService;

@RestController
@RequestMapping("/api/v1/authors")
public class AuthorController {

	private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);

	@Autowired
	IAuthenSSOService authenSSOService;

	@Autowired
	IAuthorService authorService;

	
	// findAll
	@GetMapping()
	public ResponseEntity<ResponseMessage> findAll(HttpServletRequest request, Integer page, Integer size,
			String fields, String sort, String statusIds, String authorId, String keyword) {
		User user = authenSSOService.verify(request);
		if (user == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.UNAUTHORIZED);
		ResponseMessage resMessage = new ResponseMessage();
		Item item = authorService.findAll(keyword, page, size, fields, sort, user.getEmail(), user.getOwnerBy());
		if (item == null) {
			return new ResponseEntity<ResponseMessage>(HttpStatus.FORBIDDEN);
		}
		resMessage.setStatus(1);
		resMessage.setResults(item);
		return ResponseEntity.ok(resMessage);
	}

	// get author detail
	@GetMapping("/{authorId}")
	public ResponseEntity<ResponseMessage> getAuthor(HttpServletRequest request, @PathVariable String authorId) {
		User user = authenSSOService.verify(request);
		if (user == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.UNAUTHORIZED);
		Author author = authorService.findById(authorId, user.getEmail(), user.getOwnerBy());
		if (author == null) {
			return new ResponseEntity<ResponseMessage>(HttpStatus.FORBIDDEN);
		}
		ResponseMessage resMessage = new ResponseMessage();
		resMessage.setResults(author);
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}

	// creat author
	@PostMapping()
	public ResponseEntity<ResponseMessage> createAuthor(HttpServletRequest request,
			@RequestBody Author newAuthor) {
		User user = authenSSOService.verify(request);
		if (user == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.UNAUTHORIZED);
		Object response = authorService.create(newAuthor, user.getEmail(), user.getOwnerBy());
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

	// update author
	@CrossOrigin
	@PutMapping("/{authorId}")
	public ResponseEntity<ResponseMessage> updateBook(HttpServletRequest request, @PathVariable String authorId,
			@RequestBody Author existAuthor) {
		User user = authenSSOService.verify(request);
		if (user == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.UNAUTHORIZED);
		Object response = authorService.update(authorId, existAuthor, user.getEmail(), user.getOwnerBy());
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

	// delete author
	@CrossOrigin
	@DeleteMapping("/{authorId}")
	public ResponseEntity<ResponseMessage> deleteBook(HttpServletRequest request, @PathVariable String authorId) {
		User user = authenSSOService.verify(request);
		if (user == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.UNAUTHORIZED);
		Object response = authorService.delete(authorId, user.getEmail(), user.getOwnerBy());
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
