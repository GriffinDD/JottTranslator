package src.parser;

import src.ParseSyntaxError;
import src.tokenizer.Token;

import java.util.ArrayList;

import static src.ErrorHandler.generateErrorString;
import static src.ErrorHandler.printErrorParser;
import static src.tokenizer.TokenType.*;

/**
 * This class is responsible for representing a parsed Jott function definition node
 * when a JottTree is built
 *
 * @author Hritik "Ricky" Gupta
 * @author Griffin Danner-Doran
 * @author Austin Couch
 * @author Sam Harrell
 */
public class FuncDefNode implements JottTree{

    private JottTree paramNode = null;
    private JottTree body = null;
    private Token id;
    private Token returnType;

    /**
     * Constructor that will try to build a function definition node from the provided Jott
     * tokens and will immediately throw a ParseSyntax error if it is unable to do so
     *
     * @param tokens the ArrayList of Jott tokens to parse
     * @throws ParseSyntaxError if there are any unexpected tokens preventing the proper
     *                              formation of a representative function definition
     */
    public FuncDefNode(ArrayList<Token> tokens) throws ParseSyntaxError {
        //if we are in this node, the function list detected an id
        id = tokens.remove(0);
        printErrorParser(tokens, L_BRACKET, id, "func def missing [", "expected [ got ");
        tokens.remove(0);
        //params can have multiple comma seperated values, but must always start with a single entry with an id if not empty
        if(!tokens.isEmpty() && tokens.get(0).getTokenType() == ID_KEYWORD){
            if(tokens.get(0).getToken().matches("Void|Double|Integer|Boolean|String|True|False")){
                printErrorParser("expected id got " + generateErrorString(tokens.get(0)), tokens.get(0).getFilename(),tokens.get(0).getLineNum());
            }else{
                paramNode = new FuncDefParamsNode(tokens);
            }
        }else if(!tokens.isEmpty() && tokens.get(0).getTokenType() != R_BRACKET){
            printErrorParser(tokens, R_BRACKET, id, "func def params incorrect", "expected id got ");
        }
        printErrorParser(tokens, R_BRACKET, id, "func def missing ]", "expected ] got ");
        tokens.remove(0);
        printErrorParser(tokens, COLON, id, "func def missing :", "expected : got ");
        tokens.remove(0);
        if(!tokens.isEmpty() && (tokens.get(0).getToken().matches("Void|Double|Integer|Boolean|String")) ){
            returnType = tokens.remove(0);
        }else{
            printErrorParser(tokens, null, id, "func def missing return type", "expected type got ");
        }
        printErrorParser(tokens, L_BRACE, id, "func def missing {", "expected { got ");
        tokens.remove(0);
        //let body handle its own checking, but if we see an R brace we have an empty body
        if(!tokens.isEmpty() && tokens.get(0).getTokenType() != R_BRACE){
            body = new BodyNode(tokens);
        }
        printErrorParser(tokens, R_BRACE, id, "func def missing }", "expected } got ");
        tokens.remove(0);
    }

    /**
     * Responsible for parsing a function definition
     *
     * @return this node's respective proper Jott representation as a string
     */
    public String convertToJott(){
        String readout = id.getToken() + "[";
        if(paramNode != null){
            readout += paramNode.convertToJott();
        }
        readout += "]:" + returnType.getToken() + "{";
        if(body != null){
            readout += body.convertToJott();
        }
        readout += "}";
        return readout;
    }

    // functions to be in future phases
    public String convertToJava(){return null;}
    public String convertToC(){return null;}
    public String convertToPython(){return null;}
    public boolean validateTree(){return false;}
}
