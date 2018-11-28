package vbee.bookcmsbackend.services.books;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vbee.bookcmsbackend.authorization.IAuthorizationService;
import vbee.bookcmsbackend.collections.Author;
import vbee.bookcmsbackend.config.APIConstant;
import vbee.bookcmsbackend.config.AppConstant;
import vbee.bookcmsbackend.daos.IAuthorDao;
import vbee.bookcmsbackend.models.Item;
import vbee.bookcmsbackend.repositories.AuthorRepository;
import vbee.bookcmsbackend.utils.SlugUtils;

@Service
public class AuthorService implements IAuthorService {

	@Autowired
	AuthorRepository authorRepository;
	
	@Autowired
	IAuthorDao authorDao;

	@Autowired
	IAuthorizationService authorizationService;

	@Override
	public List<Author> findByAuthorIds(List<String> authorIds) {
		return authorRepository.findByAuthorIds(authorIds);
	}

	@Override
	public Author findById(String authorId) {
		Optional<Author> optional = authorRepository.findById(authorId);
		if (optional.isPresent())
			return optional.get();
		return null;
	}
	
	@Override
	public Item findAll(String keyword, Integer page, Integer size, String fields, String sort, String email,
			String ownerEmail) {
		if (ownerEmail == null || ownerEmail.isEmpty())
			return null;
		Integer permission = authorizationService.checkPermission(email, APIConstant.FIND_AUTHOR_FEATURE_API);
		if (permission == AppConstant.PERMISSION_UNDEFINED)
			return null;
		else if (permission == AppConstant.PERMISSION_ALL_UNIT)
			email = null;
		return authorDao.findAll(keyword, page, size, fields, sort, email, ownerEmail);
	}

	@Override
	public Author findById(String authorId, String email, String ownerEmail) {
		if (ownerEmail == null || ownerEmail.isEmpty())
			return null;
		Integer permission = authorizationService.checkPermission(email, APIConstant.FIND_AUTHOR_FEATURE_API);
		if (permission == AppConstant.PERMISSION_UNDEFINED)
			return null;
		else if (permission == AppConstant.PERMISSION_ALL_UNIT) {
			email = null;
		}
		return authorDao.findById(authorId, email, ownerEmail);
	}

	@Override
	public Object create(Author newAuthor, String email, String ownerEmail) {
		if (ownerEmail == null || ownerEmail.isEmpty())
			return null;
		Integer permission = authorizationService.checkPermission(email, APIConstant.CREATE_AUTHOR_FEATURE_API);
		if (permission == AppConstant.PERMISSION_UNDEFINED)
			return null;
		if (newAuthor.getName() == null || newAuthor.getName().isEmpty()) {
			return "Tên tác giả không được để trống";
		}
		Author authorExist = authorRepository.findByNameAndOwnerBy(newAuthor.getName(), ownerEmail);
		if (authorExist != null)
			return "Tác giả đã tồn tại";
		return createAuthor(newAuthor, email, ownerEmail);
	}

	private Author createAuthor(Author newAuthor, String email, String ownerEmail) {
		newAuthor.setCreatedAt(new Date());
//     	newAuthor.setBirthDate(new Inte);
		newAuthor.setCreatedBy(email);
		newAuthor.setOwnerBy(ownerEmail);
		newAuthor = authorRepository.save(newAuthor);
		String slug = SlugUtils.makeSlugByNameAndObjectId(newAuthor.getName(), newAuthor.getId());
		newAuthor.setSlug(slug);
		return authorRepository.save(newAuthor);
	}

	@Override
	public Object delete(String authorId, String email, String ownerEmail) {
		if (ownerEmail == null || ownerEmail.isEmpty())
			return null;
		Integer permission = authorizationService.checkPermission(email, APIConstant.DELETE_AUTHOR_FEATURE_API);
		if (permission == AppConstant.PERMISSION_UNDEFINED)
			return null;
		else if (permission == AppConstant.PERMISSION_ALL_UNIT) {
			email = null;
		}
		Author authorExist = authorDao.findById(authorId, email, ownerEmail);
		if (authorExist == null)
			return null;
		authorRepository.delete(authorExist);
		return Boolean.TRUE;
	}

	@Override
	public Object update(String authorId, Author author, String email, String ownerEmail) {
		if (ownerEmail == null || ownerEmail.isEmpty())
			return null;
		String tempEmail = email;
		Integer permission = authorizationService.checkPermission(email, APIConstant.UPDATE_AUTHOR_FEATURE_API);
		if (permission == AppConstant.PERMISSION_UNDEFINED)
			return null;
		else if (permission == AppConstant.PERMISSION_ALL_UNIT) {
			tempEmail = null;
		}
		Author authorExist = authorDao.findById(authorId, email, ownerEmail);
		if (authorExist == null)
			return null;
		return updateAuthor(author, authorExist, email);
	}

	private Object updateAuthor(Author author, Author authorExist, String email) {
		if (author.getName() != null && !author.getName().isEmpty()
				&& !author.getName().equals(authorExist.getName())) {
			Author authorCheck = authorRepository.findByNameAndOwnerBy(author.getName(),
					authorExist.getOwnerBy());
			if (authorCheck != null)
				return "Tên tác giả đã tồn tại. Vui lòng thử lại";
			authorExist.setName(author.getName());
		}
		if (author.getDescription() != null && !author.getDescription().isEmpty())
			authorExist.setDescription(author.getDescription());
		author.setUpdatedAt(new Date());
		author.setUpdatedBy(email);
		return authorRepository.save(authorExist);
	}

}
