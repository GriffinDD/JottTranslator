package src.parser;

import src.ParseSyntaxError;
import src.tokenizer.Token;

import java.util.ArrayList;

import static src.ErrorHandler.printErrorParser;
import static src.tokenizer.TokenType.*;

/**
 * This class is responsible for representing a parsed Jott else node
 * when a JottTree is built
 *
 * @author Hritik "Ricky" Gupta
 * @author Griffin Danner-Doran
 * @author Austin Couch
 * @author Sam Harrell
 */
public class ElseNode implements JottTree{

    private JottTree body;

    /**
     * Constructor that will try to build an else node from the provided Jott
     * tokens and will immediately throw a ParseSyntax error if it is unable to do so
     *
     * @param tokens the ArrayList of Jott tokens to parse
     * @throws ParseSyntaxError if there are any unexpected tokens preventing the proper
     *                              formation of a representative else
     */
    public ElseNode(ArrayList<Token> tokens) throws ParseSyntaxError {
        //if we are in this node, the function list detected an else token
        Token e = tokens.remove(0);
        printErrorParser(tokens, L_BRACE, e, "else missing {", "expected { got ");
        tokens.remove(0);
        //let body handle its own checking
        if(!tokens.isEmpty()){
            body = new BodyNode(tokens);
        }
        printErrorParser(tokens, R_BRACE, e, "else missing }", "expected } got ");
        tokens.remove(0);
    }

    /**
     * Responsible for parsing an else statement
     *
     * @return this node's respective proper Jott representation as a string
     */
    public String convertToJott(){
        return "else{" + body.convertToJott() + "}";
    }

    // functions to be in future phases
    public String convertToJava(){return null;}
    public String convertToC(){return null;}
    public String convertToPython(){return null;}
    public boolean validateTree(){return false;}
}
