package src.parser;

import src.ParseSyntaxError;
import src.tokenizer.Token;

import java.util.ArrayList;

import static src.ErrorHandler.printErrorParser;
import static src.tokenizer.TokenType.*;

/**
 * This class is responsible for representing a parsed Jott body node
 * when a JottTree is built
 *
 * @author Hritik "Ricky" Gupta
 * @author Griffin Danner-Doran
 * @author Austin Couch
 * @author Sam Harrell
 */
public class BodyNode implements JottTree{

    private ArrayList<JottTree> bodies = new ArrayList<>();

    /**
     * Constructor that will try to build a body node from the provided Jott
     * tokens and will immediately throw a ParseSyntax error if it is unable to do so
     *
     * @param tokens the ArrayList of Jott tokens to parse
     * @throws ParseSyntaxError if there are any unexpected tokens preventing the proper
     *                              formation of a representative body
     */
    public BodyNode(ArrayList<Token> tokens) throws ParseSyntaxError {
        //check to see if the first item we see is an R_BRACE, if so then we have an empty body
        while(!tokens.isEmpty() && tokens.get(0).getTokenType() != R_BRACE){
            if(tokens.get(0).getToken().equals("return")){
                ReturnStmtNode next = new ReturnStmtNode(tokens);
                bodies.add(next);
                //body_stmts can loop infinitely, but a return statement signals the final stmt
                break;
            }else{
                //can be one of various body_stmts
                BodyStmtNode next = new BodyStmtNode(tokens);
                bodies.add(next);
            }

        }
    }

    /**
     * Responsible for parsing a body
     *
     * @return this node's respective proper Jott representation as a string
     */
    public String convertToJott(){
        String readout = "";
        for(JottTree node: bodies){
            readout += node.convertToJott();
        }
        return readout;
    }

    // functions to be in future phases
    public String convertToJava(){return null;}
    public String convertToC(){return null;}
    public String convertToPython(){return null;}
    public boolean validateTree(){return false;}
}
