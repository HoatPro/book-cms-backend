package vbee.bookcmsbackend.collections;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Document (collection = "roles")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Role {

	@Id
	String id;
	String name;
	List<String> featureIds;

	Date createdAt;
	Date updatedAt;

	public Role() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
