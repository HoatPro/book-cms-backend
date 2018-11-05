package vbee.bookcmsbackend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import vbee.bookcmsbackend.services.common.IStatusService;
import vbee.bookcmsbackend.services.common.IVoiceService;
import vbee.bookcmsbackend.services.users_and_roles.IUserService;

@SpringBootApplication
public class BookCmsBackendApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(BookCmsBackendApplication.class);

	@Autowired
	IUserService userService;

	@Autowired
	IStatusService statusService;

	@Autowired
	IVoiceService voiceService;

	public static void main(String[] args) {
		SpringApplication.run(BookCmsBackendApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// load user map feature to mem
		userService.loadAllUserFeatures();
		logger.info("load user map feature done");
		statusService.checkStatusOrCreate();
		voiceService.checkVoiceOrCreate();
		logger.info("load voice and status done");
	}
}
