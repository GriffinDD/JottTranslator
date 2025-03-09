package src.parser;

import src.ParseSyntaxError;
import src.tokenizer.*;

import static src.ErrorHandler.generateErrorString;
import static src.ErrorHandler.printErrorParser;
import static src.tokenizer.TokenType.*;

import java.util.ArrayList;

/**
 * This class is responsible for representing a parsed Jott function list node
 * when a JottTree is built
 *
 * @author Hritik "Ricky" Gupta
 * @author Griffin Danner-Doran
 * @author Austin Couch
 * @author Sam Harrell
 */
public class FuncLstNode implements JottTree{

    private ArrayList<JottTree> nodes = new ArrayList<JottTree>();

    /**
     * Constructor that will try to build a function list node from the provided Jott
     * tokens and will immediately throw a ParseSyntax error if it is unable to do so
     *
     * @param tokens the ArrayList of Jott tokens to parse
     * @throws ParseSyntaxError if there are any unexpected tokens preventing the proper
     *                              formation of a representative function list
     */
    public FuncLstNode(ArrayList<Token> tokens) throws ParseSyntaxError {
        //checks if the next thing is an id, means the start of a function_def
        while(!tokens.isEmpty()){
           if(tokens.get(0).getTokenType() == ID_KEYWORD && !tokens.get(0).getToken().matches("Void|Double|Integer|Boolean|String|True|False")){
                FuncDefNode next = new FuncDefNode(tokens);
                nodes.add(next);
            }else{
                printErrorParser("expected id got " + generateErrorString(tokens.get(0)), tokens.get(0).getFilename(), tokens.get(0).getLineNum());
            }
        }
    }

    /**
     * Responsible for parsing a function list
     *
     * @return this node's respective proper Jott representation as a string
     */
    public String convertToJott(){
        String readout = "";
        for(JottTree node: nodes){
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
