package vbee.bookcmsbackend.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import vbee.bookcmsbackend.collections.Role;

public interface RoleRepository extends MongoRepository<Role, String>{

}
