package vbee.bookcmsbackend.services.common;

import vbee.bookcmsbackend.collections.Voice;

public interface IVoiceService {

	void checkVoiceOrCreate();

	Voice findByValue(String value);

}
