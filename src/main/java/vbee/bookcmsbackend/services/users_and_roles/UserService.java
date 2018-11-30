package vbee.bookcmsbackend.services.users_and_roles;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vbee.bookcmsbackend.authorization.IAuthorizationService;
import vbee.bookcmsbackend.collections.Author;
import vbee.bookcmsbackend.collections.Role;
import vbee.bookcmsbackend.collections.User;
import vbee.bookcmsbackend.config.APIConstant;
import vbee.bookcmsbackend.config.AppConstant;
import vbee.bookcmsbackend.daos.IUserDao;
import vbee.bookcmsbackend.models.Item;
import vbee.bookcmsbackend.models.UserMapFeature;
import vbee.bookcmsbackend.repositories.UserRepository;



@Service
public class UserService implements IUserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	IRoleService roleService;
	
	@Autowired
	IAuthorizationService authorizationService;
	
	@Autowired
	IUserDao userDao;
  
	@Override
	public List<User> findByUserIds(List<String> userId) {
		return userRepository.findByUserIds(userId);
	}

	@Override
	public User findByEmail(String email) {
		Optional<User> option = userRepository.findByEmail(email);
		if (option.isPresent())
			return option.get();
		return null;
	}
	@Override
	public Item findAll(String keyword, Integer page, Integer size, String email, String ownerEmail) {
		if (ownerEmail == null || ownerEmail.isEmpty())
			return null;
		Integer permission = authorizationService.checkPermission(email, APIConstant.FIND_USER_FEATURE_API);
		if (permission == AppConstant.PERMISSION_UNDEFINED)
			return null;
		else if (permission == AppConstant.PERMISSION_ALL_UNIT)
			email = null;
		return userDao.findAll(keyword, page, size, email, ownerEmail);
		
	}
	@Override
	public User findById(String userId, String email, String ownerEmail) {
		if (ownerEmail == null || ownerEmail.isEmpty())
			return null;
		Integer permission = authorizationService.checkPermission(email, APIConstant.FIND_USER_FEATURE_API);
		if (permission == AppConstant.PERMISSION_UNDEFINED)
			return null;
		else if (permission == AppConstant.PERMISSION_ALL_UNIT) {
			email = null;
		}
		User user = userDao.findById(userId, email, ownerEmail);
		
		return user;
	}

	@Override
	public Object create(User newUser, String email, String ownerEmail){
		if (ownerEmail == null || ownerEmail.isEmpty())
			return null;
		Integer permission = authorizationService.checkPermission(email, APIConstant.CREATE_USER_FEATURE_API);
		if (permission == AppConstant.PERMISSION_UNDEFINED)
			return null;
		if (newUser.getEmail() == null || newUser.getEmail().isEmpty()) {
			return " Email không được để trống!! ";
		}
		User userExist = userRepository.findByEmailAndOwnerBy(newUser.getEmail(), ownerEmail);
		if (userExist != null)
			return "Người dùng đã tồn tại";
		newUser.setCreatedAt(new Date());
		newUser.setOwnerBy(ownerEmail);
		return userRepository.save(newUser);
	}
	@Override
	public Object delete(String userId, String email, String ownerEmail) {
		if (ownerEmail == null || ownerEmail.isEmpty())
			return null;
		Integer permission = authorizationService.checkPermission(email, APIConstant.DELETE_USER_FEATURE_API);
		if (permission == AppConstant.PERMISSION_UNDEFINED)
			return null;
		else if (permission == AppConstant.PERMISSION_ALL_UNIT) {
			email = null;
		}
		User userExist = userDao.findById(userId, email, ownerEmail);
		if (userExist == null)
			return null;
		userRepository.delete(userExist);
		return Boolean.TRUE;
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
