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
public class BizwdHandlers implements BotanMessageHandlers {
	private static Logger log = LoggerFactory.getLogger(BizwdHandlers.class);
	private static String BASE_SCHEME = "http";
	private static String BASE_HOST = "bizwd.net";
	private static String BASE_PATH = "/";
	private static String ERROR_PREFIX = "bizwd request failed : ";

	@Override
	public void register(Robot robot) {
		robot.respond(
				"(bizwd|b) (?<body>.+)",
				"brush up your text by bizwd",
				message -> {
					final String word = message.getMatcher().group("body");
					try {
						final String bizwdWord = bizwd(word);
						log.info("bizwd : ", bizwdWord);
						message.reply(bizwdWord);
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
	 * 入力された言葉をビジネッシュ化して返す
	 * @param word word
	 * @return bizwd
	 * @throws URISyntaxException URISyntaxException
	 * @throws IOException IOException
	 */
	private static String bizwd(final String word) throws URISyntaxException, IOException {
		final String url = new URIBuilder()
				.setScheme(BASE_SCHEME)
				.setHost(BASE_HOST)
				.setPath(BASE_PATH).build().toString();

		final Document document = Jsoup.connect(url)
				.data("before", word)
				.data("after", word)
				.data("level", "2")
				.data("transbtn", "翻訳")
				.post();
		log.info(document.toString());
		return document.select("[name=after]").text();
	}
}
