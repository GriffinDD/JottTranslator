package src.parser;

import src.ParseSyntaxError;
import src.tokenizer.Token;

import java.util.ArrayList;

import static src.ErrorHandler.printErrorParser;
import static src.tokenizer.TokenType.*;

/**
 * This class is responsible for representing a parsed Jott fucntion call node
 * when a JottTree is built
 *
 * @author Hritik "Ricky" Gupta
 * @author Griffin Danner-Doran
 * @author Austin Couch
 * @author Sam Harrell
 */
public class FuncCallNode implements JottTree{

    private Token function;
    private JottTree param = null;

    /**
     * Constructor that will try to build a function call node from the provided Jott
     * tokens and will immediately throw a ParseSyntax error if it is unable to do so
     *
     * @param tokens the ArrayList of Jott tokens to parse
     * @throws ParseSyntaxError if there are any unexpected tokens preventing the proper
     *                              formation of a representative function call
     */
    public FuncCallNode(ArrayList<Token> tokens) throws ParseSyntaxError {
        //we start with an ID if we are in this node
        function = tokens.remove(0);
        printErrorParser(tokens, L_BRACKET, function, "func call missing [", "expected [ got ");
        tokens.remove(0);
        //if next thing is the end bracket, no params, else let params handle it
        if(!tokens.isEmpty() && tokens.get(0).getTokenType() != R_BRACKET){
            param = new ParamsNode(tokens);
        }
        printErrorParser(tokens, R_BRACKET, function, "func call missing ]", "expected ] got ");
        tokens.remove(0);
    }

    /**
     * Responsible for parsing a function call
     *
     * @return this node's respective proper Jott representation as a string
     */
    public String convertToJott(){
        String readout = function.getToken() + "[";
        if(param != null){
            readout += param.convertToJott();
        }
        return readout + "]";
    }

    // functions to be in future phases
    public String convertToJava(){return null;}
    public String convertToC(){return null;}
    public String convertToPython(){return null;}
    public boolean validateTree(){return false;}
}
