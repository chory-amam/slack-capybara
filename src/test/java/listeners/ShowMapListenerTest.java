package listeners;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.integration.junit4.JMockit;
import models.ConfigReader;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.UnsupportedEncodingException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(JMockit.class)
public class ShowMapListenerTest {
    @Mocked({"getGoogleMapApiKey"
            , "getGoogleMapLanguage"
            , "getGoogleMapSensor"
            , "getGoogleMapScale"
            , "getGoogleMapZoom"
            , "getGoogleMapSizeLength"
            , "getGoogleMapSizeHeight"})
    @SuppressWarnings("unused")
    private ConfigReader reader;

    @Test
    public void 意図したQueryStringが取得できること() throws UnsupportedEncodingException {
        final String inputAddress1 = "東京";
        final String expectString1 = "?size=500x500&center=%E6%9D%B1%E4%BA%AC&scale=1&sensor=false&zoom=16&language=jp&markers=%E6%9D%B1%E4%BA%AC";
        final String inputAddress2 = "&&&";
        final String expectString2 = "?size=500x500&center=%26%26%26&scale=1&sensor=false&zoom=16&language=jp&markers=%26%26%26";

        new NonStrictExpectations() {
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

        assertThat(expectString1, is(ShowMapListener.getQueryString(inputAddress1)));
        assertThat(expectString2, is(ShowMapListener.getQueryString(inputAddress2)));
    }

}
