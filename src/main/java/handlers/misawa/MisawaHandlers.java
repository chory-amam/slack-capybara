package handlers.misawa;

import com.github.masahitojp.botan.Robot;
import com.github.masahitojp.botan.handler.BotanMessageHandlers;
import com.github.masahitojp.botan.utils.BotanUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
public class MisawaHandlers implements BotanMessageHandlers {

    @Override
    public void register(final Robot robot) {
        robot.respond(
                "misawa( +(.*))?",
                "returns JIGOKU-NO-MISAWA image",
                messge -> {
                    final OkHttpClient client = new OkHttpClient();
                    final String url = "http://horesase.github.io/horesase-boys/meigens.json";
                    final Request request = new Request.Builder()
                            .url(url)
                            .build();

                    final Response response;
                    try {
                        response = client.newCall(request).execute();
                        final String src = response.body().string();
                        final Gson gson = new Gson();
                        final Type collectionType = new TypeToken<Collection<Meigen>>() {
                        }.getType();
                        final List<Meigen> rootAsMap = gson.fromJson(src, collectionType);
                        messge.reply(BotanUtils.getRandomValue(rootAsMap).image);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }
}
