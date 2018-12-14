package vbee.bookcmsbackend.services.users_and_roles;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vbee.bookcmsbackend.authorization.IAuthorizationService;
import vbee.bookcmsbackend.collections.Role;
import vbee.bookcmsbackend.config.APIConstant;
import vbee.bookcmsbackend.config.AppConstant;
import vbee.bookcmsbackend.daos.IRoleDao;
import vbee.bookcmsbackend.repositories.RoleRepository;

@Service
public class RoleService implements IRoleService {

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	IAuthorizationService authorizationService;

	@Autowired
	IRoleDao roleDao;

	@Override
	public Role findById(String roleId) {
		Optional<Role> optional = roleRepository.findById(roleId);
		if (optional.isPresent())
			return optional.get();
		return null;
	}

	@Override
	public List<Role> findAll(String email, String ownerEmail) {
		if (ownerEmail == null || ownerEmail.isEmpty())
			return null;
		Integer permission = authorizationService.checkPermission(email, APIConstant.ROLE_USER_FEATURE_API);
		if (permission == AppConstant.PERMISSION_UNDEFINED)
			return null;
		else if (permission == AppConstant.PERMISSION_ALL_UNIT)
			email = null;
		return roleDao.findAll(email, ownerEmail);

	}

	@Override
	public Role findByLegal() {
		return roleRepository.findByIsOwner(Boolean.TRUE);
	}

	@Override
	public Object create(Role newRole) {
		if (newRole.getName() == null || newRole.getName().isEmpty()) {
			return " Tên Role không được để trống!! ";
		}
		Role roleExist = roleRepository.findByName(newRole.getName());
		if (roleExist != null)
			return "Role đã tồn tại";
		newRole.setCreatedAt(new Date());
		return roleRepository.save(newRole);
	}

	@Override
	public Object update(String roleId, Role role) {
		Role roleExist = roleDao.findById(roleId);
		if (roleExist == null)
			return null;
		return updateRole(role, roleExist);
	}

	private Object updateRole(Role role, Role roleExist) {
		if (role.getName() != null && !role.getName().isEmpty() && !role.getName().equals(roleExist.getName())) {
			Role roleCheck = roleRepository.findByName(role.getName());
			if (roleCheck != null)
				return "Tên role đã tồn tại. Vui lòng thử lại";
			roleExist.setName(role.getName());
		}
		if (role.getFeatureIds() != null && !role.getFeatureIds().isEmpty()) {
			roleExist.setFeatureIds(role.getFeatureIds());
		}
		role.setUpdatedAt(new Date());
		return roleRepository.save(roleExist);
	}

	@Override
	public Object delete(String roleId) {
		Role roleExist = roleDao.findById(roleId);
		if (roleExist == null)
			return null;
		roleRepository.delete(roleExist);
		return Boolean.TRUE;
	}

}
