package handlers.misawa;

import com.github.masahitojp.botan.Robot;
import com.github.masahitojp.botan.handler.BotanMessageHandlers;
import com.github.masahitojp.botan.utils.BotanUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
public class MisawaHandlers implements BotanMessageHandlers {
    private static Logger log = LoggerFactory.getLogger(MisawaHandlers.class);
    private static String ERROR_PREFIX = "JIGOKU-NO-MISAWA image request failed : ";
    @Override
    public void register(final Robot robot) {
        robot.respond(
                "misawa( +(.*))?",
                "returns JIGOKU-NO-MISAWA image",
                message -> {
                    final OkHttpClient client = new OkHttpClient();
                    final String url = "http://horesase.github.io/horesase-boys/meigens.json";
                    final Request request = new Request.Builder()
                            .url(url)
                            .build();

                    try {
                        final Response response = client.newCall(request).execute();
                        if (response.isSuccessful()) {
                            final String src = response.body().string();
                            final Type collectionType = new TypeToken<Collection<Meigen>>() {
                            }.getType();
                            final List<Meigen> rootAsMap = new Gson().fromJson(src, collectionType);
                            message.reply(BotanUtils.getRandomValue(rootAsMap).image);
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
