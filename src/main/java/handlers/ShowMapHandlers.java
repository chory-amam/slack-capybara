package handlers;

import com.github.masahitojp.botan.Robot;
import com.github.masahitojp.botan.handler.BotanMessageHandlers;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
import models.ConfigReader;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;

public class ShowMapHandlers implements BotanMessageHandlers {
    private static Logger log = LoggerFactory.getLogger(ShowMapHandlers.class);
    private static String BASE_SCHEME = "http";
    private static String BASE_HOST = "maps.googleapis.com";
    private static String BASE_PATH = "/maps/api/staticmap";

    @Override
    public void register(Robot robot) {
        robot.respond(
                "map (?<body>.+)",
                "show google map from input address",
                message -> {
                    final String address = message.getMatcher().group("body");
                    try {
                        final String url = buildGoogleMapURL(address);
                        log.info("show google static map. url:" + url);
                        message.reply(url);
                    } catch (final URISyntaxException e) {
                        log.warn("URISyntax exception.", e);
                        message.reply("Unexpected Error!! Check log!!");
                    }
                }
        );
    }

    @VisibleForTesting
    public static String buildGoogleMapURL(final String address) throws URISyntaxException {
        final ConfigReader config = ConfigReader.getInstance();
        final URIBuilder builder = new URIBuilder();
        builder.setScheme(BASE_SCHEME)
                .setHost(BASE_HOST)
                .setPath(BASE_PATH)
                .addParameter("center", address)
                .addParameter("size", config.getGoogleMapSizeLength() + "x" + config.getGoogleMapSizeHeight())
                .addParameter("scale", config.getGoogleMapScale())
                .addParameter("sensor", config.getGoogleMapSensor())
                .addParameter("zoom", config.getGoogleMapZoom())
                .addParameter("language", config.getGoogleMapLanguage())
                .addParameter("markers", address);
        if (!Strings.isNullOrEmpty(config.getGoogleMapApiKey()))
            builder.addParameter("key", config.getGoogleMapApiKey());
        return builder.build().toString();
    }
}
