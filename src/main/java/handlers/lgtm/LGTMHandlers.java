package handlers.lgtm;

import com.github.masahitojp.botan.Robot;
import com.github.masahitojp.botan.handler.BotanMessageHandlers;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@SuppressWarnings("unused")
public class LGTMHandlers implements BotanMessageHandlers {
    private static Logger log = LoggerFactory.getLogger(LGTMHandlers.class);
    private static String ERROR_PREFIX = "LGTM image request failed : ";

    @Override
    public final void register(final Robot robot) {
        robot.respond(
                "(?i)lgtm\\z",
                "fetching an image from www.lgtm.in.",
                message -> {
                    final OkHttpClient client = new OkHttpClient();
                    final String url = "https://www.lgtm.in/g";
                    final Request request = new Request.Builder()
                            .url(url)
                            .header("Accept", "application/json")
                            .build();

                    try {
                        final Response response = client.newCall(request).execute();
                        if (response.isSuccessful()) {
                            final String src = response.body().string();
                            final JsonObject jsonObject = new Gson().fromJson(src, JsonObject.class);
                            final String imageUrl = jsonObject.get("imageUrl").getAsString();
                            message.reply(imageUrl);
                        } else {
                            message.reply(ERROR_PREFIX + String.format("http request failed (%d)", response.code()));
                        }
                    } catch (IOException e) {
                        log.warn(e.getMessage());
                        message.reply(ERROR_PREFIX + e.getMessage());
                    }
                }
        );
    }
}
