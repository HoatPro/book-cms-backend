package vbee.bookcmsbackend.collections;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Document(collection = "books")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Book {
	@Id
	String id;
	String title;
	String description;
	String image;
	Integer publicYear;
	Integer publicMonth;
	String publishingCompany;
	String translator;
	List<String> categoryIds;
	List<String> authorIds;
	Integer pageNumber;
	@Indexed
	String fileName;
	@Indexed
	String slug;
	Integer statusId;

	String ownerBy;

	String createdBy;
	Date createdAt;

	String updatedBy;
	Date updatedAt;

	// not save to db
	@Transient
	List<Category> categories;
	@Transient
	List<Author> authors;
	@Transient
	List<Chapter> chapters;
	@Transient
	Status status;

	public Book() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Integer getPublicYear() {
		return publicYear;
	}

	public void setPublicYear(Integer publicYear) {
		this.publicYear = publicYear;
	}

	public Integer getPublicMonth() {
		return publicMonth;
	}

	public void setPublicMonth(Integer publicMonth) {
		this.publicMonth = publicMonth;
	}

	public String getPublishingCompany() {
		return publishingCompany;
	}

	public void setPublishingCompany(String publishingCompany) {
		this.publishingCompany = publishingCompany;
	}

	public String getTranslator() {
		return translator;
	}

	public void setTranslator(String translator) {
		this.translator = translator;
	}

	public List<String> getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(List<String> categoryIds) {
		this.categoryIds = categoryIds;
	}

	public List<String> getAuthorIds() {
		return authorIds;
	}

	public void setAuthorIds(List<String> authorIds) {
		this.authorIds = authorIds;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getOwnerBy() {
		return ownerBy;
	}

	public void setOwnerBy(String ownerBy) {
		this.ownerBy = ownerBy;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<Chapter> getChapters() {
		return chapters;
	}

	public void setChapters(List<Chapter> chapters) {
		this.chapters = chapters;
	}

}
