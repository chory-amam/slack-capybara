
import com.github.masahitojp.botan.Botan;
import com.github.masahitojp.botan.adapter.SlackAdapter;
import com.github.masahitojp.botan.adapter.SlackRTMAdapter;
import com.github.masahitojp.botan.exception.BotanException;
import com.google.common.base.Strings;
import models.ConfigReader;
import models.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class Bootstrap {
	private static Logger log = LoggerFactory.getLogger(Bootstrap.class);
	private Optional<Botan> botStoppable = Optional.empty();

	public Bootstrap() {
	}

	public int startUp() {
		log.info("slack-capybara start.");

		// DB
		Database.initialize();

		// bot env
		final ConfigReader reader = ConfigReader.getInstance();
		if(Strings.isNullOrEmpty(reader.getSlackApiToken())) {
			throw new IllegalArgumentException("slack.api.token is null or empty");
		}
		final Botan botan = new Botan
				.BotanBuilder()
				.setAdapter(new SlackRTMAdapter(reader.getSlackApiToken()))
				.build();
		this.botStoppable = Optional.of(botan);

		return 0;
	}

	public void stop() {
		log.info("slack-capybara stop");
		botStoppable.ifPresent(botan -> {
			try {
				botan.stop();
			} catch (final Exception e) {
				log.warn("", e);
			}
		});
		Database.dispose();
	}

	public static void main(final String[] args) {
		final Bootstrap bootstrap = new Bootstrap();
		bootstrap.startUp();

		java.lang.Runtime.getRuntime().addShutdownHook(
				new Thread() {
					@Override
					public void run() {
						bootstrap.stop();
					}
				}
		);

		bootstrap.botStoppable.ifPresent(botan -> {
			try {
				botan.start();
			} catch (final BotanException e) {
				log.warn("", e);
			}
		});
	}

}
