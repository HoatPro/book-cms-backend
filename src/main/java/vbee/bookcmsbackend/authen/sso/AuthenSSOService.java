package vbee.bookcmsbackend.authen.sso;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import vbee.bookcmsbackend.collections.User;
import vbee.bookcmsbackend.config.ConfigProperties;
import vbee.bookcmsbackend.services.users_and_roles.IUserService;

@Service
public class AuthenSSOService implements IAuthenSSOService {

	@Autowired
	ConfigProperties configProperties;

	@Autowired
	IUserService userService;
	
	@Override
	public User verify(HttpServletRequest request) {
		Cookie accessTokenCookie = WebUtils.getCookie(request, "accessToken");
		if (accessTokenCookie == null)
			return null;
		String accessToken = accessTokenCookie.getValue();
		String email = SSOApi.isValidAccessToken(configProperties.getSsoPath(), accessToken);
		if (email == null) return null;
		return userService.findByEmail(email);
	}

}
