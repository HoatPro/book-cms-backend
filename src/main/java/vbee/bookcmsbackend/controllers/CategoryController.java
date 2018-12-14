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
import vbee.bookcmsbackend.collections.Category;
import vbee.bookcmsbackend.collections.User;
import vbee.bookcmsbackend.models.Item;
import vbee.bookcmsbackend.models.ResponseMessage;
import vbee.bookcmsbackend.services.books.ICategoryService;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

	private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

	@Autowired
	IAuthenSSOService authenSSOService;

	@Autowired
	ICategoryService categoryService;

	// findAll
	@CrossOrigin
	@GetMapping()
	public ResponseEntity<ResponseMessage> findAll(HttpServletRequest request, Integer page, Integer size,
			String fields, String sort, String statusIds, String categoryId, String keyword) {
		User user = authenSSOService.verify(request);
		if (user == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.UNAUTHORIZED);
		ResponseMessage resMessage = new ResponseMessage();
		Item item = categoryService.findAll(keyword, page, size, fields, sort, user.getEmail(), user.getOwnerBy());
		if (item == null) {
			return new ResponseEntity<ResponseMessage>(HttpStatus.FORBIDDEN);
		}
		resMessage.setStatus(1);
		resMessage.setResults(item);
		return ResponseEntity.ok(resMessage);
	}

	// get category detail
	@GetMapping("/{categoryId}")
	public ResponseEntity<ResponseMessage> getCategory(HttpServletRequest request, @PathVariable String categoryId) {
		User user = authenSSOService.verify(request);
		if (user == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.UNAUTHORIZED);
		Category category = categoryService.findById(categoryId, user.getEmail(), user.getOwnerBy());
		if (category == null) {
			return new ResponseEntity<ResponseMessage>(HttpStatus.FORBIDDEN);
		}
		ResponseMessage resMessage = new ResponseMessage();
		resMessage.setResults(category);
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}

	// creat category
	@PostMapping()
	public ResponseEntity<ResponseMessage> createCategory(HttpServletRequest request,
			@RequestBody Category newCategory) {
		User user = authenSSOService.verify(request);
		if (user == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.UNAUTHORIZED);
		Object response = categoryService.create(newCategory, user.getEmail(), user.getOwnerBy());
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

	// update category
	@CrossOrigin
	@PutMapping("/{categoryId}")
	public ResponseEntity<ResponseMessage> updateBook(HttpServletRequest request, @PathVariable String categoryId,
			@RequestBody Category existCategory) {
		User user = authenSSOService.verify(request);
		if (user == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.UNAUTHORIZED);
		Object response = categoryService.update(categoryId, existCategory, user.getEmail(), user.getOwnerBy());
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

	// delete category
	@CrossOrigin
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<ResponseMessage> deleteBook(HttpServletRequest request, @PathVariable String categoryId) {
		User user = authenSSOService.verify(request);
		if (user == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.UNAUTHORIZED);
		Object response = categoryService.delete(categoryId, user.getEmail(), user.getOwnerBy());
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
