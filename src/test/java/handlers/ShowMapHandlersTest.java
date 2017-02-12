package handlers;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.integration.junit4.JMockit;
import models.ConfigReader;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URISyntaxException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(JMockit.class)
public class ShowMapHandlersTest {

    @SuppressWarnings("unused")
    private ConfigReader reader;

    @Test
    public void 意図したQueryStringが取得できること() throws URISyntaxException {
        final String inputAddress1 = "東京";
        final String expectString1 = "http://maps.googleapis.com/maps/api/staticmap?center=%E6%9D%B1%E4%BA%AC&size=500x500&scale=1&sensor=false&zoom=16&language=jp&markers=%E6%9D%B1%E4%BA%AC";
        final String inputAddress2 = "&&&";
        final String expectString2 = "http://maps.googleapis.com/maps/api/staticmap?center=%26%26%26&size=500x500&scale=1&sensor=false&zoom=16&language=jp&markers=%26%26%26";
        reader = ConfigReader.getInstance();
        new NonStrictExpectations(ConfigReader.class) {
            {
                reader.getGoogleMapApiKey();
                result = "";
                reader.getGoogleMapLanguage();
                result = "jp";
                reader.getGoogleMapSensor();
                result = "false";
                reader.getGoogleMapScale();
                result = "1";
                reader.getGoogleMapZoom();
                result = "16";
                reader.getGoogleMapSizeLength();
                result = "500";
                reader.getGoogleMapSizeHeight();
                result = "500";
            }
        };

        assertThat(ShowMapHandlers.buildGoogleMapURL(inputAddress1), is(expectString1));
        assertThat(ShowMapHandlers.buildGoogleMapURL(inputAddress2), is(expectString2));
    }

}
