package vbee.bookcmsbackend.services.users_and_roles;

import java.util.List;

import vbee.bookcmsbackend.collections.Page;

public interface IPageService {

	List<Page> findAll(String email, String ownerBy);

	Page findById(String pageId);

	Object create(Page newPage);

	Object update(String pageId, Page existPage);

	Object delete(String pageId);

}
