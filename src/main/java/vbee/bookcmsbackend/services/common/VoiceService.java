package vbee.bookcmsbackend.services.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vbee.bookcmsbackend.collections.Author;
import vbee.bookcmsbackend.collections.Voice;
import vbee.bookcmsbackend.config.APIConstant;
import vbee.bookcmsbackend.config.AppConstant;
import vbee.bookcmsbackend.daos.IRoleDao;
import vbee.bookcmsbackend.daos.IVoiceDao;
import vbee.bookcmsbackend.models.Item;
import vbee.bookcmsbackend.repositories.VoiceRepository;

@Service
public class VoiceService implements IVoiceService {

	@Autowired
	VoiceRepository voiceRepository;
	
	@Autowired
	IVoiceDao voiceDao;

	@Override
	public Voice findByValue(String value) {
		Optional<Voice> optional = voiceRepository.findById(value);
		if (optional.isPresent())
			return optional.get();
		return null;
	}

	@Override
	public void checkVoiceOrCreate() {
		List<Voice> voices = new ArrayList<>();
		voices.add(new Voice("hn_female_thutrang_phrase_48k-hsmm", "Thùy Linh (Nữ HN)"));
		voices.add(new Voice("hn_male_xuantin_vdts_48k-hsmm", "Mạnh Dũng (Nam HN)"));
		voices.add(new Voice("hn_female_xuanthu_news_48k-hsmm", "Mai Phương (Nữ HN)"));
		voices.add(new Voice("sg_male_xuankien_vdts_48k-hsmm", "Nhất Nam (Nam SG)"));
		voices.add(new Voice("sg_female_xuanhong_vdts_48k-hsmm", "Lan Trinh (Nữ SG)"));
		for (Voice voice : voices) {
			Voice voiceExist = findByValue(voice.getValue());
			if (voiceExist == null) {
				voiceRepository.save(voice);
			}
		}

	}

	@Override
	public List<Voice> findAll(String email, String ownerBy) {
		
		return voiceDao.findAll(email, ownerBy);

	}

	@Override
	public List<Voice> findAll() {
		return voiceRepository.findAll();
	}



}
