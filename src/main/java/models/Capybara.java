package models;

import com.google.common.annotations.VisibleForTesting;

public class Capybara {
    String word;

    @VisibleForTesting
    public static String choiceIcon(final int iconNum) {
        final ConfigReader reader = ConfigReader.getInstance();
        switch (iconNum) {
            case 0:
                return reader.getBasicIcon();
            case 1:
                return reader.getHappyIcon();
            case 2:
                return reader.getAngryIcon();
            case 3:
                return reader.getSadIcon();
            case 4:
                return reader.getOneOthericon();
            default: // note: 念のため
                return reader.getBasicIcon();
        }
    }

    /**
     * 0～4の整数をランダムで生成
     * @return 0~４のどれかの整数
     */
    @VisibleForTesting
    public static int getIconNumber() {
        return (int) (Math.random() * 5);
    }

    public Capybara() {
        this.word = choiceIcon(getIconNumber()) + " < " + Database.pickSentence();
    }

    public String getWord() {
        return word;
    }
}
