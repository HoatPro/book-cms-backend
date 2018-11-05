package vbee.bookcmsbackend.thread;

import java.net.URI;
import java.util.List;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vbee.bookcmsbackend.collections.Chapter;
import vbee.bookcmsbackend.config.ConfigProperties;
import vbee.bookcmsbackend.models.BookEntity;
import vbee.bookcmsbackend.utils.BookUtils;

public class SynthesisBookThread extends Thread {

	private Thread t;
	private BookEntity bookEntity;
	private static final Logger logger = LoggerFactory.getLogger(SynthesisBookThread.class);

	SynthesisBookThread(BookEntity bookSynthesis) {
		this.bookEntity = bookSynthesis;
	}

	public void run() {
		List<Chapter> chapters = bookEntity.getChapters();
		long startTimeBook = System.currentTimeMillis();
		for (Chapter chapter : chapters) {
			synthesisChapter(chapter, bookEntity.getVoice(), bookEntity.getBitRate(), startTimeBook);
		}
		SynthesisBookQueue.finish();
	}

	private void synthesisChapter(Chapter chapter, String voiceValue, Integer bitRate, long startTimeBook) {
		try {
			ConfigProperties configProperties = bookEntity.getConfigProperties();
			String synthesisId = System.currentTimeMillis() + "_" + chapter.getBookId();
			logger.info("ws: " + configProperties.getTtsPath());
			WebSocketClient ws = new WebSocketClient(new URI(configProperties.getTtsPath()), new Draft_6455()) {

				@Override
				public void onClose(int i, String reason, boolean remote) {
					logger.info("Connection closed by " + (remote ? "remote peer" : "us") + " and reason is " + reason);

				}

				@Override
				public void onError(Exception arg0) {
					// TODO Auto-generated method stub
					arg0.printStackTrace();
					close();
				}

				@Override
				public void onMessage(String message) {
					logger.info("Recieved message: " + message);
					handleReceiveMessage(message);
					close();
				}

				@Override
				public void onOpen(ServerHandshake arg0) {
					logger.info("server is opened");
					JSONObject messenger = new JSONObject();

					// handle request synthesis
					messenger.clear();
					String callback = "http://" + configProperties.getIp() + ":" + configProperties.getPort()
							+ "/api/v1/books/" + chapter.getBookId() + "/synthesis/callback?chapter-id="
							+ chapter.getId() + "&startBookTime=" + startTimeBook + "&bit-rate=" + bitRate
							+ "&synthesis-id=" + synthesisId;
					logger.info("callback: " + callback);
					String content = "";
					content = BookUtils.prepareProcessingChapterContent(chapter);

					if (chapter.getNormalizationValue() != null && !chapter.getNormalizationValue().isEmpty())
						content = BookUtils.replacePreviewItem(chapter.getNormalizationValue(), content);
					if (voiceValue.equals("hn_male_xuantin_vdts_48k-hsmm"))
						messenger.put("RATE", "0.85");
					messenger.put("INPUT_TEXT", content);
					messenger.put("VOICE", voiceValue);
					messenger.put("APP_ID", configProperties.getAppId());
					messenger.put("HTTP_CALLBACK", callback);
					messenger.put("BIT_RATE", bitRate);
					messenger.put("OTHER", chapter.getId());
					send(messenger.toString());
					messenger = null;

				}
			};

			ws.connect();
			while (true) {
				if (ws.getConnection().isOpen()) {
					logger.info("CONNECTING...");
					break;
				}
				Thread.sleep(100);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Error when synthesize chapter: " + chapter.getTitle() + " ------------- " + e.getMessage());
		}
	}

	protected void handleReceiveMessage(String message) {
		try {
			JSONParser parser = new JSONParser();
			JSONObject response = (JSONObject) parser.parse(message);
			Integer status = Integer.parseInt("" + (long) response.get("status"));
			String chapterId = (String) response.get("OTHER");
			if (status == 1) {
				logger.info("Chapter: " + chapterId + " đang được tổng hợp !!!");
			} else {
				logger.info("Chapter: " + chapterId + " lỗi khi truyền dữ liệu !!!");
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Error when parse response message --------- " + e.getMessage());
		}
	}

	public void start() {
		if (t == null) {
			t = new Thread(this);
			t.start();
		}
	}
}
