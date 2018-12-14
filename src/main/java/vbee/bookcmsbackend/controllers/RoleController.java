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
import vbee.bookcmsbackend.collections.Role;
import vbee.bookcmsbackend.collections.User;
import vbee.bookcmsbackend.models.ResponseMessage;
import vbee.bookcmsbackend.services.users_and_roles.IRoleService;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

	private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

	@Autowired
	IRoleService roleService;

	@Autowired
	IAuthenSSOService authenSSOService;

//	find All
	@GetMapping()
	public ResponseEntity<ResponseMessage> findAll(HttpServletRequest request, String roleId) {
		User user = authenSSOService.verify(request);
		if (user == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.UNAUTHORIZED);
		ResponseMessage resMessage = new ResponseMessage();
		List<Role> roles = roleService.findAll(user.getEmail(), user.getOwnerBy());
		if (roles == null) {
			return new ResponseEntity<ResponseMessage>(HttpStatus.FORBIDDEN);
		}
		resMessage.setStatus(1);
		resMessage.setResults(roles);
		return ResponseEntity.ok(resMessage);
	}

	// create role
	@PostMapping()
	public ResponseEntity<ResponseMessage> creatRole(HttpServletRequest request, @RequestBody Role newRole) {
		Object response = roleService.create(newRole);
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

	// get role detail
	@GetMapping("/{roleId}")
	public ResponseEntity<ResponseMessage> getRole(HttpServletRequest request, @PathVariable String roleId) {
		Role roleDetail = roleService.findById(roleId);
		if (roleDetail == null) {
			return new ResponseEntity<ResponseMessage>(HttpStatus.FORBIDDEN);
		}
		ResponseMessage resMessage = new ResponseMessage();
		resMessage.setResults(roleDetail);
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}

	// update role
	@CrossOrigin
	@PutMapping("/{roleId}")
	public ResponseEntity<ResponseMessage> updateRole(HttpServletRequest request, @PathVariable String roleId,
			@RequestBody Role existRole) {
		Object response = roleService.update(roleId, existRole);
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

	// delete role
	@CrossOrigin
	@DeleteMapping("/{roleId}")
	public ResponseEntity<ResponseMessage> deleteRole(HttpServletRequest request, @PathVariable String roleId) {

		if (roleId == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.UNAUTHORIZED);
		Object response = roleService.delete(roleId);
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
