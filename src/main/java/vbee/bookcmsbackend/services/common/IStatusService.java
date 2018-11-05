package vbee.bookcmsbackend.services.common;

import vbee.bookcmsbackend.collections.Status;

public interface IStatusService {

	Status findById(Integer statusId);

	void checkStatusOrCreate();

}
