package vbee.bookcmsbackend.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import vbee.bookcmsbackend.collections.Chapter;

public interface ChapterRepository extends MongoRepository<Chapter, String>{

	List<Chapter> findByBookId(String bookId);

	Chapter findByBookIdAndOrderNo(String bookId, Integer orderNo);

}
