package vbee.bookcmsbackend.authen.sso;

import java.io.BufferedReader;
import java.io.DataOutputStream;
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
			logger.info("Sending 'POST' to url: " + ssoPath + "/api/users");
			// optional default is GET
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

	public static Boolean checkUserExists(String ssoPath, String email) {
		try {
			URL obj = new URL(ssoPath + "/api/oath");
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			JSONObject json = new JSONObject();
			json.put("email", email);
			json.put("password", "");
			json.put("type", "password");
			logger.info("Sending 'POST' to url: " + ssoPath + "/api/oath");
			// optional default is POST
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.write(json.toJSONString().getBytes("UTF-8"));
			wr.flush();
			wr.close();
			// response
			int responseCode = con.getResponseCode();
			logger.info("responseCode: " + responseCode);
			if (responseCode != 200) {
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
				String output;
				StringBuffer response = new StringBuffer();
				while ((output = in.readLine()) != null) {
					response.append(output);
				}
				in.close();
				if (response.toString().contains("Email " + email + " is not found")) {
					return Boolean.FALSE;
				} else {
					return Boolean.TRUE;
				}
			} else
				return Boolean.TRUE;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Boolean.FALSE;
	}
}
