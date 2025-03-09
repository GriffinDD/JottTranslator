package src.parser;

import src.ParseSyntaxError;
import src.tokenizer.Token;

import java.util.ArrayList;

import static src.ErrorHandler.printErrorParser;
import static src.tokenizer.TokenType.*;

/**
 * This class is responsible for representing a parsed Jott assignment node
 * when a JottTree is built
 *
 * @author Hritik "Ricky" Gupta
 * @author Griffin Danner-Doran
 * @author Austin Couch
 * @author Sam Harrell
 */
public class AsmtNode implements JottTree{

    private Token type = null;
    private Token id;
    private JottTree expression;

    /**
     * Constructor that will try to build an assignment node from the provided Jott
     * tokens and will immediately throw a ParseSyntax error if it is unable to do so
     *
     * @param tokens the ArrayList of Jott tokens to parse
     * @throws ParseSyntaxError if there are any unexpected tokens preventing the proper
     *                              formation of a representative assignment
     */
    public AsmtNode(ArrayList<Token> tokens) throws ParseSyntaxError {
        //if it has a variable type, has to go to a specific expr type
        if(!tokens.isEmpty() && tokens.get(0).getToken().matches("Double|Integer|Boolean|String")){
            type = tokens.remove(0);
            //next thing needs to be an ID
            if(!tokens.isEmpty() && tokens.get(0).getTokenType() == ID_KEYWORD && !tokens.get(0).getToken().matches("Void|Double|Integer|Boolean|String|True|False")){
                id = tokens.remove(0);
            }else{
                printErrorParser(tokens, null, id, "asmt missing id", "expected id got ");
            }
            printErrorParser(tokens, ASSIGN, id, "asmt missing =", "expected = got ");
            tokens.remove(0);

            if(!tokens.isEmpty() && tokens.get(0).getTokenType() != SEMICOLON){
                //As per discussion with prof in class, even with a defined type given, it simply hands it off to the expr node
                //to figure out what type of expr it is instead of having it try to match the given type
                expression = new ExprNode(tokens);
            }else{
                printErrorParser(tokens, null, id, "asmt missing expr", "expected expr got ");
            }

        }//if there is not a specific type, it can be any one of the 4 expression types, so pass to expr to figure it out
        else{
            if(!tokens.isEmpty() && tokens.get(0).getTokenType() == ID_KEYWORD && !tokens.get(0).getToken().matches("Void|Double|Integer|Boolean|String|True|False")){
                id = tokens.remove(0);
            }else{
                printErrorParser(tokens, null, id, "asmt missing id", "expected id got ");
            }
            printErrorParser(tokens, ASSIGN, id, "asmt missing =", "expected = got ");
            tokens.remove(0);
            //can be an expr as long as there is something between the = and the ;
            if(!tokens.isEmpty() && tokens.get(0).getTokenType() != SEMICOLON){
                expression = new ExprNode(tokens);
            }else{
                printErrorParser(tokens, null, id, "asmt missing expr", "expected expr got ");
            }
        }
        printErrorParser(tokens, SEMICOLON, id, "asmt missing ;", "expected ; got ");
        tokens.remove(0);
    }

    /**
     * Responsible for parsing an assignment
     *
     * @return this node's respective proper Jott representation as a string
     */
    public String convertToJott(){
        String readout = "";
        if(type != null){
            readout += type.getToken() + " ";
        }
        readout += id.getToken() + "=" + expression.convertToJott() + ";";
        return readout;
    }

    // functions to be in future phases
    public String convertToJava(){return null;}
    public String convertToC(){return null;}
    public String convertToPython(){return null;}
    public boolean validateTree(){return false;}
}
