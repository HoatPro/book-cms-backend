package vbee.bookcmsbackend.daos;

import java.util.List;

import vbee.bookcmsbackend.collections.ChapterVoice;

public interface IChapterVoiceDao {

	List<ChapterVoice> findAll(String email, String ownerEmail);

	ChapterVoice findByVoiceIdAndChapterIdLatest(String value, String chapterId);

}
