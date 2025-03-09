package src.parser;

import src.ParseSyntaxError;
import src.tokenizer.Token;

import java.util.ArrayList;

/**
 * This class is responsible for parsing Jott Tokens
 * into a Jott parse tree.
 *
 * @author Hritik "Ricky" Gupta
 * @author Griffin Danner-Doran
 * @author Austin Couch
 * @author Sam Harrell
 */
public class JottParser {

    /**
     * Parses an ArrayList of Jotton tokens into a Jott Parse Tree.
     * @param tokens the ArrayList of Jott tokens to parse
     * @return the root of the Jott Parse Tree represented by the tokens.
     *         or null upon an error in parsing.
     */
    public static JottTree parse(ArrayList<Token> tokens){
        if(tokens.isEmpty()){
            return null;
        }else{
            try {
                ProgramNode programRoot = new ProgramNode(tokens);
                return programRoot;
            }catch(ParseSyntaxError p){
                return null;
            }//may need to catch out of bounds access errors if the missing symbol is the last in a parse
        }
    }
}