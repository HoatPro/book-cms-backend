package vbee.bookcmsbackend.utils;

public class HanleUltis {
	public static Integer countTotalPages(Integer totalResults, Integer limit) {
		if (totalResults == null || totalResults == 0) {
			return 0;
		}
		if (totalResults < limit) {
			return 1;
		} else if (totalResults % limit == 0) {
			return totalResults / limit;
		} else {
			return totalResults / limit + 1;
		}
	}
}
