package vbee.bookcmsbackend.models;

public class Item {

	Object items;
	Integer totalPages;

	public Item(Object items, Integer totalPages) {
		this.items = items;
		this.totalPages = totalPages;
	}

	public Object getItems() {
		return items;
	}

	public void setItems(Object items) {
		this.items = items;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

}
