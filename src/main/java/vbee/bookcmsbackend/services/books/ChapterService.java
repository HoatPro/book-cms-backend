package vbee.bookcmsbackend.services.books;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vbee.bookcmsbackend.authorization.IAuthorizationService;
import vbee.bookcmsbackend.collections.Book;
import vbee.bookcmsbackend.collections.Chapter;
import vbee.bookcmsbackend.config.APIConstant;
import vbee.bookcmsbackend.config.AppConstant;
import vbee.bookcmsbackend.daos.IBookDao;
import vbee.bookcmsbackend.daos.IChapterDao;
import vbee.bookcmsbackend.repositories.ChapterRepository;

@Service
public class ChapterService implements IChapterService {

	@Autowired
	ChapterRepository chapterRepository;

	@Autowired
	IAuthorizationService authorizationService;

	@Autowired
	IBookDao bookDao;

	@Autowired
	IChapterDao chapterDao;

	@Override
	public List<Chapter> findByBookId(String bookId, String email, String ownerEmail) {
		if (ownerEmail == null || ownerEmail.isEmpty())
			return null;
		Integer permission = authorizationService.checkPermission(email, APIConstant.FIND_BOOK_FEATURE_API);
		if (permission == AppConstant.PERMISSION_UNDEFINED)
			return null;
		else if (permission == AppConstant.PERMISSION_ALL_UNIT) {
			email = null;
		}
		Book bookExist = bookDao.findById(bookId, email, ownerEmail);
		if (bookExist == null)
			return null;
		return chapterRepository.findByBookId(bookExist.getId());
	}

	@Override
	public List<Chapter> findByBookId(String bookId) {
		return chapterRepository.findByBookId(bookId);
	}

	@Override
	public Object createByBookId(String bookId, String email, String ownerEmail, Chapter chapter) {
		if (ownerEmail == null || ownerEmail.isEmpty())
			return null;
		String tempEmail = email;
		Integer permission = authorizationService.checkPermission(email, APIConstant.UPDATE_BOOK_FEATURE_API);
		if (permission == AppConstant.PERMISSION_UNDEFINED)
			return null;
		else if (permission == AppConstant.PERMISSION_ALL_UNIT) {
			tempEmail = null;
		}
		// check user might have permission update book
		Book bookExist = bookDao.findById(bookId, tempEmail, ownerEmail);
		if (bookExist == null)
			return null;

		// check chapter exist
		Chapter chapterExist = chapterRepository.findByBookIdAndOrderNo(bookId, chapter.getOrderNo());
		if (chapterExist != null)
			return "Đã tồn tại orderNo: " + chapter.getOrderNo() + ". Vui lòng thử lại với orderNo mới";
		return createChapter(chapter, email, ownerEmail, bookId);
	}

	@Override
	public Object updateByBookId(String bookId, String email, String ownerEmail, Chapter chapter) {
		if (ownerEmail == null || ownerEmail.isEmpty())
			return null;
		String tempEmail = email;
		Integer permission = authorizationService.checkPermission(email, APIConstant.UPDATE_BOOK_FEATURE_API);
		if (permission == AppConstant.PERMISSION_UNDEFINED)
			return null;
		else if (permission == AppConstant.PERMISSION_ALL_UNIT) {
			tempEmail = null;
		}
		// check user might have permission update book
		Book bookExist = bookDao.findById(bookId, tempEmail, ownerEmail);
		if (bookExist == null)
			return null;
		// check chapter exist
		Chapter chapterExist = findById(chapter.getId());
		if (chapterExist == null)
			return "Không tìm thấy chapterId: " + chapter.getId();
		return updateChapter(chapter, chapterExist, email);
	}

	@Override
	public Object deleteByBookId(String bookId, String email, String ownerEmail, String chapterId) {
		if (ownerEmail == null || ownerEmail.isEmpty())
			return null;
		Integer permission = authorizationService.checkPermission(email, APIConstant.UPDATE_BOOK_FEATURE_API);
		if (permission == AppConstant.PERMISSION_UNDEFINED)
			return null;
		else if (permission == AppConstant.PERMISSION_ALL_UNIT) {
			email = null;
		}
		// check user might have permission update book
		Book bookExist = bookDao.findById(bookId, email, ownerEmail);
		if (bookExist == null)
			return null;
		// check chapter exist
		Chapter chapterExist = findById(chapterId);
		if (chapterExist == null)
			return "Không tìm thấy chapterId: " + chapterId;
		chapterRepository.delete(chapterExist);
		return Boolean.TRUE;
	}

	@Override
	public Chapter createChapter(Chapter chapter, String email, String ownerEmail, String bookId) {
		chapter.setBookId(bookId);
		chapter.setOwnerBy(ownerEmail);
		chapter.setCreatedBy(email);
		chapter.setCreatedAt(new Date());
		return chapterRepository.save(chapter);
	}

	private Chapter updateChapter(Chapter chapter, Chapter chapterExist, String email) {
		if (chapter.getTitle() != null && !chapter.getTitle().isEmpty()) {
			chapterExist.setTitle(chapter.getTitle());
		}
		if (chapter.getContent() != null && !chapter.getContent().isEmpty()) {
			chapterExist.setContent(chapter.getContent());
		}
		chapterExist.setUpdatedAt(new Date());
		chapterExist.setUpdatedBy(email);
		return chapterRepository.save(chapterExist);
	}

	@Override
	public Chapter findById(String chapterId) {
		Optional<Chapter> optional = chapterRepository.findById(chapterId);
		if (optional.isPresent())
			return optional.get();
		return null;
	}

	@Override
	public Object deleteChaptersByBookId(String bookId, String email, String ownerEmail, List<String> chapterIds) {
		if (ownerEmail == null || ownerEmail.isEmpty())
			return null;
		Integer permission = authorizationService.checkPermission(email, APIConstant.UPDATE_BOOK_FEATURE_API);
		if (permission == AppConstant.PERMISSION_UNDEFINED)
			return null;
		else if (permission == AppConstant.PERMISSION_ALL_UNIT) {
			email = null;
		}
		// check user might have permission update book
		Book bookExist = bookDao.findById(bookId, email, ownerEmail);
		if (bookExist == null)
			return null;
		// check chapter exist
		for (String chapterId : chapterIds) {
			Chapter chapterExist = findById(chapterId);
			if (chapterExist != null)
				chapterRepository.delete(chapterExist);
		}
		return Boolean.TRUE;
	}

	@Override
	public void createChapters(List<Chapter> chapters) {
		chapterRepository.saveAll(chapters);
	}

}
