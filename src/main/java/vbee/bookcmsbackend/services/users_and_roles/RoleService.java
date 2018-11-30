package vbee.bookcmsbackend.services.users_and_roles;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vbee.bookcmsbackend.authorization.IAuthorizationService;
import vbee.bookcmsbackend.collections.Role;
import vbee.bookcmsbackend.config.APIConstant;
import vbee.bookcmsbackend.config.AppConstant;
import vbee.bookcmsbackend.daos.IRoleDao;
import vbee.bookcmsbackend.daos.IUserDao;
import vbee.bookcmsbackend.models.Item;
import vbee.bookcmsbackend.repositories.RoleRepository;

@Service
public class RoleService implements IRoleService{

	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	IAuthorizationService authorizationService;
	
	
	@Autowired
	IRoleDao roleDao;
	
	@Override
	public Role findById(String roleId) {
		Optional<Role> optional = roleRepository.findById(roleId);
		if (optional.isPresent()) return optional.get();
		return null;
	}

	@Override
	public List<Role> findAll( String email, String ownerEmail) {
		if (ownerEmail == null || ownerEmail.isEmpty())
			return null;
		Integer permission = authorizationService.checkPermission(email, APIConstant.ROLE_USER_FEATURE_API);
		if (permission == AppConstant.PERMISSION_UNDEFINED)
			return null;
		else if (permission == AppConstant.PERMISSION_ALL_UNIT)
			email = null;
		return roleDao.findAll( email, ownerEmail);
		
	}

}
