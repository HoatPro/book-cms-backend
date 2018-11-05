package vbee.bookcmsbackend.utils;

public class TextUtils {
	public static String toKey(String key) {
		return key.replace("_", "").replaceAll("[^\\d\\w<>]+\\s*", " ").trim().toLowerCase().replaceAll("\\s+", "");
	}
}
