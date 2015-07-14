
import models.ConfigReader;
import models.Database;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.security.ProtectionDomain;
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
		WebAppContext war = new WebAppContext();
		war.setContextPath("/");

		// ここで war ファイルの場所を取得している
		ProtectionDomain domain = Bootstrap.class.getProtectionDomain();
		URL warLocation = domain.getCodeSource().getLocation();
		war.setWar(warLocation.toExternalForm());

		Configuration[] configurations = {
				new AnnotationConfiguration(),
				new WebInfConfiguration(),
				new WebXmlConfiguration(),
				new MetaInfConfiguration(),
				new FragmentConfiguration(),
				new EnvConfiguration(),
				new PlusConfiguration(),
				new JettyWebXmlConfiguration()
		};

		war.setConfigurations(configurations);


		Server server = new Server(port);
		server.setHandler(war);
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
