package vbee.bookcmsbackend.collections;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Document(collection = "features")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Feature {
	@Id
	String id;
	@Indexed
	String frontendKey;
	String displayName;
	@Indexed
	String backendKey;
	boolean isAll;
	
	Date createdAt;
	Date updatedAt;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFrontendKey() {
		return frontendKey;
	}

	public void setFrontendKey(String frontendKey) {
		this.frontendKey = frontendKey;
	}

	public String getBackendKey() {
		return backendKey;
	}

	public void setBackendKey(String backendKey) {
		this.backendKey = backendKey;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean isAll() {
		return isAll;
	}

	public void setAll(boolean isAll) {
		this.isAll = isAll;
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
