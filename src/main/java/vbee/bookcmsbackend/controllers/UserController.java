package vbee.bookcmsbackend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vbee.bookcmsbackend.collections.User;
import vbee.bookcmsbackend.models.ResponseMessage;
import vbee.bookcmsbackend.services.users_and_roles.IUserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	IUserService userService;
	
	@PostMapping
	public ResponseEntity<ResponseMessage> creatUser(@RequestBody User user) {
		ResponseMessage resMessage = new ResponseMessage();
		User userCreated = userService.create(user);
		resMessage.setStatus(1);
		resMessage.setResults(userCreated);
		return ResponseEntity.ok(resMessage);
	}
}
