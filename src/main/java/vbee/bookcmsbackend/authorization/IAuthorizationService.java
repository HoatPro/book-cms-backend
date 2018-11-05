package vbee.bookcmsbackend.authorization;

public interface IAuthorizationService {

	Integer checkPermission(String email, String apiKey);

}
