package src.parser;

import src.ParseSyntaxError;
import src.tokenizer.Token;

import java.util.ArrayList;

import static src.ErrorHandler.printErrorParser;
import static src.tokenizer.TokenType.*;

/**
 * This class is responsible for representing a parsed Jott while statement node
 * when a JottTree is built
 *
 * @author Hritik "Ricky" Gupta
 * @author Griffin Danner-Doran
 * @author Austin Couch
 * @author Sam Harrell
 */
public class WhileStmtNode implements JottTree{

    private JottTree cond;
    private JottTree body;

    /**
     * Constructor that will try to build a while statement node from the provided Jott
     * tokens and will immediately throw a ParseSyntax error if it is unable to do so
     *
     * @param tokens the ArrayList of Jott tokens to parse
     * @throws ParseSyntaxError if there are any unexpected tokens preventing the proper
     *                              formation of a representative while statement
     */
    public WhileStmtNode(ArrayList<Token> tokens) throws ParseSyntaxError {
        //if we are in this node, the function list detected a while token
        Token w = tokens.remove(0);
        printErrorParser(tokens, L_BRACKET, w, "while stmt missing [", "expected [ got ");
        tokens.remove(0);
        if(!tokens.isEmpty() && tokens.get(0).getTokenType() != R_BRACKET){// b_expr can start with basically anything, let it handle itself
            cond = new BExprNode(tokens);
        }else{
            //however, if the next thing we see is an r bracket, we dont have cond, which is an issue
            printErrorParser("while stmt missing condition", tokens.get(0).getFilename(),tokens.get(0).getLineNum());
        }
        printErrorParser(tokens, R_BRACKET, w, "while stmt missing ]", "expected ] got ");
        tokens.remove(0);
        printErrorParser(tokens, L_BRACE, w, "while stmt missing {", "expected { got ");
        tokens.remove(0);
        //let body handle its own checking
        if(!tokens.isEmpty()){
            body = new BodyNode(tokens);
        }
        printErrorParser(tokens, R_BRACE, w, "while stmt missing }", "expected } got ");
        tokens.remove(0);
    }

    /**
     * Responsible for parsing a while statement
     *
     * @return this node's respective proper Jott representation as a string
     */
    public String convertToJott(){
        return "while[" + cond.convertToJott() + "]{" + body.convertToJott() + "}";

    }

    // functions to be in future phases
    public String convertToJava(){return null;}
    public String convertToC(){return null;}
    public String convertToPython(){return null;}
    public boolean validateTree(){return false;}
}
