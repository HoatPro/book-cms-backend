package vbee.bookcmsbackend.daos;

import java.util.List;

import vbee.bookcmsbackend.collections.Voice;
import vbee.bookcmsbackend.models.Item;

public interface IVoiceDao {

	List<Voice> findAll(String email, String ownerBy);

}
