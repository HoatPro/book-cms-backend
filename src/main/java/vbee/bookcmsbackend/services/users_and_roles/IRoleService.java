package vbee.bookcmsbackend.services.users_and_roles;

import vbee.bookcmsbackend.collections.Role;

public interface IRoleService {

	Role findById(String roleId);

}
