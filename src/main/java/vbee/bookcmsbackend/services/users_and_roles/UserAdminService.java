package vbee.bookcmsbackend.services.users_and_roles;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import vbee.bookcmsbackend.authorization.IAuthorizationService;
import vbee.bookcmsbackend.collections.User;
import vbee.bookcmsbackend.collections.UserAdmin;
import vbee.bookcmsbackend.daos.IUserAdminDao;
import vbee.bookcmsbackend.models.Item;
import vbee.bookcmsbackend.repositories.UserAdminRepository;


@Service
public class UserAdminService implements IUserAdminService {
	@Autowired
	UserAdminRepository userAdminRepository;

	@Autowired
	IRoleService roleService;

	@Autowired
	IAuthorizationService authorizationService;

	@Autowired
	IUserAdminDao userAdminDao;

	private List<String> roleIds;

	@Override
	public UserAdmin findByEmail(String email) {
		Optional<UserAdmin> option = userAdminRepository.findByEmail(email);
		if (option.isPresent())
			return option.get();
		return null;
	}

	@Override
	public Item findAll(String keyword, Integer page, Integer size, String email) {

		return userAdminDao.findAll(keyword, page, size, email);

	}

	@Override
	public List<User> findByUserIds(List<String> userId) {

		User user = userAdminDao.findById(userId);

		return (List<User>) user;
	}

	@Override
	public UserAdmin findById(String userId) {
		UserAdmin user = userAdminDao.findById(userId);

		return user;
	}

	@Override
	public Object create(UserAdmin newUserAdmin) {
		String email = newUserAdmin.getEmail();
		if (StringUtils.isEmpty(email)) {
			return " Email không được để trống!! ";
		} else {
			Optional<UserAdmin> option = userAdminRepository.findByEmail(email);
			if (!Optional.empty().equals(option))
				return "Người dùng đã tồn tại";
			else {
				newUserAdmin.setCreatedAt(new Date());
				return userAdminRepository.save(newUserAdmin);
			}

		}
	}

}
