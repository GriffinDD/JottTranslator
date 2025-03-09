package src.parser;

import src.ParseSyntaxError;
import src.tokenizer.Token;

import java.util.ArrayList;

import static src.ErrorHandler.printErrorParser;
import static src.tokenizer.TokenType.*;

/**
 * This class is responsible for representing a parsed Jott variable declaration node
 * when a JottTree is built
 *
 * @author Hritik "Ricky" Gupta
 * @author Griffin Danner-Doran
 * @author Austin Couch
 * @author Sam Harrell
 */
public class VarDecNode implements JottTree{

    private Token idType;
    private Token id;

    /**
     * Constructor that will try to build a variable declaration node from the provided Jott
     * tokens and will immediately throw a ParseSyntax error if it is unable to do so
     *
     * @param tokens the ArrayList of Jott tokens to parse
     * @throws ParseSyntaxError if there are any unexpected tokens preventing the proper
     *                              formation of a representative variable declaration
     */
    public VarDecNode(ArrayList<Token> tokens) throws ParseSyntaxError {
        //if we are in this node, we know our first token is a type
        idType = tokens.remove(0);
        //next item should be an id
        if(!tokens.isEmpty() && tokens.get(0).getTokenType() == ID_KEYWORD && !tokens.get(0).getToken().matches("Void|Double|Integer|Boolean|String|True|False")){
            id = tokens.remove(0);
        }else{
            printErrorParser(tokens, null, idType, "var dec missing id", "expected id got ");
        }
        printErrorParser(tokens, SEMICOLON, idType, "var dec missing ;", "expected ; got ");
        tokens.remove(0);

    }

    /**
     * Responsible for parsing a variable declaration
     *
     * @return this node's respective proper Jott representation as a string
     */
    public String convertToJott(){return idType.getToken() + id.getToken() + ";";
    }

    // functions to be in future phases
    public String convertToJava(){return null;}
    public String convertToC(){return null;}
    public String convertToPython(){return null;}
    public boolean validateTree(){return false;}
}
