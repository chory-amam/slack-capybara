package models.word;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class SymbolConst {
    public static List<String> PERIOD = Lists.newArrayList(
            "。"
            , "！"
            , "!"
            , "？"
            , "?"
            , "."
            , "．"
    );
    public static List<String> SPACE = Lists.newArrayList(
            " "
            , "　"
    );
    public static List<Pair<String, String>> PARENTHESIS = Lists.newArrayList(
            new ImmutablePair<>("「", "」"),
            new ImmutablePair<>("『", "』"),
            new ImmutablePair<>("【", "】"),
            new ImmutablePair<>("［", "］"),
            new ImmutablePair<>("\\[", "\\]"),
            new ImmutablePair<>("\\{", "\\}"),
            new ImmutablePair<>("｛", "｝")
    );
}