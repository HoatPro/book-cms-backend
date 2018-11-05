package vbee.bookcmsbackend.authen.sso;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SSOApi {

	private static final Logger logger = LoggerFactory.getLogger(SSOApi.class);

	public static String isValidAccessToken(String ssoPath, String accessToken) {
		try {
			URL obj = new URL(ssoPath + "/api/users");
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			// optional default is POST
			con.setRequestMethod("GET");
			con.setRequestProperty("Authorization", accessToken);
			// response
			int responseCode = con.getResponseCode();
			logger.info("responseCode: " + responseCode);
			if (responseCode == 200) {
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String output;
				StringBuffer response = new StringBuffer();

				while ((output = in.readLine()) != null) {
					response.append(output);
				}
				in.close();
				JSONParser parser = new JSONParser();
				JSONObject result = (JSONObject) parser.parse(response.toString());
				if (result.containsKey("email")) {
					return (String) result.get("email");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
