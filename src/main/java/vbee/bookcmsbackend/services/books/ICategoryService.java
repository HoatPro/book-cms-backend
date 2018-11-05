package vbee.bookcmsbackend.services.books;

import java.util.List;

import vbee.bookcmsbackend.collections.Category;
import vbee.bookcmsbackend.models.Item;

public interface ICategoryService {

	List<Category> findByCategoryIds(List<String> categoryIds);

	Category findById(String categoryId);

	Item findAll(String keyword, Integer page, Integer size, String fields, String sort, String email, String ownerBy);

	Category findById(String categoryId, String email, String ownerBy);

	Object create(Category newCategory, String email, String ownerBy);

	Object delete(String categoryId, String email, String ownerBy);

	Object update(String categoryId, Category category, String email, String ownerBy);

}
