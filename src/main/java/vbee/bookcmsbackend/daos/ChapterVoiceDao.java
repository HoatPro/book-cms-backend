package vbee.bookcmsbackend.daos;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import vbee.bookcmsbackend.collections.Book;
import vbee.bookcmsbackend.collections.ChapterVoice;
import vbee.bookcmsbackend.collections.Feature;
import vbee.bookcmsbackend.collections.Role;
import vbee.bookcmsbackend.collections.Author;
import vbee.bookcmsbackend.models.Item;
import vbee.bookcmsbackend.utils.HanleUltis;

@Repository
public class ChapterVoiceDao implements IChapterVoiceDao{
	private final MongoTemplate mongoTemplate;

	@Autowired
	ChapterVoiceDao(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	private static final Logger logger = LoggerFactory.getLogger(ChapterVoice.class);

	@Override
	public List<ChapterVoice> findAll(String email, String ownerEmail) {
		Query query = new Query();
		List<ChapterVoice> chapterVoice = mongoTemplate.find(query, ChapterVoice.class);

		return chapterVoice;
	}

	@Override
	public ChapterVoice findByVoiceIdAndChapterIdLatest(String value, String chapterId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("voiceId").is(value));
		query.addCriteria(Criteria.where("chapterId").is(chapterId));
		query.limit(1);
		query.with(new Sort(Direction.DESC, "createdAt"));
		
		return mongoTemplate.findOne(query, ChapterVoice.class);
	}
}
