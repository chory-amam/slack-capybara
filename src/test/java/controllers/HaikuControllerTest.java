package controllers;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import models.Capybara;
import models.Database;
import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pojos.HaikuResulotPojo;
import pojos.StudyResultPojo;

import java.util.concurrent.Future;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;


public class HaikuControllerTest {

	@SuppressWarnings("unused")
	@Mocked
	private Database database;

	Server server;
	final ObjectMapper mapper = JsonFactory.create();

	@Before
	public void setUp() throws Exception {
		ServletHandler handler = new ServletHandler();
		handler.addServletWithMapping(HaikuController.class, "/");

		server = new Server(8080);
		server.setHandler(handler);
		server.start();
	}

	@After
	public void tearDown() throws Exception {
		if (server != null) {
			server.stop();
		}
	}

	@Test
	public void 俳句判定テスト成功() throws Exception {

		final String wordForTest = "五月雨を集めてはやし最上川";

		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		Future<Response> f = asyncHttpClient
				.preparePost("http://localhost:8080/haiku/")
				.setHeader("Content-Length", "" + (wordForTest.getBytes().length))
				.setBody(wordForTest.getBytes("UTF-8"))
				.execute();

		final Response response = f.get();
		final String str = response.getResponseBody();
		final HaikuResulotPojo value = mapper.fromJson(str, HaikuResulotPojo.class);

		assertThat(response.getStatusCode(), is(200));
		assertThat(value.hasDiscovered(), is(true));
		assertThat(value.getHaiku(), is("五月雨を 集めてはやし 最上川"));

	}

	@Test
	public void 俳句判定テスト失敗() throws Exception {

		final String wordForTest = "新しい朝が来た希望の朝だ";

		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		Future<Response> f = asyncHttpClient
				.preparePost("http://localhost:8080/haiku/")
				.setHeader("Content-Length", "" + (wordForTest.getBytes().length))
				.setBody(wordForTest.getBytes("UTF-8"))
				.execute();

		final Response response = f.get();
		final String str = response.getResponseBody();
		final HaikuResulotPojo value = mapper.fromJson(str, HaikuResulotPojo.class);

		assertThat(response.getStatusCode(), is(200));
		assertThat(value.hasDiscovered(), is(false));
		assertThat(value.getHaiku(), is(""));

	}
}
