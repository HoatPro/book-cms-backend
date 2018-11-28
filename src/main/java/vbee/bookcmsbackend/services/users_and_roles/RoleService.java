package vbee.bookcmsbackend.services.users_and_roles;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vbee.bookcmsbackend.collections.Role;
import vbee.bookcmsbackend.models.Item;
import vbee.bookcmsbackend.repositories.RoleRepository;

@Service
public class RoleService implements IRoleService{

	@Autowired
	RoleRepository roleRepository;
	
	@Override
	public Role findById(String roleId) {
		Optional<Role> optional = roleRepository.findById(roleId);
		if (optional.isPresent()) return optional.get();
		return null;
	}

	@Override
	public Item findAll(String email, String ownerBy) {
		// TODO Auto-generated method stub
		return null;
	}

}
