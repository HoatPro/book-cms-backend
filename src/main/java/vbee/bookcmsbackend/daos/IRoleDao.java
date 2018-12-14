package vbee.bookcmsbackend.daos;

import java.util.List;

import vbee.bookcmsbackend.collections.Role;

public interface IRoleDao {

	List<Role> findAll(String email, String ownerEmail);

	Role findById(String roleId);

}
