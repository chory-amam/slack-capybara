package controllers;

import models.Capybara;
import models.Database;
import ninja.siden.App;
import ninja.siden.Renderer;
import ninja.siden.Request;
import org.boon.json.JsonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.Pooled;
import org.xnio.channels.StreamSourceChannel;
import pojos.StudyResultPojo;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class CapybaraController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private App app;

	public CapybaraController(App app) {
		this.app = app;
	}

	public void defineRoutes() {
		final String JSON_TYPE = "application/json";

		// 言葉を取得す

		app.get("/capybara", (req, res) -> JsonFactory.toJson(new Capybara()));
		// 会話内容を登録
		app.post(
				"/capybara",
				(req, res) -> {
					boolean result = false;
					try {
						final String body = getRequestBody(req);
						// TODO バックエンドでstudyするように変更する
						Database.study(body);
						result = true;
					} catch (IOException e) {
						log.warn("", e);
					}

					return JsonFactory.toJson(new StudyResultPojo(result));
				});
	}

	private  String getRequestBody(Request req) throws IOException {

		final String requestBody;

		final Pooled<ByteBuffer> pooledByteBuffer = req.raw().getConnection().getBufferPool().allocate();
		try {
			final ByteBuffer byteBuffer = pooledByteBuffer.getResource();
			byteBuffer.clear();

			try(final StreamSourceChannel reqChannel = req.raw().getRequestChannel()) {
				reqChannel.read(byteBuffer);
				byteBuffer.flip();

				int limit = byteBuffer.limit();
				byte[] bytes = new byte[limit];
				byteBuffer.get(bytes);

				requestBody = new String(bytes, Charset.forName("UTF-8"));
			} finally {
				byteBuffer.clear();
			}
		} finally {
			pooledByteBuffer.free();
		}

		return requestBody;

	}
}
