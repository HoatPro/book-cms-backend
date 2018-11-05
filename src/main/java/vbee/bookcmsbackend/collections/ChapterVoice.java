package vbee.bookcmsbackend.collections;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Document(collection = "chapterVoices")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ChapterVoice {

	@Id
	String id;
	@Indexed
	String chapterId;
	@Indexed
	String voiceId;
	@Indexed
	String bookId;
	String link;
	Boolean isSuccess;
	@Indexed
	String synthesisId;

	Date createdAt;
	Date updatedAt;

	public ChapterVoice() {
	}

	public ChapterVoice(String chapterId, String voiceId, String bookId, String link) {
		this.chapterId = chapterId;
		this.voiceId = voiceId;
		this.bookId = bookId;
		this.link = link;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChapterId() {
		return chapterId;
	}

	public void setChapterId(String chapterId) {
		this.chapterId = chapterId;
	}

	public String getVoiceId() {
		return voiceId;
	}

	public void setVoiceId(String voiceId) {
		this.voiceId = voiceId;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Boolean getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getSynthesisId() {
		return synthesisId;
	}

	public void setSynthesisId(String synthesisId) {
		this.synthesisId = synthesisId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

}
