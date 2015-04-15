import org.atilika.kuromoji.Tokenizer;
import org.atilika.kuromoji.Token;

public class Bootstrap {
	public static void main(final String[] args) {
		final Tokenizer tokenizer = Tokenizer.builder().build();
		for (final Token token : tokenizer.tokenize("こんにちは、ぼく、ドラえもんです")) {
			System.out.println(token.getSurfaceForm() + "\t" + token.getAllFeatures());
		}
	}
}
