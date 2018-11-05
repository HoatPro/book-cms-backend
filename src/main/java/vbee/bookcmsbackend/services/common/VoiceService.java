package vbee.bookcmsbackend.services.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vbee.bookcmsbackend.collections.Author;
import vbee.bookcmsbackend.collections.Voice;
import vbee.bookcmsbackend.repositories.VoiceRepository;

@Service
public class VoiceService implements IVoiceService {

	@Autowired
	VoiceRepository voiceRepository;

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
		voices.add(new Voice("Thùy Linh (Nữ HN)", "hn_female_thutrang_phrase_48k-hsmm"));
		voices.add(new Voice("Mạnh Dũng (Nam HN)", "hn_male_xuantin_vdts_48k-hsmm"));
		voices.add(new Voice("Mai Phương (Nữ HN)", "hn_female_xuanthu_news_48k-hsmm"));
		voices.add(new Voice("Nhất Nam (Nam SG)", "sg_male_xuankien_vdts_48k-hsmm"));
		voices.add(new Voice("Lan Trinh (Nữ SG)", "sg_female_xuanhong_vdts_48k-hsmm"));
		for (Voice voice : voices) {
			Voice voiceExist = findByValue(voice.getValue());
			if (voiceExist == null) {
				voiceRepository.save(voice);
			}
		}

	}

}
