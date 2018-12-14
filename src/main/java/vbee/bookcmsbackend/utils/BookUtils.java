package vbee.bookcmsbackend.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vbee.bookcmsbackend.collections.Chapter;

public class BookUtils {
	private static final Logger logger = LoggerFactory.getLogger(BookUtils.class);

	public static String replacePreviewItem(String value, String content) {
		content = content.toLowerCase();
		try {
			JSONObject json = new JSONObject(value);
			JSONArray words = json.getJSONArray("words");
			for (int i = 0; i < words.length(); i++) {
				JSONObject word = new JSONObject(words.get(i).toString());
				if (word.getString("status").equals("checked")) {
					String keyword = word.getString("key");
					JSONArray expandations = word.getJSONArray("expandations");
					for (int j = 0; j < expandations.length(); j++) {
						JSONObject context = new JSONObject(expandations.get(j).toString());
						String expandation = context.getString("expandation");
						// String contextContent = context.getString("context");
						content = content.replace(keyword, expandation);
					}
				}
			}
		} catch (JSONException ex) {
			logger.info("Error when replacePreviewItem !!! ------ " + ex.getMessage());
		}
		return content;
	}

	public static String prepareProcessingChapterContent(Chapter chapter) {
		if (chapter.getContent() == null)
			return "";
		String content = chapter.getContent().trim();
		content = content.replaceAll("<p>", ". ").replaceAll("</p>", " ").trim();
		content = content.replaceAll("\\s{3}", "~");
		content = content.replaceAll("(\\.\\s*)+", ". ");
		content = content.replaceAll("~+", "~");
		content = content.replaceAll("~", "...");
		return content;
	}
}
