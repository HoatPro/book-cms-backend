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
import vbee.bookcmsbackend.collections.Feature;
import vbee.bookcmsbackend.collections.User;
import vbee.bookcmsbackend.models.ResponseMessage;
import vbee.bookcmsbackend.services.users_and_roles.IFeatureService;

@RestController
@RequestMapping("/api/v1/features")
public class FeatureController {

	private static final Logger logger = LoggerFactory.getLogger(FeatureController.class);

	@Autowired
	IFeatureService featureService;

	@Autowired
	IAuthenSSOService authenSSOService;

//	find All
	@GetMapping()
	public ResponseEntity<ResponseMessage> findAll(HttpServletRequest request, String featureId) {
		User user = authenSSOService.verify(request);
		if (user == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.UNAUTHORIZED);
		ResponseMessage resMessage = new ResponseMessage();
		List<Feature> features = featureService.findAll(user.getEmail(), user.getOwnerBy());
		if (features == null) {
			return new ResponseEntity<ResponseMessage>(HttpStatus.FORBIDDEN);
		}
		resMessage.setStatus(1);
		resMessage.setResults(features);
		return ResponseEntity.ok(resMessage);
	}

	// get feature detail
	@GetMapping("/{featureId}")
	public ResponseEntity<ResponseMessage> getFeature(HttpServletRequest request, @PathVariable String featureId) {
		Feature featureDetail = featureService.findById(featureId);
		if (featureDetail == null) {
			return new ResponseEntity<ResponseMessage>(HttpStatus.FORBIDDEN);
		}
		ResponseMessage resMessage = new ResponseMessage();
		resMessage.setResults(featureDetail);
		resMessage.setStatus(1);
		return ResponseEntity.ok(resMessage);
	}

	// create feature
	@PostMapping()
	public ResponseEntity<ResponseMessage> creatFeature(HttpServletRequest request, @RequestBody Feature newFeature) {
		Object response = featureService.create(newFeature);
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

	// update feature
	@CrossOrigin
	@PutMapping("/{featureId}")
	public ResponseEntity<ResponseMessage> updateFature(HttpServletRequest request, @PathVariable String featureId,
			@RequestBody Feature existFeature) {
		Object response = featureService.update(featureId, existFeature);
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
	@DeleteMapping("/{featureId}")
	public ResponseEntity<ResponseMessage> deleteRole(HttpServletRequest request, @PathVariable String featureId) {

		if (featureId == null)
			return new ResponseEntity<ResponseMessage>(HttpStatus.UNAUTHORIZED);
		Object response = featureService.delete(featureId);
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
