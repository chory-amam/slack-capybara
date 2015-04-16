import org.atilika.kuromoji.Tokenizer;
import org.atilika.kuromoji.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bootstrap {
	private static Logger log = LoggerFactory.getLogger(Bootstrap.class);
	public static void main(final String[] args) {
		final Tokenizer tokenizer = Tokenizer.builder().build();
		for (final Token token : tokenizer.tokenize("こんにちは、ぼく、ドラえもんです")) {
			log.info(token.getSurfaceForm() + "\t" + token.getAllFeatures());
		}
	}
}
