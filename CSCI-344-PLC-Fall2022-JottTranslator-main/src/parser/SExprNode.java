package src.parser;

import src.ParseSyntaxError;
import src.tokenizer.Token;

import java.util.ArrayList;

import static src.ErrorHandler.generateErrorString;
import static src.ErrorHandler.printErrorParser;
import static src.tokenizer.TokenType.*;

/**
 * This class is responsible for representing a parsed Jott string expression node
 * when a JottTree is built
 *
 * @author Hritik "Ricky" Gupta
 * @author Griffin Danner-Doran
 * @author Austin Couch
 * @author Sam Harrell
 */
public class SExprNode implements JottTree{

    private Token str = null;
    private JottTree func = null;

    /**
     * Constructor that will try to build a string expression node from the provided Jott
     * tokens and will immediately throw a ParseSyntax error if it is unable to do so
     *
     * @param tokens the ArrayList of Jott tokens to parse
     * @throws ParseSyntaxError if there are any unexpected tokens preventing the proper
     *                              formation of a representative string expression
     */
    public SExprNode(ArrayList<Token> tokens) throws ParseSyntaxError {
        //s expr can only be 3 things
        //just a string
        if(!tokens.isEmpty() && tokens.get(0).getTokenType() == STRING){
            str = tokens.remove(0);
        }//an id or an id[
        else if(!tokens.isEmpty() && tokens.get(0).getTokenType() == ID_KEYWORD && !tokens.get(0).getToken().matches("Void|Double|Integer|Boolean|String|True|False")){
            //if it is a function call, we must make a FuncCallNode
            if(tokens.size() >= 2 && tokens.get(1).getTokenType() == R_BRACKET){
                func = new FuncCallNode(tokens);
            }//if not, just a single id
            else{
                str = tokens.remove(0);
            }
        }else{
            printErrorParser("Expected s expr start got " + generateErrorString(tokens.get(0)), tokens.get(0).getFilename(), tokens.get(0).getLineNum());
        }
        if(tokens.get(0).getTokenType() == SEMICOLON || tokens.get(0).getTokenType() == R_BRACKET
                || tokens.get(0).getTokenType() == COMMA || tokens.get(0).getTokenType() == REL_OP) {
            return;
        } else {
            String pre = "";
            if(str != null){
                pre = "string";
            }else if(func != null){
                pre = "func call";
            }
            printErrorParser("Unexpected token - " + generateErrorString(tokens.get(0)) + " after " + pre, tokens.get(0).getFilename(), tokens.get(0).getLineNum());
        }
    }

    /**
     * Responsible for parsing a string expression
     *
     * @return this node's respective proper Jott representation as a string
     */
    public String convertToJott(){
        if(str != null){
            return str.getToken();
        }else if(func != null){
            return func.convertToJott();
        }else{
            return null;
        }
    }

    // functions to be in future phases
    public String convertToJava(){return null;}
    public String convertToC(){return null;}
    public String convertToPython(){return null;}
    public boolean validateTree(){return false;}
}
