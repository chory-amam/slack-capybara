package handlers;

import com.github.masahitojp.botan.Robot;
import com.github.masahitojp.botan.handler.BotanMessageHandlers;

import java.util.Date;

@SuppressWarnings("unused")
public class DiagnosticsHandlers implements BotanMessageHandlers {

    @Override
    public void register(final Robot robot) {
        robot.respond(
                "(?i)ping\\z",
                "Reply with pong",
                message -> message.reply(message.getBody().substring(message.getBody().length() - 4)
                        .replace("i", "o").replace("I", "O"))
        );

        robot.respond(
                "(?i)time\\z",
                "Reply with current time",
                message -> message.reply(String.format("Server time is: %s", new Date()))
        );
    }
}
