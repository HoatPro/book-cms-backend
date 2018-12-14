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
import vbee.bookcmsbackend.collections.User;
import vbee.bookcmsbackend.collections.UserAdmin;
import vbee.bookcmsbackend.models.Item;
import vbee.bookcmsbackend.models.ResponseMessage;
import vbee.bookcmsbackend.services.users_and_roles.IUserAdminService;

@RestController
@RequestMapping("/api/v1/users/admin")
public class UserAdminController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	IUserAdminService userAdminService;

	@Autowired
	IAuthenSSOService authenSSOService;

//	find All
	@GetMapping()
	public ResponseEntity<ResponseMessage> findAll(HttpServletRequest request, String userId, String keyword,
			Integer page, Integer size) {
		User user = authenSSOService.verify(request);
		if (user == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.UNAUTHORIZED);
		ResponseMessage resMessage = new ResponseMessage();
		Item item = userAdminService.findAll(keyword, page, size, user.getEmail());
		if (item == null) {
			return new ResponseEntity<ResponseMessage>(HttpStatus.FORBIDDEN);
		}
		resMessage.setStatus(1);
		resMessage.setResults(item);
		return ResponseEntity.ok(resMessage);
	}

	// get user detail
	@GetMapping("/{userId}")
	public ResponseEntity<ResponseMessage> getUser(HttpServletRequest request, @PathVariable String userId) {
		UserAdmin userDetail = userAdminService.findById(userId);
		if (userDetail == null) {
			return new ResponseEntity<ResponseMessage>(HttpStatus.FORBIDDEN);
		}
		ResponseMessage resMessage = new ResponseMessage();
		resMessage.setResults(userDetail);
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}

	// create user
	@PostMapping()
	public ResponseEntity<ResponseMessage> creatUser(HttpServletRequest request, @RequestBody UserAdmin newUserAdmin) {
		Object response = userAdminService.create(newUserAdmin);
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

}
