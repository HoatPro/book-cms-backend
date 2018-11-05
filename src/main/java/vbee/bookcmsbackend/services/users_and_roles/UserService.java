package vbee.bookcmsbackend.services.users_and_roles;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vbee.bookcmsbackend.collections.Role;
import vbee.bookcmsbackend.collections.User;
import vbee.bookcmsbackend.models.UserMapFeature;
import vbee.bookcmsbackend.repositories.UserRepository;

@Service
public class UserService implements IUserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	IRoleService roleService;

	@Override
	public User findByEmail(String email) {
		Optional<User> option = userRepository.findById(email);
		if (option.isPresent())
			return option.get();
		return null;
	}

	@Override
	public User create(User user) {
		user.setCreatedAt(new Date());
		return userRepository.save(user);
	}

	@Override
	public void loadAllUserFeatures() {
		List<User> users = userRepository.findAll();
		for (User user : users) {
			List<String> featureIds = new ArrayList<>();
			if (user.getRoleIds() != null) {
				for (String roleId : user.getRoleIds()) {
					Role role = roleService.findById(roleId);
					featureIds.addAll(role.getFeatureIds());
				}
			}
			UserMapFeature.loadUserFeauture(user.getEmail(), featureIds);
			featureIds = null;
		}
	}

}
