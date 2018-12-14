package vbee.bookcmsbackend.services.users_and_roles;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vbee.bookcmsbackend.authorization.IAuthorizationService;
import vbee.bookcmsbackend.collections.Page;
import vbee.bookcmsbackend.config.APIConstant;
import vbee.bookcmsbackend.daos.IPageDao;

import vbee.bookcmsbackend.repositories.PageRepository;

@Service

public class PageService implements IPageService {
	@Autowired
	PageRepository pageRepository;

	@Autowired
	IAuthorizationService authorizationService;

	@Autowired
	IPageDao pageDao;

	@Override
	public List<Page> findAll(String email, String ownerBy) {
//		if (ownerEmail == null || ownerEmail.isEmpty())
//			return null;
//		Integer permission = authorizationService.checkPermission(email, APIConstant.ROLE_USER_FEATURE_API);
//		if (permission == AppConstant.PERMISSION_UNDEFINED)
//			return null;
//		else if (permission == AppConstant.PERMISSION_ALL_UNIT)
//			email = null;
		return pageDao.findAll(email, ownerBy);
	}

	@Override
	public Page findById(String pageId) {
		Optional<Page> optional = pageRepository.findById(pageId);
		if (optional.isPresent())
			return optional.get();
		return null;
	}

	@Override
	public Object create(Page newPage) {
		if (newPage.getDisplayName() == null || newPage.getDisplayName().isEmpty()) {
			return " Tên Page không được để trống!! ";
		}
		Page pageExist = pageRepository.findByDisplayName(newPage.getDisplayName());
		if (pageExist != null)
			return "Page đã tồn tại";
		if (newPage.getKey() == null || newPage.getKey().isEmpty()) {
			return " Tên key Page không được để trống!! ";
		}
		if (newPage.getFeatureIds() == null || newPage.getFeatureIds().isEmpty()) {
			return " Danh sách Feature của Page không được để trống!! ";
		}
		newPage.setCreatedAt(new Date());
		return pageRepository.save(newPage);
	}

	@Override
	public Object update(String pageId, Page page) {
		Page pageExist = pageDao.findById(pageId);
		if (pageExist == null)
			return null;
		return updatePage(page, pageExist);
	}

	private Object updatePage(Page page, Page pageExist) {
		if (page.getDisplayName() != null && !page.getDisplayName().isEmpty()
				&& !page.getDisplayName().equals(pageExist.getDisplayName())) {
			Page pageCheck = pageRepository.findByDisplayName(page.getDisplayName());
			if (pageCheck != null)
				return "Tên Page đã tồn tại. Vui lòng thử lại";
			pageExist.setDisplayName(page.getDisplayName());
		}
		if (page.getKey() != null && !page.getKey().isEmpty()) {
			pageExist.setKey(page.getKey());
		}
		if (page.getFeatureIds() != null && !page.getFeatureIds().isEmpty()) {
			pageExist.setFeatureIds(page.getFeatureIds());
		}
		page.setUpdatedAt(new Date());
		return pageRepository.save(pageExist);
	}

	@Override
	public Object delete(String pageId) {
		Page pageExist = pageDao.findById(pageId);
		if (pageExist == null)
			return null;
		pageRepository.delete(pageExist);
		return Boolean.TRUE;
	}

}
