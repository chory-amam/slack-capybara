package listeners;

import com.github.masahitojp.botan.Robot;
import com.github.masahitojp.botan.listener.BotanMessageListenerRegister;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import models.ConfigReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class ShowMapListener implements BotanMessageListenerRegister {
	private static Logger log = LoggerFactory.getLogger(ShowMapListener.class);
	private static String BASE_URL = "http://maps.googleapis.com/maps/api/staticmap";

	@Override
	public void register(Robot robot) {
		robot.respond(
				"map (?<body>.+)",
				"show google map from input address",
				message -> {
					final String body = message.getMatcher().group("body");
					try {
						final String url = BASE_URL + getQueryString(body);
						log.info("show google static map. url:" + url);
						message.reply(url);
					} catch (final UnsupportedEncodingException e) {
						log.warn("unsupported exception. check programming source.");
						message.reply("Unexpected Error!! Check log!!");
					}
				}
		);
	}

	@VisibleForTesting
	public static String getQueryString(final String address) throws UnsupportedEncodingException {
		final ConfigReader config = ConfigReader.getInstance();
		final Map<String, String> dictionary = Maps.newHashMap();
		final String encodedAddress = URLEncoder.encode(address, "UTF-8");
		dictionary.put("center", encodedAddress);
		dictionary.put("size", config.getGoogleMapSizeLength() + "x" + config.getGoogleMapSizeHeight());
		dictionary.put("sensor", config.getGoogleMapSensor());
		dictionary.put("scale", config.getGoogleMapScale());
		dictionary.put("zoom", config.getGoogleMapZoom());
		dictionary.put("markers", encodedAddress);
		if (!Strings.isNullOrEmpty(config.getGoogleMapApiKey())) {
			dictionary.put("key", config.getGoogleMapApiKey());
		}
		if (!Strings.isNullOrEmpty(config.getGoogleMapLanguage())) {
			dictionary.put("language", config.getGoogleMapLanguage());
		}
		Joiner.MapJoiner joiner = Joiner.on("&").withKeyValueSeparator("=").useForNull("");
		return "?" + joiner.join(dictionary);
	}
}
