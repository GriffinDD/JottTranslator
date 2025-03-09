package src;

import src.tokenizer.Token;
import src.tokenizer.TokenType;

import java.util.ArrayList;

import static src.tokenizer.TokenType.*;

public class ErrorHandler {
    /**
     * Prints syntax error details to standard error when
     * erroneous token is processed.
     *
     * @param message message to print out
     * @param tokenString string of the token that caused an error
     * @param filename name of the file that contains the error causing token
     * @param lineNum line number of the token
     */
    public static void printErrorTokenizer(String message, String tokenString, String filename, int lineNum) {
        System.err.printf("Syntax Error:%n%s \"%s\"%n%s:%d%n", message, tokenString, filename, lineNum);
    }

    /**
     * Prints syntax error details to standard error when
     * an error in parsing occurs
     *
     * @param message message to print out
     * @param filename name of the file that contains the error causing token
     * @param lineNum line number of the token
     */
    public static void printErrorParser(String message, String filename, int lineNum) throws ParseSyntaxError{
        System.err.printf("Syntax Error:%n%s %n%s:%d%n", message, filename, lineNum);
        throw new ParseSyntaxError("Parse Failed");
    }

    /**
     * Prints syntax error details to standard error when
     * an error in parsing occurs at a potential end of tokenizer string
     *
     * @param tokens the list of tokens we are parsing
     * @param t the TokenType we expect to see here
     * @param id the token that defines the structure we are evaluating (ex a func def)
     * @param emptyMessage the message to print if we find out that the set of tokens was empty
     * @param wrongSymbolMessage the message to print if we find out that the next token is wrong
     */
    public static void printErrorParser(ArrayList<Token> tokens, TokenType t, Token id, String emptyMessage, String wrongSymbolMessage) throws ParseSyntaxError{
        if(tokens.isEmpty()){
            printErrorParser(emptyMessage, id.getFilename(), id.getLineNum());
        }else if(tokens.get(0).getTokenType() != t){
            wrongSymbolMessage += generateErrorString(tokens.get(0));
            printErrorParser(wrongSymbolMessage, id.getFilename(), id.getLineNum());
        }
    }

    /**
     * Generates the corresponding string for an incorrect element detected in the token stream
     *
     * @param t, the token that was not of the type we expected
     */
    public static String generateErrorString(Token t){
       if(t.getTokenType() == NUMBER){
           if(t.getToken().contains(Character.toString('.'))){
               return "double";
           }else{
               return "int";
           }
       }else if(t.getTokenType() == STRING){
           return "string";
       }else if(t.getTokenType() == ID_KEYWORD) {
           if (t.getToken().matches("True|False")){
               return "boolean";
           }else if (t.getToken().matches("Double|Integer|Boolean|String")){
               return "type";
           }else if(t.getToken().matches("Void")){
               return "Void";
           }else{
               return "id";
           }
       }
           return t.getToken();
    }

}
