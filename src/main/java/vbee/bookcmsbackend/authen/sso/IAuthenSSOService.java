package vbee.bookcmsbackend.authen.sso;

import javax.servlet.http.HttpServletRequest;

import vbee.bookcmsbackend.collections.User;

public interface IAuthenSSOService {

	User verify(HttpServletRequest request);

}
