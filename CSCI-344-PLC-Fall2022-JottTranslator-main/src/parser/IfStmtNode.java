package src.parser;

import src.ParseSyntaxError;
import src.tokenizer.Token;

import java.util.ArrayList;

import static src.ErrorHandler.printErrorParser;
import static src.tokenizer.TokenType.*;

/**
 * This class is responsible for representing a parsed Jott if statement node
 * when a JottTree is built
 *
 * @author Hritik "Ricky" Gupta
 * @author Griffin Danner-Doran
 * @author Austin Couch
 * @author Sam Harrell
 */
public class IfStmtNode implements JottTree{

    private JottTree cond;
    private JottTree body;
    private JottTree elseIf = null;
    private JottTree elseCase = null;

    /**
     * Constructor that will try to build an if statement node from the provided Jott
     * tokens and will immediately throw a ParseSyntax error if it is unable to do so
     *
     * @param tokens the ArrayList of Jott tokens to parse
     * @throws ParseSyntaxError if there are any unexpected tokens preventing the proper
     *                              formation of a representative if statement
     */
    public IfStmtNode(ArrayList<Token> tokens) throws ParseSyntaxError {
        //if we are in this node, the function list detected an if token
        Token i = tokens.remove(0);
        printErrorParser(tokens, L_BRACKET, i, "if stmt missing [", "expected [ got ");
        tokens.remove(0);
        if(!tokens.isEmpty() && tokens.get(0).getTokenType() != R_BRACKET){// b_expr can start with basically anything, let it handle itself
            cond = new BExprNode(tokens);
        }else{
            //however, if the next thing we see is an r bracket, we dont have cond, which is an issue
            printErrorParser("if stmt missing condition", tokens.get(0).getFilename(),tokens.get(0).getLineNum());
        }
        printErrorParser(tokens, R_BRACKET, i, "if stmt missing ]", "expected ] got ");
        tokens.remove(0);
        printErrorParser(tokens, L_BRACE, i, "if stmt missing {", "expected { got ");
        tokens.remove(0);
        //let body handle its own checking
        if(!tokens.isEmpty()){
            body = new BodyNode(tokens);
        }
        printErrorParser(tokens, R_BRACE, i, "if stmt missing }", "expected } got ");
        tokens.remove(0);
        if(!tokens.isEmpty() && tokens.get(0).getToken().equals("elseif")){
            elseIf = new ElseIfLstNode(tokens);
        }
        if(!tokens.isEmpty() && tokens.get(0).getToken().equals("else")){
            elseCase = new ElseNode(tokens);
        }
    }

    /**
     * Responsible for parsing an if statement
     *
     * @return this node's respective proper Jott representation as a string
     */
    public String convertToJott(){
        String readout = "if[" + cond.convertToJott() + "]{" + body.convertToJott() + "}";
        if(elseIf != null){
            readout += elseIf.convertToJott();
        }
        if(elseCase != null){
            readout += elseCase.convertToJott();
        }
        return readout;
    }

    // functions to be in future phases
    public String convertToJava(){return null;}
    public String convertToC(){return null;}
    public String convertToPython(){return null;}
    public boolean validateTree(){return false;}
}
