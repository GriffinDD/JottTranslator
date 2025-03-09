package src.parser;

import src.ParseSyntaxError;
import src.tokenizer.Token;

import java.util.ArrayList;

import static src.ErrorHandler.printErrorParser;
import static src.tokenizer.TokenType.*;

/**
 * This class is responsible for representing a parsed Jott else if list node
 * when a JottTree is built
 *
 * @author Hritik "Ricky" Gupta
 * @author Griffin Danner-Doran
 * @author Austin Couch
 * @author Sam Harrell
 */
public class ElseIfLstNode implements JottTree{

    private JottTree cond;
    private JottTree body;
    private JottTree elseIf = null;

    /**
     * Constructor that will try to build an else if list node from the provided Jott
     * tokens and will immediately throw a ParseSyntax error if it is unable to do so
     *
     * @param tokens the ArrayList of Jott tokens to parse
     * @throws ParseSyntaxError if there are any unexpected tokens preventing the proper
     *                              formation of a representative else if list
     */
    public ElseIfLstNode(ArrayList<Token> tokens) throws ParseSyntaxError {
        //if we are in this node, the function list detected an elseif token
        Token ei = tokens.remove(0);
        printErrorParser(tokens, L_BRACKET, ei, "elseif missing [", "expected [ got ");
        tokens.remove(0);
        if(!tokens.isEmpty()){// b_expr can start with basically anything, let it handle itself
            cond = new BExprNode(tokens);
        }
        printErrorParser(tokens, R_BRACKET, ei, "elseif missing ]", "expected ] got ");
        tokens.remove(0);
        printErrorParser(tokens, L_BRACE, ei, "elseif missing {", "expected { got ");
        tokens.remove(0);
        //let body handle its own checking
        if(!tokens.isEmpty()){
            body = new BodyNode(tokens);
        }
        printErrorParser(tokens, R_BRACE, ei, "elseif missing }", "expected } got ");
        tokens.remove(0);
        if(!tokens.isEmpty() && tokens.get(0).getToken().equals("elseif")){
            elseIf = new ElseIfLstNode(tokens);
        }
    }

    /**
     * Responsible for parsing an else if list
     *
     * @return this node's respective proper Jott representation as a string
     */
    public String convertToJott(){
        String readout = "elseif[" + cond.convertToJott() + "]{" + body.convertToJott() + "}";
        if(elseIf != null){
            readout += elseIf.convertToJott();
        }
        return readout;
    }

    // functions to be in future phases
    public String convertToJava(){return null;}
    public String convertToC(){return null;}
    public String convertToPython(){return null;}
    public boolean validateTree(){return false;}
}
