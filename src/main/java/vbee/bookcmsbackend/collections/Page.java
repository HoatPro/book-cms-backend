package vbee.bookcmsbackend.collections;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Document(collection = "pages")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Page {
	@Id
	String id;
	String key;
	String displayName;
	List<String> featureIds;

	Date createdAt;
	Date updatedAt;

	public Page() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public List<String> getFeatureIds() {
		return featureIds;
	}

	public void setFeatureIds(List<String> featureIds) {
		this.featureIds = featureIds;
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
