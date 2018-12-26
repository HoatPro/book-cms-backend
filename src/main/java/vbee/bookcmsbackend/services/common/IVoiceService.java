package vbee.bookcmsbackend.services.common;

import java.util.List;

import vbee.bookcmsbackend.collections.Voice;

public interface IVoiceService {

	void checkVoiceOrCreate();

	Voice findByValue(String value);

	List<Voice> findAll(String email, String ownerBy);

	List<Voice> findAll();

	

}
