package vbee.bookcmsbackend.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import vbee.bookcmsbackend.collections.ChapterVoice;

public interface ChapterVoiceRepository extends MongoRepository<ChapterVoice, String>{

	ChapterVoice findByVoiceIdAndChapterId(String voiceId, String chapterId);

	List<ChapterVoice> findByBookIdAndVoiceIdAndSynthesisId(String bookId, String voiceId, String synthesisId);

	List<ChapterVoice> findByBookIdAndVoiceIdAndSynthesisIdAndIsSuccess(String bookId, String voiceId,
			String synthesisId, Boolean isSuccess);

}
