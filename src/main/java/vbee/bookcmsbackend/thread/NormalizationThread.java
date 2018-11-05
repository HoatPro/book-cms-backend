package vbee.bookcmsbackend.thread;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.NotYetConnectedException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vbee.bookcmsbackend.collections.Book;
import vbee.bookcmsbackend.collections.Chapter;
import vbee.bookcmsbackend.config.AppConstant;
import vbee.bookcmsbackend.config.ConfigProperties;
import vbee.bookcmsbackend.models.BookEntity;
import vbee.bookcmsbackend.repositories.BookRepository;
import vbee.bookcmsbackend.repositories.ChapterRepository;
import vbee.bookcmsbackend.utils.BookUtils;

public class NormalizationThread extends Thread {

	private static final Logger logger = LoggerFactory.getLogger(NormalizationThread.class);

	private Thread t;
	private BookEntity bookEntity;
	private int countChapterReceived = 0;
	private int countChapterOfBook = 0;
	private Queue<Chapter> queueChapterNSW = new LinkedList<Chapter>();
	private boolean canRun = true;
	private WebSocketClient ws = null;

	NormalizationThread(BookEntity bookEntity) {
		this.bookEntity = bookEntity;
	}

	public void run() {
		List<Chapter> chapters = bookEntity.getChapters();
		for (Chapter chapter : chapters) {
			queueChapterNSW.add(chapter);
		}
		logger.info("ws: " + bookEntity.getConfigProperties().getWsNomalizationPath() + " --- book title: "
				+ bookEntity.getBook().getTitle());
		startNormalizationChapter();
	}

	private void startNormalizationChapter() {
		if (!queueChapterNSW.isEmpty() && canRun) {
			Chapter chapter = queueChapterNSW.poll();
			normalizationChapter(chapter);
		}

	}

	private void normalizationChapter(Chapter chapter) {
		ConfigProperties configProperties = bookEntity.getConfigProperties();
		Book book = bookEntity.getBook();
		ChapterRepository chapterRepository = bookEntity.getChapterRepository();
		BookRepository bookRepository = bookEntity.getBookRepository();
		countChapterOfBook = bookEntity.getChapters().size();
		try {
			ws = new WebSocketClient(new URI(configProperties.getWsNomalizationPath()),
					new Draft_6455()) {

				@Override
				public void onOpen(ServerHandshake sh) {
					logger.info("server is opened");
					JSONObject messenger = new JSONObject();
					String content = BookUtils.prepareProcessingChapterContent(chapter);
					messenger.put("APP_ID", configProperties.getAppId());
					messenger.put("INPUT_TEXT", content);
					messenger.put("OUTPUT_TYPE", "NSW");
					messenger.put("OTHER", "" + chapter.getId());
					send(messenger.toString());
				}

				@Override
				public void onMessage(String message) {
					logger.info("received: " + message);
					countChapterReceived++;
					handleReceiveMessage(message, chapter.getBookId(), chapterRepository);
					if (countChapterReceived == countChapterOfBook) {
						Optional<Book> optional = bookRepository.findById(chapter.getBookId());
						if (optional.isPresent()) {
							book.setStatusId(AppConstant.NORMALIZED);
							logger.info("Normalized book: " + book.getTitle());
							bookRepository.save(book);
						}
						canRun = true;
						bookEntity = null;
					}
					close();
				}

				@Override
				public void onClose(int i, String reason, boolean remote) {
					logger.info("Connection closed by " + (remote ? "remote peer" : "us") + " and reason is " + reason);
					startNormalizationChapter();
				}

				@Override
				public void onError(Exception ex) {
					ex.printStackTrace();
					canRun = true;
					startNormalizationChapter();
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

		} catch (InterruptedException | URISyntaxException | NotYetConnectedException e) {
			logger.info("Error when normalization Book: " + book.getTitle() + " ----" + e.getMessage());
		}

	}

	protected void handleReceiveMessage(String message, String bookId, ChapterRepository chapterRepository) {
		try {
			JSONParser parser = new JSONParser();
			JSONObject response = (JSONObject) parser.parse(message);
			String chapterId = (String) response.get("other");
			JSONObject content = (JSONObject) response.get("content");
			String value = content.toString();
			if (value != null && !value.isEmpty()) {
				Optional<Chapter> optional = chapterRepository.findById(chapterId);
				if (optional.isPresent()) {
					Chapter chapter = optional.get();
					chapter.setNormalizationValue(value);
					chapter.setStatusId(AppConstant.NORMALIZED);
					chapterRepository.save(chapter);
					logger.info("Chapter " + chapter.getId() + " normalization saved");
				}
			}
		} catch (ParseException e) {
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
