package vbee.bookcmsbackend.utils;

import org.bson.types.ObjectId;

import vbee.bookcmsbackend.models.Slug;

public class SlugUtils {
	public static String makeSlugObjectId(String name) {
		StringBuilder slug = new StringBuilder();
		slug.append(Slug.makeSlug(name));
		slug.append("-" + new ObjectId());
		return slug.toString();
	}
	
	public static String makeSlugByNameAndObjectId(String name, String id) {
		StringBuilder slug = new StringBuilder();
		slug.append(Slug.makeSlug(name));
		slug.append("-" + id);
		return slug.toString();
	}
}
