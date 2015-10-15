package handlers;

import com.github.masahitojp.botan.Robot;
import com.github.masahitojp.botan.handler.BotanMessageHandlers;

import java.util.Date;

@SuppressWarnings("unused")
public class DiagnosticsHandlers implements BotanMessageHandlers {
    public static String PING_DESCRIPTION = "Reply with pong";
    public static String TIME_DESCRIPTION = "Reply with current time";
    @Override
    public void register(final Robot robot) {
        robot.respond(
                "(?i)ping\\z",
                PING_DESCRIPTION,
                message -> message.reply(message.getBody().substring(message.getBody().length() - 4).replace("i", "o").replace("I", "O"))
        );

        robot.respond(
                "(?i)time\\z",
                TIME_DESCRIPTION,
                message -> message.reply(String.format("Server time is: %s", new Date()))
        );
    }
}
