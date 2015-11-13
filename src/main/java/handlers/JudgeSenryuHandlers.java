package handlers;

import com.github.masahitojp.botan.Robot;
import com.github.masahitojp.botan.handler.BotanMessageHandlers;
import com.github.masahitojp.nineteen.Reviewer;
import com.github.masahitojp.nineteen.Song;
import com.github.masahitojp.nineteen.Token;
import com.google.common.base.Strings;

import java.util.Optional;
import java.util.stream.Collectors;


@SuppressWarnings("unused")
public class JudgeSenryuHandlers implements BotanMessageHandlers {

    public final String toSenryuString(final Optional<Song> songOpt) {
        return songOpt.map(song -> song.getPhrases().stream()
                .map(list -> list.stream().map(Token::toString).collect(Collectors.joining()))
                .collect(Collectors.joining(" "))).orElse("");
    }

    @Override
    public void register(final Robot robot) {
        robot.hear(
                "(?<body>.+)",
                "koko de ikku",
                message -> {
                    final String body = message.getBody();
                    if (!Strings.isNullOrEmpty(body)) {
                        final String result = toSenryuString(new Reviewer().find(body));
                        if(!Strings.isNullOrEmpty(result)) {
                            message.reply("ここで一句: " + result);
                        }
                    }
                });
    }


}
