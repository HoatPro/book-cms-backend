package vbee.bookcmsbackend.collections;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Document(collection = "logSynthesis")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class LogSynthesis {

	@Id
	String id;
	String bookId;
	String chapterId;

	String ownerBy;

	String createdBy;
	Date createdAt;

	public LogSynthesis() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getChapterId() {
		return chapterId;
	}

	public void setChapterId(String chapterId) {
		this.chapterId = chapterId;
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
}
