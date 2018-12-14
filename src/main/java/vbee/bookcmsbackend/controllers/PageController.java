package vbee.bookcmsbackend.controllers;

import java.util.List;

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
import vbee.bookcmsbackend.collections.Page;
import vbee.bookcmsbackend.collections.User;
import vbee.bookcmsbackend.models.ResponseMessage;
import vbee.bookcmsbackend.services.users_and_roles.IPageService;

@RestController
@RequestMapping("/api/v1/pages")
public class PageController {
	private static final Logger logger = LoggerFactory.getLogger(FeatureController.class);

	@Autowired
	IPageService pageService;

	@Autowired
	IAuthenSSOService authenSSOService;

//	find All
	@GetMapping()
	public ResponseEntity<ResponseMessage> findAll(HttpServletRequest request, String pageId) {
		User user = authenSSOService.verify(request);
		if (user == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.UNAUTHORIZED);
		ResponseMessage resMessage = new ResponseMessage();
		List<Page> pages = pageService.findAll(user.getEmail(), user.getOwnerBy());
		if (pages == null) {
			return new ResponseEntity<ResponseMessage>(HttpStatus.FORBIDDEN);
		}
		resMessage.setStatus(1);
		resMessage.setResults(pages);
		return ResponseEntity.ok(resMessage);
	}

	// get feature detail
	@GetMapping("/{pageId}")
	public ResponseEntity<ResponseMessage> getFeature(HttpServletRequest request, @PathVariable String pageId) {
		Page pageDetail = pageService.findById(pageId);
		if (pageDetail == null) {
			return new ResponseEntity<ResponseMessage>(HttpStatus.FORBIDDEN);
		}
		ResponseMessage resMessage = new ResponseMessage();
		resMessage.setResults(pageDetail);
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}

	// create page
	@PostMapping()
	public ResponseEntity<ResponseMessage> creatFeature(HttpServletRequest request, @RequestBody Page newPage) {
		Object response = pageService.create(newPage);
		ResponseMessage resMessage = new ResponseMessage();
		if (response == null) {
			return new ResponseEntity<ResponseMessage>(HttpStatus.FORBIDDEN);
		} else if (response instanceof String) {
			resMessage.setMessage((String) response);
			resMessage.setStatus(0);
			return ResponseEntity.ok(resMessage);
		}
		resMessage.setStatus(1);
		resMessage.setResults(response);
		return ResponseEntity.ok(resMessage);

	}

	// update page
	@CrossOrigin
	@PutMapping("/{pageId}")
	public ResponseEntity<ResponseMessage> updateFature(HttpServletRequest request, @PathVariable String pageId,
			@RequestBody Page existPage) {
		Object response = pageService.update(pageId, existPage);
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

	// delete feature
	@CrossOrigin
	@DeleteMapping("/{pageId}")
	public ResponseEntity<ResponseMessage> deleteRole(HttpServletRequest request, @PathVariable String pageId) {

		if (pageId == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.UNAUTHORIZED);
		Object response = pageService.delete(pageId);
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
