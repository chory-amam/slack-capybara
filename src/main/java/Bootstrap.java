import com.google.common.base.Charsets;
import com.google.common.io.Files;
import controllers.CapybaraController;
import models.Database;
import ninja.siden.App;
import ninja.siden.Stoppable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

public class Bootstrap {
	private static Logger log = LoggerFactory.getLogger(Bootstrap.class);
	private Optional<Stoppable> stoppable = Optional.empty();

	public Bootstrap() {
	}

	public Stoppable startUp(final int port) {
		log.info("slack-capybara start.");

		// DB
		Database.initialize();

		// Webサーバーの起動
		final App app = new App();
		new CapybaraController(app).defineRoutes();
		return app.listen(port);
	}

	public void stop() {
		log.info("slack-capybara stop");
		if(stoppable.isPresent()) {
			stoppable.get().stop();
		}
	}

	public static void main(final String[] args) {
		final Bootstrap bootstrap = new Bootstrap();
		final int port;
		try {
			@SuppressWarnings("unchecked")
			final Map<String, Object> yaml = (Map<String, Object>)new Yaml().load(Files.newReader(getSettingsFile(), Charsets.UTF_8));
			port = (Integer)yaml.get("port");
		} catch (final FileNotFoundException e) {
			log.warn("Settings file is not exist.", e);
			throw new RuntimeException();
		}

		final Stoppable stoppable = bootstrap.startUp(port);
		bootstrap.stoppable = Optional.of(stoppable);
		java.lang.Runtime.getRuntime().addShutdownHook(
				new Thread() {
					@Override
					public void run() {
						bootstrap.stop();
					}
				}
		);
	}

	private static File getSettingsFile() {
		final Path settingsFile = Paths.get("conf/capybara.yaml");
		if (java.nio.file.Files.notExists(settingsFile)) {
			return new File("src/main/resources/capybara.yaml");
		}
		return settingsFile.toFile();
	}
}
