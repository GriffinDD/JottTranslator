package src.parser;

import src.ParseSyntaxError;
import src.tokenizer.Token;

import java.util.ArrayList;

import static src.ErrorHandler.generateErrorString;
import static src.ErrorHandler.printErrorParser;
import static src.tokenizer.TokenType.*;

/**
 * This class is responsible for representing a parsed Jott statement node
 * when a JottTree is built
 *
 * @author Hritik "Ricky" Gupta
 * @author Griffin Danner-Doran
 * @author Austin Couch
 * @author Sam Harrell
 */
public class StmtNode implements JottTree{

    private JottTree node;
    private Boolean isFunc = false;

    /**
     * Constructor that will try to build a statement node from the provided Jott
     * tokens and will immediately throw a ParseSyntax error if it is unable to do so
     *
     * @param tokens the ArrayList of Jott tokens to parse
     * @throws ParseSyntaxError if there are any unexpected tokens preventing the proper
     *                              formation of a representative statement
     */
    public StmtNode(ArrayList<Token> tokens) throws ParseSyntaxError {
       //if it is type, an ID, then semicolon, we have a variable declaration
        if(tokens.size() >= 3 && (tokens.get(0).getToken().matches("Double|Integer|Boolean|String")
        && tokens.get(1).getTokenType() == ID_KEYWORD && tokens.get(2).getTokenType() == SEMICOLON)){
            node = new VarDecNode(tokens);
            //if it is a type id = or id = then it is an assignment
        }else if(tokens.size() >= 3 && tokens.get(0).getToken().matches("Double|Integer|Boolean|String") && tokens.get(1).getTokenType() == ID_KEYWORD
                && tokens.get(2).getTokenType() == ASSIGN){
            node = new AsmtNode(tokens);
        }else if(tokens.size() >= 2 && tokens.get(0).getTokenType() == ID_KEYWORD && tokens.get(1).getTokenType() == ASSIGN
        && !tokens.get(0).getToken().matches("Void|Double|Integer|Boolean|String|True|False")){
            node = new AsmtNode(tokens);
        }else if(!tokens.isEmpty() && tokens.get(0).getTokenType() == ID_KEYWORD && !tokens.get(0).getToken().matches("Void|Double|Integer|Boolean|String|True|False")){
            Token func = tokens.get(0);
            node = new FuncCallNode(tokens);
            printErrorParser(tokens, SEMICOLON, func, "func call stmt missing ;", "expected ; got ");
            tokens.remove(0);
            isFunc = true;
        }else{
            printErrorParser("Expected stmt start got " + generateErrorString(tokens.get(0)), tokens.get(0).getFilename(), tokens.get(0).getLineNum());
        }
    }

    /**
     * Responsible for parsing a statement
     *
     * @return this node's respective proper Jott representation as a string
     */
    public String convertToJott(){
        String readout = node.convertToJott();
        if(isFunc){
            readout += ";";
        }
        return readout;
    }

    // functions to be in future phases
    public String convertToJava(){return null;}
    public String convertToC(){return null;}
    public String convertToPython(){return null;}
    public boolean validateTree(){return false;}
}
