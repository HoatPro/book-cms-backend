package vbee.bookcmsbackend.services.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vbee.bookcmsbackend.collections.Status;
import vbee.bookcmsbackend.config.AppConstant;
import vbee.bookcmsbackend.repositories.StatusRepository;

@Service
public class StatusService implements IStatusService {

	@Autowired
	StatusRepository statusRepository;

	@Override
	public Status findById(Integer statusId) {
		Optional<Status> optional = statusRepository.findById(statusId);
		if (optional.isPresent())
			return optional.get();
		return null;
	}

	@Override
	public void checkStatusOrCreate() {
		List<Status> statuses = new ArrayList<>();
		statuses.add(new Status(AppConstant.NO_CONTENT, "Chưa có nội dung"));
		statuses.add(new Status(AppConstant.HAS_CONTENT, "Đã có nội dung"));
		statuses.add(new Status(AppConstant.NORMALIZING, "Đang chuẩn hóa"));
		statuses.add(new Status(AppConstant.NORMALIZED, "Đã chuẩn hóa"));
		statuses.add(new Status(AppConstant.READY_SYNTHESIZE, "Sẵn sàng tổng hợp"));
		statuses.add(new Status(AppConstant.SYNTHESIZING, "Đang tổng hợp"));
		statuses.add(new Status(AppConstant.SYNTHESIZED_ERROR, "Tổng hợp lỗi"));
		statuses.add(new Status(AppConstant.SYNTHESIZED_SUCCESS, "Tổng hợp thành công"));
		for (Status status : statuses) {
			Status statusExist = findById(status.getId());
			if(statusExist == null) {
				statusRepository.save(status);
			}
		}
		
	}

}
