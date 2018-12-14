package vbee.bookcmsbackend.services.books;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vbee.bookcmsbackend.authorization.IAuthorizationService;
import vbee.bookcmsbackend.collections.Category;
import vbee.bookcmsbackend.config.APIConstant;
import vbee.bookcmsbackend.config.AppConstant;
import vbee.bookcmsbackend.daos.ICategoryDao;
import vbee.bookcmsbackend.models.Item;
import vbee.bookcmsbackend.repositories.CategoryRepository;
import vbee.bookcmsbackend.utils.SlugUtils;

@Service
public class CategoryService implements ICategoryService {

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	ICategoryDao categoryDao;

	@Autowired
	IAuthorizationService authorizationService;

	@Override
	public List<Category> findByCategoryIds(List<String> categoryIds) {
		return categoryRepository.findByCategoryIds(categoryIds);
	}

	@Override
	public Category findById(String categoryId) {
		Optional<Category> optional = categoryRepository.findById(categoryId);
		if (optional.isPresent())
			return optional.get();
		return null;
	}

	@Override
	public Item findAll(String keyword, Integer page, Integer size, String fields, String sort, String email,
			String ownerEmail) {
		if (ownerEmail == null || ownerEmail.isEmpty())
			return null;
		Integer permission = authorizationService.checkPermission(email, APIConstant.FIND_CATEGORY_FEATURE_API);
		if (permission == AppConstant.PERMISSION_UNDEFINED)
			return null;
		else if (permission == AppConstant.PERMISSION_ALL_UNIT)
			email = null;
		return categoryDao.findAll(keyword, page, size, fields, sort, email, ownerEmail);
	}

	@Override
	public Category findById(String categoryId, String email, String ownerEmail) {
		if (ownerEmail == null || ownerEmail.isEmpty())
			return null;
		Integer permission = authorizationService.checkPermission(email, APIConstant.FIND_CATEGORY_FEATURE_API);
		if (permission == AppConstant.PERMISSION_UNDEFINED)
			return null;
		else if (permission == AppConstant.PERMISSION_ALL_UNIT) {
			email = null;
		}
		return categoryDao.findById(categoryId, email, ownerEmail);
	}

	@Override
	public Object create(Category newCategory, String email, String ownerEmail) {
		if (ownerEmail == null || ownerEmail.isEmpty())
			return null;
		Integer permission = authorizationService.checkPermission(email, APIConstant.CREATE_CATEGORY_FEATURE_API);
		if (permission == AppConstant.PERMISSION_UNDEFINED)
			return null;
		if (newCategory.getName() == null || newCategory.getName().isEmpty()) {
			return "Tên thể loại không được để trống";
		}
		Category categoryExist = categoryRepository.findByNameAndOwnerBy(newCategory.getName(), ownerEmail);
		if (categoryExist != null)
			return "Thể loại đã tồn tại";
		return createCategory(newCategory, email, ownerEmail);
	}

	private Category createCategory(Category newCategory, String email, String ownerEmail) {
		newCategory.setCreatedAt(new Date());
		newCategory.setCreatedBy(email);
		newCategory.setOwnerBy(ownerEmail);
		newCategory = categoryRepository.save(newCategory);
		String slug = SlugUtils.makeSlugByNameAndObjectId(newCategory.getName(), newCategory.getId());
		newCategory.setSlug(slug);
		return categoryRepository.save(newCategory);
	}

	@Override
	public Object delete(String categoryId, String email, String ownerEmail) {
		if (ownerEmail == null || ownerEmail.isEmpty())
			return null;
		Integer permission = authorizationService.checkPermission(email, APIConstant.DELETE_CATEGORY_FEATURE_API);
		if (permission == AppConstant.PERMISSION_UNDEFINED)
			return null;
		else if (permission == AppConstant.PERMISSION_ALL_UNIT) {
			email = null;
		}
		Category categoryExist = categoryDao.findById(categoryId, email, ownerEmail);
		if (categoryExist == null)
			return null;
		categoryRepository.delete(categoryExist);
		return Boolean.TRUE;
	}

	@Override
	public Object update(String categoryId, Category category, String email, String ownerEmail) {
		if (ownerEmail == null || ownerEmail.isEmpty())
			return null;
		String tempEmail = email;
		Integer permission = authorizationService.checkPermission(email, APIConstant.UPDATE_CATEGORY_FEATURE_API);
		if (permission == AppConstant.PERMISSION_UNDEFINED)
			return null;
		else if (permission == AppConstant.PERMISSION_ALL_UNIT) {
			tempEmail = null;
		}
		Category categoryExist = categoryDao.findById(categoryId, email, ownerEmail);
		if (categoryExist == null)
			return null;
		return updateCategory(category, categoryExist, email);
	}

	private Object updateCategory(Category category, Category categoryExist, String email) {
		if (category.getName() != null && !category.getName().isEmpty()
				&& !category.getName().equals(categoryExist.getName())) {

			Category categoryCheck = categoryRepository.findByNameAndOwnerBy(category.getName(),
					categoryExist.getOwnerBy());
			if (categoryCheck != null)
				return "Tên thể loại đã tồn tại. Vui lòng thử lại";
			categoryExist.setName(category.getName());
		}
		if (category.getDescription() != null && !category.getDescription().isEmpty())
			categoryExist.setDescription(category.getDescription());

		category.setUpdatedAt(new Date());
		category.setUpdatedBy(email);
		return categoryRepository.save(categoryExist);
	}

}
