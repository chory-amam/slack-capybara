import controllers.CapybaraController;
import models.ConfigReader;
import models.Database;
import ninja.siden.App;
import ninja.siden.Stoppable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		final ConfigReader reader = ConfigReader.getInstance();
		final int port = reader.getPort();

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

}
