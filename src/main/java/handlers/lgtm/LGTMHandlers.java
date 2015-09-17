package handlers.lgtm;

import com.github.masahitojp.botan.Robot;
import com.github.masahitojp.botan.handler.BotanMessageHandlers;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

@SuppressWarnings("unused")
public class LGTMHandlers implements BotanMessageHandlers {

    @Override
    public final void register(final Robot robot) {
        robot.respond(
                "lgtm",
                "fetching an image from www.lgtm.in.",
                message -> {
                    final OkHttpClient client = new OkHttpClient();
                    final String url = "http://www.lgtm.in/g";
                    final Request request = new Request.Builder()
                            .url(url)
                            .header("Accept", "application/json")
                            .build();

                    final Response response;
                    try {
                        response = client.newCall(request).execute();
                        final String src = response.body().string();
                        final Gson gson = new Gson();
                        final JsonObject jsonObject = new Gson().fromJson(src, JsonObject.class);
                        final String imageUrl = jsonObject.get("imageUrl").getAsString();
                        message.reply(imageUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                        message.reply(e.getMessage());
                    }
                }
        );
    }
}
