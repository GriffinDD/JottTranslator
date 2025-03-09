package src.parser;

import src.ParseSyntaxError;
import src.tokenizer.Token;

import java.util.ArrayList;

import static src.ErrorHandler.printErrorParser;
import static src.tokenizer.TokenType.SEMICOLON;

/**
 * This class is responsible for representing a parsed Jott return statement node
 * when a JottTree is built
 *
 * @author Hritik "Ricky" Gupta
 * @author Griffin Danner-Doran
 * @author Austin Couch
 * @author Sam Harrell
 */
public class ReturnStmtNode implements JottTree{

    private JottTree expression;

    /**
     * Constructor that will try to build a return statement node from the provided Jott
     * tokens and will immediately throw a ParseSyntax error if it is unable to do so
     *
     * @param tokens the ArrayList of Jott tokens to parse
     * @throws ParseSyntaxError if there are any unexpected tokens preventing the proper
     *                              formation of a representative return statement
     */
    public ReturnStmtNode(ArrayList<Token> tokens) throws ParseSyntaxError {
        //we know that we saw the token return since we are in this node
        Token ret = tokens.remove(0);
        //check to make sure we actually have an expression to return
        if(!tokens.isEmpty() && tokens.get(0).getTokenType() != SEMICOLON){
            expression = new ExprNode(tokens);
        }else{
            printErrorParser("Expected return expr got ;", ret.getFilename(), ret.getLineNum());
        }
        //remove ending semicolon after expression is grabbed
        printErrorParser(tokens, SEMICOLON, ret, "return expr missing ;", "expected ; got ");
        tokens.remove(0);
    }

    /**
     * Responsible for parsing a return statement
     *
     * @return this node's respective proper Jott representation as a string
     */
    public String convertToJott(){
        return "return " + expression.convertToJott() + ";";
    }

    // functions to be in future phases
    public String convertToJava(){return null;}
    public String convertToC(){return null;}
    public String convertToPython(){return null;}
    public boolean validateTree(){return false;}
}
