import contollers.CapybaraController;
import models.Database;
import ninja.siden.App;
import ninja.siden.Stoppable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class Bootstrap {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	private Optional<Stoppable> stoppable = Optional.empty();

	private static int PORT = 8080;

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
		final Stoppable stoppable = bootstrap.startUp(PORT);
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
