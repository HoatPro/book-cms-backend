package vbee.bookcmsbackend.daos;

import java.util.List;

import vbee.bookcmsbackend.collections.Role;
import vbee.bookcmsbackend.models.Item;

public interface IRoleDao {

	List<Role> findAll(String email, String ownerEmail);

}
