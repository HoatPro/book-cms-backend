package vbee.bookcmsbackend.thread;

import java.util.LinkedList;
import java.util.Queue;

import vbee.bookcmsbackend.models.BookEntity;

public class SynthesisBookQueue {
	private static Queue<BookEntity> queueSynthesis = new LinkedList<>();
	public static Boolean canRun = true;

	public synchronized static void push(BookEntity bookSynthesis) {
		queueSynthesis.add(bookSynthesis);
	}

	public synchronized static void run() {
		if (!queueSynthesis.isEmpty() && canRun) {
			synthesis();
		}
	}

	public synchronized static void finish() {
		canRun = true;
		run();
	}

	private synchronized static void synthesis() {
		canRun = false;
		BookEntity bookSynthesis = queueSynthesis.poll();
		SynthesisBookThread bookSynthesisThread = new SynthesisBookThread(bookSynthesis);
		bookSynthesisThread.start();
	}

}
