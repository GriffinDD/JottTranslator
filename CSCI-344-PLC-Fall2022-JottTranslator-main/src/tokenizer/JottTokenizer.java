package src.tokenizer;

import src.ErrorHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static src.ErrorHandler.printErrorParser;
import static src.tokenizer.TokenType.*;

/**
 * This class is responsible for tokenizing Jott code.
 *
 * @author Hritik "Ricky" Gupta
 * @author Griffin Danner-Doran
 * @author Austin Couch
 * @author Sam Harrell
 */
public class JottTokenizer {
	private static final String invalidTokenMsg = "Invalid Token";

	private static final char NEWLINE_CHAR = '\n';
	private static final char CARR_RET_CHAR = '\r';
	private static final char TAB_CHAR = '\t';
	private static final char WHITESPACE_CHAR = ' ';
	private static final char COMMA_CHAR = ',';
	private static final char RBRACKET_CHAR = ']';
	private static final char LBRACKET_CHAR = '[';
	private static final char RBRACE_CHAR = '}';
	private static final char LBRACE_CHAR = '{';
	private static final char EQUALS_CHAR = '=';
	private static final char LARROW_CHAR = '<';
	private static final char RARROW_CHAR = '>';
	private static final char DIVISION_CHAR = '/';
	private static final char ADDITION_CHAR = '+';
	private static final char MINUS_CHAR = '-';
	private static final char MULTIPLICATION_CHAR = '*';
	private static final char SEMICOLON_CHAR = ';';
	private static final char DOT_CHAR = '.';
	private static final char COLON_CHAR = ':';
	private static final char BANG_CHAR = '!';
	private static final char DOUBLE_QUOTE_CHAR = '"';
	private static final char POUND = '#';

	/**
	 * Creates a character ArrayList that contains every character of the
	 * original file in the order it appears.
	 *
	 * @param filename path to the file to be read
	 * @return ArrayList of char based on the original file
	 * @throws IOException if there's an issue when reading the filename
	 */
	private static ArrayList<Character> createCharArrayList(String filename) throws IOException {
		String program = new String(Files.readAllBytes(Paths.get(filename)));
		ArrayList<Character> charList = new ArrayList<>();

		for (var ch : program.toCharArray()) {
			charList.add(ch);
		}

		return charList;
	}

	/**
     * Takes in a filename and tokenizes that file into Tokens
     * based on the rules of the Jott Language
     * @param filename the name of the file to tokenize; can be relative or absolute path
     * @return an ArrayList of Jott Tokens
     */
    public static ArrayList<Token> tokenize(String filename) {
		ArrayList<Character> charArray;
		ArrayList<Token> tokenList = new ArrayList<>();
		int lineNum = 1;

		try {
			charArray = createCharArrayList(filename);
		} catch (IOException e) {
			System.err.println(e);
			return null;
		}

		while (!charArray.isEmpty()) {
			char currentChar = charArray.remove(0);
			StringBuilder tokenStringBuilder = new StringBuilder();
			tokenStringBuilder.append(currentChar);

			switch (currentChar) {
				// all base cases(single characters)
				case NEWLINE_CHAR -> lineNum++;
				//clears whitespace
				case WHITESPACE_CHAR, CARR_RET_CHAR, TAB_CHAR -> {}
				case COMMA_CHAR -> tokenList.add(new Token(tokenStringBuilder.toString(), filename, lineNum, COMMA));
				case RBRACKET_CHAR -> tokenList.add(new Token(tokenStringBuilder.toString(), filename, lineNum, R_BRACKET));
				case LBRACKET_CHAR -> tokenList.add(new Token(tokenStringBuilder.toString(), filename, lineNum, L_BRACKET));
				case LBRACE_CHAR -> tokenList.add(new Token(tokenStringBuilder.toString(), filename, lineNum, L_BRACE));
				case RBRACE_CHAR -> tokenList.add(new Token(tokenStringBuilder.toString(), filename, lineNum, R_BRACE));
				case SEMICOLON_CHAR -> tokenList.add(new Token(tokenStringBuilder.toString(), filename, lineNum, SEMICOLON));
				case COLON_CHAR -> tokenList.add(new Token(tokenStringBuilder.toString(), filename, lineNum, COLON));

				//comment escape
				case POUND -> {
					while (!charArray.isEmpty() && charArray.remove(0) != '\n'){}
					lineNum++;
				}

				// mathOps
				case ADDITION_CHAR, MINUS_CHAR, MULTIPLICATION_CHAR, DIVISION_CHAR ->
						tokenList.add(new Token(tokenStringBuilder.toString(), filename, lineNum, MATH_OP));

				// relOps
				case LARROW_CHAR, RARROW_CHAR -> {
					if (!charArray.isEmpty() && charArray.get(0) == EQUALS_CHAR) {
						tokenStringBuilder.append(charArray.remove(0));
					}

					tokenList.add(new Token(tokenStringBuilder.toString(), filename, lineNum, REL_OP));
				}

				case EQUALS_CHAR -> {
					if (!charArray.isEmpty() && charArray.get(0) == EQUALS_CHAR) {
						tokenStringBuilder.append(charArray.remove(0));
						tokenList.add(new Token(tokenStringBuilder.toString(), filename, lineNum, REL_OP));
					} else {
						tokenList.add(new Token(tokenStringBuilder.toString(), filename, lineNum, ASSIGN));
					}
				}

				// extra-step cases
				case BANG_CHAR -> {
					if (charArray.isEmpty() || charArray.get(0) != EQUALS_CHAR) {
						ErrorHandler.printErrorTokenizer(invalidTokenMsg, tokenStringBuilder.toString(), filename, lineNum);
						return null;
					}

					tokenStringBuilder.append(charArray.remove(0));
					tokenList.add(new Token(tokenStringBuilder.toString(), filename, lineNum, REL_OP));
				}

				case DOUBLE_QUOTE_CHAR -> {
					while (!charArray.isEmpty() && Character.toString(charArray.get(0)).matches("[a-zA-Z0-9\s]")) {
						tokenStringBuilder.append(charArray.remove(0));
					}

					if (charArray.isEmpty() || charArray.get(0) != DOUBLE_QUOTE_CHAR) {
						ErrorHandler.printErrorTokenizer("Invalid token - open string", tokenStringBuilder.toString(), filename, lineNum);
						return null;
					}

					tokenStringBuilder.append(charArray.remove(0));
					tokenList.add(new Token(tokenStringBuilder.toString(), filename, lineNum, STRING));
				}

				case DOT_CHAR -> {
					if (charArray.isEmpty() || !Character.toString(charArray.get(0)).matches("[0-9]")) {
						ErrorHandler.printErrorTokenizer("Invalid token - missing companion digit", tokenStringBuilder.toString(), filename, lineNum);
						return null;
					}

					tokenStringBuilder.append(charArray.remove(0));
					while (!charArray.isEmpty() && Character.toString(charArray.get(0)).matches("[0-9]")) {
						tokenStringBuilder.append(charArray.remove(0));
					}

					tokenList.add(new Token(tokenStringBuilder.toString(), filename, lineNum, NUMBER));
				}

				// more complex cases
				default -> {
					// id, keyword
					if (Character.toString(currentChar).matches("[a-zA-z]")) {
						while (!charArray.isEmpty() && Character.toString(charArray.get(0)).matches("[a-zA-Z0-9]")) {
							tokenStringBuilder.append(charArray.remove(0));
						}

						tokenList.add(new Token(tokenStringBuilder.toString(), filename, lineNum, ID_KEYWORD));

					// number
					} else if (Character.toString(currentChar).matches("[0-9]")) {
						while (!charArray.isEmpty() && Character.toString(charArray.get(0)).matches("[0-9]")) {
							tokenStringBuilder.append(charArray.remove(0));
						}
						//if the next char is . the number continues
						if (!charArray.isEmpty() && charArray.get(0) == DOT_CHAR) {
							tokenStringBuilder.append(charArray.remove(0));
							while (!charArray.isEmpty() && Character.toString(charArray.get(0)).matches("[0-9]")) {
								tokenStringBuilder.append(charArray.remove(0));
							}
						}

						tokenList.add(new Token(tokenStringBuilder.toString(), filename, lineNum, NUMBER));

					//case for non-language characters
					} else {
						ErrorHandler.printErrorTokenizer("Invalid token - token not in language", tokenStringBuilder.toString(), filename, lineNum);
						return null;
					}
				}
			}
		}

		return tokenList;
	}
}