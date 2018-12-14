package vbee.bookcmsbackend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import vbee.bookcmsbackend.collections.User;
import vbee.bookcmsbackend.collections.UserAdmin;

public interface UserAdminRepository extends MongoRepository<UserAdmin, String> {

	Optional<UserAdmin> findByEmail(String email);
	UserAdmin findByEmailAndOwnerBy(String email, String ownerBy);

}
