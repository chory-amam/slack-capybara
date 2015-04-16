package models;

import models.word.BeginWord;
import models.word.Relation;
import org.h2.jdbcx.JdbcConnectionPool;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Database {
	private static Logger log = LoggerFactory.getLogger(Database.class);

	/**
	 * データベースの初期化
	 */
	public static void initialize() {
		log.info("database initialize.");
		final JdbcConnectionPool ds = createConnection();
		final DBI dbi = new DBI(ds);

		final Relation relation = dbi.open(Relation.class);
		relation.createRelationTable();
		relation.close();
		log.info("table 'Relation' created.");

		final BeginWord beginWord = dbi.open(BeginWord.class);
		beginWord.createBeginWordTable();
		beginWord.close();
		log.info("table 'begin_word' created.");

		ds.dispose();
		log.info("database initialized.");
	}

	/**
	 * 引数sentenceを解析し、文言にわけて、記録する
	 */
	public static void study(final String sentence) {
		log.info("study start. sentence: " + sentence);
		// 。をすべて改行付の。にして、改行で分割。
		final String[] lines = sentence.replace("/。/", "。¥n").split("¥n");
		for (final String line : lines) {
			// 文章を解析して、単語ごとにスペースで区切る
			final String analyzedLine = WordAnalyzer.analyze(line);

			// 「と」と（と）をけして、スペースで分割
			final String[] words = analyzedLine.replace("/「|」|（|）/", "").split("¥s");

			String preWord = null;
			int count = 1;
			for (final String word : words) {
				if (preWord == null) {
					if (!isExistBeginWord(word)) {
						insertBeginWord(word);
					}
				} else {
					final boolean isLast = (words.length == count);
					if (!isExistRelation(preWord, word, isLast)) {
						insertRelation(preWord, word, isLast);
					}
				}
				++count;
				preWord = word;
			}
		}
		log.info("study end");
	}

	/**
	 * セイ！する
	 */
	public static String say() {
		// do something
		return null;
	}

	/**
	 * データベースへの接続を確立する
	 */
	private static JdbcConnectionPool createConnection() {
		return JdbcConnectionPool.create("jdbc:h2:mem:test", "capybara", "password");
	}

	/**
	 * begin wordテーブルにすでに入っている文字か調べる
	 */
	private static boolean isExistBeginWord(final String word) {
		final JdbcConnectionPool ds = createConnection();
		final DBI dbi = new DBI(ds);
		final BeginWord beginWord = dbi.open(BeginWord.class);
		return beginWord.beginWordCount(word) > 0;
	}

	/**
	 * begin wordテーブルに挿入する
	 */
	private static void insertBeginWord(final String word) {
		final JdbcConnectionPool ds = createConnection();
		final DBI dbi = new DBI(ds);
		final BeginWord beginWord = dbi.open(BeginWord.class);
		beginWord.beginWordInsert(word);
	}

	/**
	 * Relationテーブルにすでに入っている文字か調べる
	 */
	private static boolean isExistRelation(final String leadWord, final String followWord, final boolean isLast) {
		final JdbcConnectionPool ds = createConnection();
		final DBI dbi = new DBI(ds);
		final Relation relation = dbi.open(Relation.class);
		return relation.relationCount(leadWord, followWord, isLast) > 0;
	}

	/**
	 * Relationテーブルに挿入する
	 */
	private static void insertRelation(final String leadWord, final String followWord, final boolean isLast) {
		final JdbcConnectionPool ds = createConnection();
		final DBI dbi = new DBI(ds);
		final Relation relation = dbi.open(Relation.class);
		relation.relationInsert(leadWord, followWord, isLast);
	}

}
