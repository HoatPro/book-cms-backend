package vbee.bookcmsbackend.thread;

import java.util.LinkedList;
import java.util.Queue;

import vbee.bookcmsbackend.models.BookEntity;

public class NormalizationBookQueue {
	private static Queue<BookEntity> queueNormalization = new LinkedList<>();
	public static Boolean canRun = true;

	public synchronized static void push(BookEntity bookEntity) {
		queueNormalization.add(bookEntity);
	}

	public synchronized static void run() {
		if (!queueNormalization.isEmpty() && canRun) {
			normalization();
		}
	}

	public synchronized static void finish() {
		canRun = true;
		run();
	}

	private synchronized static void normalization() {
		canRun = false;
		BookEntity bookEntity = queueNormalization.poll();
		NormalizationThread normalizationThread = new NormalizationThread(bookEntity);
		normalizationThread.start();
	}

}
