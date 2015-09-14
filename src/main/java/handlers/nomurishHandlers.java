package handlers;

import com.github.masahitojp.botan.Robot;
import com.github.masahitojp.botan.handler.BotanMessageHandlers;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;

@SuppressWarnings("unused")
public class nomurishHandlers implements BotanMessageHandlers {
	private static Logger log = LoggerFactory.getLogger(nomurishHandlers.class);
	private static String BASE_SCHEME = "http";
	private static String BASE_HOST = "racing-lagoon.info";
	private static String BASE_PATH = "/nomu/translate.php";
	private static String ERROR_PREFIX = "nomurish request failed : ";

	@Override
	public void register(Robot robot) {
		robot.respond(
				"nomurish (?<body>.+)",
				"brush up your text by nomurish",
				message -> {
					final String word = message.getMatcher().group("body");
					try {
						final String nomurishedWord = nomurish(word);
						log.info("nomurish : ", nomurishedWord);
						message.reply(nomurishedWord);
					} catch (final URISyntaxException e) {
						log.warn("URISyntax exception.", e);
						message.reply(ERROR_PREFIX + e.getMessage());
					} catch (final IOException e) {
						log.warn("IOException.", e);
						message.reply(ERROR_PREFIX + e.getMessage());
					}
				}
		);
	}

	/**
	 * 入力された言葉をノムリッシュ化して返す
	 * @param word word
	 * @return nomurished word
	 * @throws URISyntaxException URISyntaxException
	 * @throws IOException IOException
	 */
	private static String nomurish(final String word) throws URISyntaxException, IOException {
		final String url = new URIBuilder()
				.setScheme(BASE_SCHEME)
				.setHost(BASE_HOST)
				.setPath(BASE_PATH).build().toString();
		final Document document = Jsoup.connect(url)
				.data("before", word)
				.data("level", "1")
				.data("option", "nochk")
				.data("transbtn", "翻訳")
				.post();
		return document.select("[name=after1]").text();
	}
}
