import controllers.CapybaraController;
import controllers.SampleServlet;
import models.ConfigReader;
import models.Database;
import ninja.siden.App;
import ninja.siden.Stoppable;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class Bootstrap {
	private static Logger log = LoggerFactory.getLogger(Bootstrap.class);
	private Optional<Server> stoppable = Optional.empty();

	public Bootstrap() {
	}

	public Server startUp(final int port) {
		log.info("slack-capybara start.");

		// DB
		Database.initialize();

		// Webサーバーの起動
		ServletHandler handler = new ServletHandler();
		handler.addServletWithMapping(SampleServlet.class, "/capybara/");

		Server server = new Server(port);
		server.setHandler(handler);
		return server;
	}

	public void stop() {
		log.info("slack-capybara stop");
		if(stoppable.isPresent()) {
			try {
				stoppable.get().stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(final String[] args) {
		final Bootstrap bootstrap = new Bootstrap();
		final ConfigReader reader = ConfigReader.getInstance();
		final int port = reader.getPort();

		final Server stoppable = bootstrap.startUp(port);
		bootstrap.stoppable = Optional.of(stoppable);

		java.lang.Runtime.getRuntime().addShutdownHook(
				new Thread() {
					@Override
					public void run() {
						bootstrap.stop();
					}
				}
		);

		try {
			stoppable.start();
			stoppable.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
