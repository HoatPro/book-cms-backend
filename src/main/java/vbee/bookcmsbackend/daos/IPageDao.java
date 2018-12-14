package vbee.bookcmsbackend.daos;

import java.util.List;

import vbee.bookcmsbackend.collections.Page;

public interface IPageDao {

	List<Page> findAll(String email, String ownerBy);

	Page findById(String pageId);

}
