package src.parser;

import src.ParseSyntaxError;
import src.tokenizer.Token;

import java.util.ArrayList;

import static src.ErrorHandler.printErrorParser;
import static src.tokenizer.TokenType.*;

/**
 * This class is responsible for representing a parsed Jott paramsT node
 * when a JottTree is built
 *
 * @author Hritik "Ricky" Gupta
 * @author Griffin Danner-Doran
 * @author Austin Couch
 * @author Sam Harrell
 */
public class ParamsTNode implements JottTree{

    private JottTree param1;
    private JottTree param2 = null;

    /**
     * Constructor that will try to build a paramsT node from the provided Jott
     * tokens and will immediately throw a ParseSyntax error if it is unable to do so
     *
     * @param tokens the ArrayList of Jott tokens to parse
     * @throws ParseSyntaxError if there are any unexpected tokens preventing the proper
     *                              formation of a representative paramsT
     */
    public ParamsTNode(ArrayList<Token> tokens) throws ParseSyntaxError {
        //if we are in this node, there must be a comma at the start
        Token comma = tokens.remove(0);
        //if we see anything after a comma, we let expr figure out what expr it is
       if(!tokens.isEmpty()){
            param1 = new ExprNode(tokens);
       }else{
           printErrorParser(tokens, null, comma, "func params missing expr", "expected expr start got ");
       }
        //add a second param if we see a comma
        if(!tokens.isEmpty() && tokens.get(0).getTokenType() == COMMA){
            param2 = new ParamsTNode(tokens);
        }else{
            printErrorParser(tokens, R_BRACKET, comma, "func call missing ]", "expected , or ] got ");
        }
    }

    /**
     * Responsible for parsing a paramsT
     *
     * @return this node's respective proper Jott representation as a string
     */
    public String convertToJott(){
        String readout = "," + param1.convertToJott();
        if(param2 != null){
            readout += param2.convertToJott();
        }
        return readout;
    }

    // functions to be in future phases
    public String convertToJava(){return null;}
    public String convertToC(){return null;}
    public String convertToPython(){return null;}
    public boolean validateTree(){return false;}
}
