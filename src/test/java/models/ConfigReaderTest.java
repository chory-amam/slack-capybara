package models;

import com.google.common.base.Strings;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

@RunWith(JMockit.class)
public class ConfigReaderTest {
    ConfigReader reader;

    @Before
    public void startUP() {
        reader = ConfigReader.getInstance();
    }
    @Test
    public void port取得テスト() {
        assertThat(reader.getPort(), is(8080));
    }

    @Test
    public void basicIcon取得テスト() {
        assertThat(reader.getBasicIcon(), is(":grinning:"));
    }

    @Test
    public void happyIcon取得テスト() {
        assertThat(reader.getHappyIcon(), is(":smiley:"));
    }

    @Test
    public void angryIcon取得テスト() {
        assertThat(reader.getAngryIcon(), is(":rage4:"));
    }

    @Test
    public void sadIcon取得テスト() {
        assertThat(reader.getSadIcon(), is(":cry:"));
    }

    @Test
    public void OtherIcon取得テスト() {
        final List<String> actualResult = reader.getOtherIcon();
        assertFalse(actualResult.isEmpty());
        assertThat(reader.getOtherIcon().size(), is(3));
    }

    @Test
    public void OneOtherIcon取得テスト() {

        assertFalse(Strings.isNullOrEmpty(reader.getOneOthericon()));
    }

    @Test
    public void データベースURIの取得テスト() {

        assertThat(reader.getDatabaseURI(), is("jdbc:h2:./db/h2/slack_capybara"));
    }

    @Test
    public void データベースIDの取得テスト() {

        assertThat(reader.getDatabaseId(), is("capybara"));
    }

    @Test
    public void データベースpasswordの取得テスト() {

        assertThat(reader.getDatabasePassword(), is("password"));
    }

    @Test
    public void KVS_URIの取得テスト() {

        assertThat(reader.getKvsURI(), is("./db/mapdb/slack_capybara"));
    }

    @Test
    public void KVS名の取得テスト() {

        assertThat(reader.getKvsName(), is("slack_capybara"));
    }

    @Test
    public void SlackTeamの取得テスト() {

        assertThat(reader.getSlackTeam(), is(""));
    }

    @Test
    public void SlackUserNameの取得テスト() {

        assertThat(reader.getSlackUserName(), is(""));
    }

    @Test
    public void SlackUserPasswordの取得テスト() {

        assertThat(reader.getSlackPassword(), is(""));
    }

    @Test
    public void SlackRoomの取得テスト() {

        assertThat(reader.getSlackRoom(), is(""));
    }

    @Test
    public void SlackAPITokenの取得テスト() {

        assertThat(reader.getSlackApiToken(), is(""));
    }

    @Test
    public void googleMapApiKeyの取得テスト() {

        assertThat(reader.getGoogleMapApiKey(), is(""));
    }

    @Test
    public void googleMapLanguageの取得テスト() {

        assertThat(reader.getGoogleMapLanguage(), is("jp"));
    }

    @Test
    public void googleMapSensorの取得テスト() {

        assertThat(reader.getGoogleMapSensor(), is("false"));
    }

    @Test
    public void googleMapScaleの取得テスト() {

        assertThat(reader.getGoogleMapScale(), is("1"));
    }

    @Test
    public void googleMapZoomの取得テスト() {

        assertThat(reader.getGoogleMapZoom(), is("16"));
    }

    @Test
    public void mapの横幅の取得テスト() {

        assertThat(reader.getGoogleMapSizeLength(), is("500"));
    }

    @Test
    public void mapの縦幅の取得テスト() {
        assertThat(reader.getGoogleMapSizeHeight(), is("500"));
    }
}
