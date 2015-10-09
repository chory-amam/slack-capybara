package models;

import com.google.common.base.Strings;
import models.word.BeginWord;
import models.word.Relation;
import models.word.RelationQueries;
import org.h2.jdbcx.JdbcConnectionPool;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Database {
	private static Logger log = LoggerFactory.getLogger(Database.class);
	private static int MAX_PERIOD = 2;
	private static int MIN_WORD_COUNT = 3;
	private static AtomicReference<JdbcConnectionPool> ds = new AtomicReference<>();
	/**
	 * データベースの初期化
	 */
	public static void initialize() {
		log.info("database initialize.");

		ds.set(createConnection());
		final DBI dbi = new DBI(ds.get());

		final RelationQueries relation = dbi.open(RelationQueries.class);
		relation.createRelationTable();
		relation.close();

		final BeginWord beginWord = dbi.open(BeginWord.class);
		beginWord.createBeginWordTable();
		beginWord.close();

		log.info("database initialized.");
	}

	public static void dispose() {
		ds.get().dispose();
	}

	/**
	 * 引数sentenceを解析し、文言にわけて、記録する
	 */
	public static void study(final String sentence) {
		log.info("study start. sentence: " + sentence);
		final String[] lines = WordAnalyzer.splitBySentenceEnd(sentence);
		
		try {
			for (final String line : lines) {
				// 文章を解析して、単語ごとにスペースで区切る
				if (!Strings.isNullOrEmpty(line)) { // note: lineがnull,空文字の時は学習しない
					final List<String> words = WordAnalyzer.analyze(line);
					String preWord = null;
					int count = 1;
					for (final String word : words) {
						if (count == 1) {
							if (!isExistBeginWord(word, ds.get())) {
								insertBeginWord(word, ds.get());
							}
						} else {
							final boolean isLast = (words.size() == count);
							if (!isExistRelation(preWord, word, isLast, ds.get())) {
								insertRelation(preWord, word, isLast, ds.get());
							}
						}
						++count;
						preWord = word;
					}
				}
			}
			log.info("study end");
		} finally {
			//ds.dispose();
		}
	}

	/**
	 * 発言内容を生成する
	 */
	public static String pickSentence() {
		log.info("pickSentence start");
		String words = "";
		int periodCount = 0;
		int loopCount = 0;

		try {
			while (true) {
				// はじめの言葉を取得
				words += selectBeginWord(ds.get());

				String word = words;
				// 文言が英数記号のみならスペース追加
				if (WordAnalyzer.isAllHalfNumericAndSymbols(words)) {
					words += " ";
				}

				int wordCount = 1;
				for (int i = 0; i < 15; ++i) {
					final Relation relation = selectRelation(word, ds.get());
					if (relation == null) {
						periodCount += MAX_PERIOD;
						break;
					}

					final String nextWord = relation.followWord;
					word = relation.followWord;

					if (WordAnalyzer.isContainsPeriodWord(word)) ++periodCount;

					words += nextWord;
					++wordCount;

					if (periodCount >= MAX_PERIOD) break;
				}

				if (periodCount >= MAX_PERIOD) break;

				if (wordCount < MIN_WORD_COUNT || loopCount < 5) {
					words = "";
					++loopCount;
				} else {
					break;
				}
			}

			// 半角英数字のみは許さない
			if (WordAnalyzer.isAllHalfNumericAndSymbols(words)) {
				return pickSentence();
			} else {
				log.info("pickSentence end");
				return words;
			}
		} finally {
			//ds.dispose();
		}
	}

	/**
	 * データベースへの接続を確立する
	 */
	private static JdbcConnectionPool createConnection() {
		final ConfigReader reader = ConfigReader.getInstance();
		return JdbcConnectionPool.create(reader.getDatabaseURI(), reader.getDatabaseId(), reader.getDatabasePassword());
	}

	/**
	 * begin wordテーブルにすでに入っている文字か調べる
	 */
	private static boolean isExistBeginWord(final String word, final JdbcConnectionPool ds) {
		final DBI dbi = new DBI(ds);
		final BeginWord beginWord = dbi.open(BeginWord.class);
		final int result = beginWord.beginWordCount(word);
		beginWord.close();

		return result > 0;
	}

	/**
	 * begin wordテーブルに挿入する
	 */
	private static void insertBeginWord(final String word, final JdbcConnectionPool ds) {
		final DBI dbi = new DBI(ds);
		final BeginWord beginWord = dbi.open(BeginWord.class);
		beginWord.beginWordInsert(word);
		beginWord.close();
	}

	/**
	 * 最初の言葉を取得する
	 */
	private static String selectBeginWord(final JdbcConnectionPool ds) {
		final DBI dbi = new DBI(ds);
		final BeginWord beginWord = dbi.open(BeginWord.class);
		final String result = beginWord.beginWordSelectByRandom();
		beginWord.close();

		return result;
	}

	/**
	 * Relationテーブルにすでに入っている文字か調べる
	 */
	private static boolean isExistRelation(final String leadWord, final String followWord, final boolean isLast, final JdbcConnectionPool ds) {
		final DBI dbi = new DBI(ds);
		final RelationQueries relation = dbi.open(RelationQueries.class);
		final boolean result = relation.relationGetOrNull(leadWord, followWord, isLast) != null;
		relation.close();
		return result;
	}

	/**
	 * Relationテーブルに挿入する
	 */
	private static void insertRelation(final String leadWord, final String followWord, final boolean isLast, final JdbcConnectionPool ds) {
		final DBI dbi = new DBI(ds);
		final RelationQueries relation = dbi.open(RelationQueries.class);
		relation.relationInsert(leadWord, followWord, isLast);
		relation.close();
	}

	/**
	 * 続きの言葉を取得する
	 */
	private static Relation selectRelation(final String leadWord, final JdbcConnectionPool ds) {
		final DBI dbi = new DBI(ds);
		final RelationQueries relation = dbi.open(RelationQueries.class);
		final Relation result = relation.relationSelectByRandom(leadWord);
		relation.close();
		return result;
	}
}
