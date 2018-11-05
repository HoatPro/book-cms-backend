package vbee.bookcmsbackend.services.books;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vbee.bookcmsbackend.collections.ChapterVoice;
import vbee.bookcmsbackend.repositories.ChapterVoiceRepository;

@Service
public class ChapterVoiceService implements IChapterVoiceService {

	@Autowired
	ChapterVoiceRepository chapterVoiceRepository;

	@Override
	public ChapterVoice findByVoiceIdAndChapterId(String voiceId, String chapterId) {
		return chapterVoiceRepository.findByVoiceIdAndChapterId(voiceId, chapterId);
	}

	@Override
	public ChapterVoice save(ChapterVoice chapterVoice) {
		return chapterVoiceRepository.save(chapterVoice);
	}

	@Override
	public List<ChapterVoice> findByBookIdAndVoiceId(String bookId, String voiceId, String synthesisId) {
		return chapterVoiceRepository.findByBookIdAndVoiceIdAndSynthesisId(bookId, voiceId, synthesisId);
	}

	@Override
	public List<ChapterVoice> findByBookIdAndVoiceIdAndSuccess(String bookId, String voiceId, String synthesisId,
			Boolean isSuccess) {
		return chapterVoiceRepository.findByBookIdAndVoiceIdAndSynthesisIdAndIsSuccess(bookId, voiceId, synthesisId,
				isSuccess);
	}

}
