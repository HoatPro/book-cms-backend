package vbee.bookcmsbackend.services.books;

import java.util.List;

import vbee.bookcmsbackend.collections.ChapterVoice;

public interface IChapterVoiceService {

	ChapterVoice findByVoiceIdAndChapterId(String voiceValue, String chapterId);

	ChapterVoice save(ChapterVoice chapterVoice);

	List<ChapterVoice> findByBookIdAndVoiceId(String bookId, String voiceValue, String synthesisId);

	List<ChapterVoice> findByBookIdAndVoiceIdAndSuccess(String bookId, String voiceValue, String synthesisId,
			Boolean isSuccess);

}
