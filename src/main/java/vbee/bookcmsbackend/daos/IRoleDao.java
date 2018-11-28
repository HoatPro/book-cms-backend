package vbee.bookcmsbackend.daos;

import vbee.bookcmsbackend.models.Item;

public interface IRoleDao {

	Item findAll(String email, String ownerEmail);

}
