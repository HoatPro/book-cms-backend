package vbee.bookcmsbackend.services.users_and_roles;

import java.util.List;

import vbee.bookcmsbackend.collections.Role;
import vbee.bookcmsbackend.models.Item;

public interface IRoleService {

	Role findById(String roleId);

	List<Role> findAll(String email, String ownerBy);

}
