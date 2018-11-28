package vbee.bookcmsbackend.services.users_and_roles;

import vbee.bookcmsbackend.collections.Role;
import vbee.bookcmsbackend.models.Item;

public interface IRoleService {

	Role findById(String roleId);

	Item findAll(String email, String ownerBy);

}
