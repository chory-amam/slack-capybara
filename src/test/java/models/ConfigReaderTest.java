package models;

import com.google.common.base.Strings;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

@RunWith(JMockit.class)
public class ConfigReaderTest {
    @Test
    public void port取得テスト() {
        final ConfigReader reader = ConfigReader.getInstance();
        assertThat(reader.getPort(), is(8080));
    }

    @Test
    public void basicIcon取得テスト() {
        final ConfigReader reader = ConfigReader.getInstance();
        assertThat(reader.getBasicIcon(), is(":grinning:"));
    }

    @Test
    public void happyIcon取得テスト() {
        final ConfigReader reader = ConfigReader.getInstance();
        assertThat(reader.getHappyIcon(), is(":smiley:"));
    }

    @Test
    public void angryIcon取得テスト() {
        final ConfigReader reader = ConfigReader.getInstance();
        assertThat(reader.getAngryIcon(), is(":rage4:"));
    }

    @Test
    public void sadIcon取得テスト() {
        final ConfigReader reader = ConfigReader.getInstance();
        assertThat(reader.getSadIcon(), is(":cry:"));
    }

    @Test
    public void OtherIcon取得テスト() {
        final ConfigReader reader = ConfigReader.getInstance();
        final List<String> actualResult = reader.getOtherIcon();
        assertFalse(actualResult.isEmpty());
        assertThat(reader.getOtherIcon().size(), is(3));
    }

    @Test
    public void OneOtherIcon取得テスト() {
        final ConfigReader reader = ConfigReader.getInstance();
        assertFalse(Strings.isNullOrEmpty(reader.getOneOthericon()));
    }

    @Test
    public void データベースURIの取得テスト() {
        final ConfigReader reader = ConfigReader.getInstance();
        assertThat(reader.getDatabaseURI(), is("jdbc:h2:./db/h2/slack_capybara"));
    }

    @Test
    public void データベースIDの取得テスト() {
        final ConfigReader reader = ConfigReader.getInstance();
        assertThat(reader.getDatabaseId(), is("capybara"));
    }

    @Test
    public void データベースpasswordの取得テスト() {
        final ConfigReader reader = ConfigReader.getInstance();
        assertThat(reader.getDatabasePassword(), is("password"));
    }

    @Test
    public void SlackTeamの取得テスト() {
        final ConfigReader reader = ConfigReader.getInstance();
        assertThat(reader.getSlackTeam(), is(""));
    }

    @Test
    public void SlackUserNameの取得テスト() {
        final ConfigReader reader = ConfigReader.getInstance();
        assertThat(reader.getSlackUserName(), is(""));
    }

    @Test
    public void SlackUserPasswordの取得テスト() {
        final ConfigReader reader = ConfigReader.getInstance();
        assertThat(reader.getSlackPassword(), is(""));
    }

    @Test
    public void SlackRoomの取得テスト() {
        final ConfigReader reader = ConfigReader.getInstance();
        assertThat(reader.getSlackRoom(), is(""));
    }

    @Test
    public void googleMapApiKeyの取得テスト() {
        final ConfigReader reader = ConfigReader.getInstance();
        assertThat(reader.getGoogleMapApiKey(), is(""));
    }

    @Test
    public void googleMapLanguageの取得テスト() {
        final ConfigReader reader = ConfigReader.getInstance();
        assertThat(reader.getGoogleMapLanguage(), is("jp"));
    }

    @Test
    public void googleMapSensorの取得テスト() {
        final ConfigReader reader = ConfigReader.getInstance();
        assertThat(reader.getGoogleMapSensor(), is("false"));
    }

    @Test
    public void googleMapScaleの取得テスト() {
        final ConfigReader reader = ConfigReader.getInstance();
        assertThat(reader.getGoogleMapScale(), is("1"));
    }

    @Test
    public void googleMapZoomの取得テスト() {
        final ConfigReader reader = ConfigReader.getInstance();
        assertThat(reader.getGoogleMapZoom(), is("16"));
    }

    @Test
    public void mapの横幅の取得テスト() {
        final ConfigReader reader = ConfigReader.getInstance();
        assertThat(reader.getGoogleMapSizeLength(), is("500"));
    }

    @Test
    public void mapの縦幅の取得テスト() {
        final ConfigReader reader = ConfigReader.getInstance();
        assertThat(reader.getGoogleMapSizeHeight(), is("500"));
    }
}
