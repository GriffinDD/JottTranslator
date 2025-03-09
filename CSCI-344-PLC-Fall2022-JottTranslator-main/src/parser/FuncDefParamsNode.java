package src.parser;

import src.ParseSyntaxError;
import src.tokenizer.Token;

import java.util.ArrayList;

import static src.ErrorHandler.generateErrorString;
import static src.ErrorHandler.printErrorParser;
import static src.tokenizer.TokenType.*;

/**
 * This class is responsible for representing a parsed Jott function definition parameters node
 * when a JottTree is built
 *
 * @author Hritik "Ricky" Gupta
 * @author Griffin Danner-Doran
 * @author Austin Couch
 * @author Sam Harrell
 */
public class FuncDefParamsNode implements JottTree{

    private Token id;
    private Token idType;
    private JottTree addParam = null;

    /**
     * Constructor that will try to build a function definition parameters node from the provided Jott
     * tokens and will immediately throw a ParseSyntax error if it is unable to do so
     *
     * @param tokens the ArrayList of Jott tokens to parse
     * @throws ParseSyntaxError if there are any unexpected tokens preventing the proper
     *                              formation of a representative function definition parameters
     */
    public FuncDefParamsNode(ArrayList<Token> tokens) throws ParseSyntaxError {
        //if we are in this node, there is at least 1 param node starting with an ID_Keyword
        id = tokens.remove(0);
        printErrorParser(tokens, COLON, id, "func def params missing :", "expected : got ");
        tokens.remove(0);
        //check for param type
        if(!tokens.isEmpty() && tokens.get(0).getToken().matches("Double|Integer|Boolean|String")){
            idType = tokens.remove(0);
        }else{
            printErrorParser(tokens, null, id, "func def missing param type", "expected type got ");
        }
        //add another param if we see a comma after this param
        if(!tokens.isEmpty() && tokens.get(0).getTokenType() == ID_KEYWORD){
            printErrorParser("expected , or ] got " + generateErrorString(tokens.get(0)), tokens.get(0).getFilename(), tokens.get(0).getLineNum());
        }else if(!tokens.isEmpty() && tokens.get(0).getTokenType() == COMMA){
            addParam = new FuncDefParamsTNode(tokens);
        }
    }

    /**
     * Responsible for parsing a function definition parameters
     *
     * @return this node's respective proper Jott representation as a string
     */
    public String convertToJott(){
        String readout = id.getToken() + ":" + idType.getToken();
        if(addParam != null){
            readout += addParam.convertToJott();
        }
        return readout;
    }

    // functions to be in future phases
    public String convertToJava(){return null;}
    public String convertToC(){return null;}
    public String convertToPython(){return null;}
    public boolean validateTree(){return false;}
}
