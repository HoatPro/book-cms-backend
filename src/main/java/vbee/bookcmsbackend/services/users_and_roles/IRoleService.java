package vbee.bookcmsbackend.services.users_and_roles;

import java.util.List;

import vbee.bookcmsbackend.collections.Role;

public interface IRoleService {

	Role findById(String roleId);

	List<Role> findAll(String email, String ownerBy);

	Role findByLegal();

	Object create(Role newRole);

	Object update(String roleId, Role existRole);

	Object delete(String roleId);

}
