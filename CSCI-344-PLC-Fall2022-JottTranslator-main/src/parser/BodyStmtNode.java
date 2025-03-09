package src.parser;

import src.ParseSyntaxError;
import src.tokenizer.Token;

import java.util.ArrayList;

import static src.ErrorHandler.printErrorParser;
import static src.tokenizer.TokenType.ID_KEYWORD;

/**
 * This class is responsible for representing a parsed Jott body statement node
 * when a JottTree is built
 *
 * @author Hritik "Ricky" Gupta
 * @author Griffin Danner-Doran
 * @author Austin Couch
 * @author Sam Harrell
 */
public class BodyStmtNode implements JottTree{

    private JottTree statement;

    /**
     * Constructor that will try to build a body statement node from the provided Jott
     * tokens and will immediately throw a ParseSyntax error if it is unable to do so
     *
     * @param tokens the ArrayList of Jott tokens to parse
     * @throws ParseSyntaxError if there are any unexpected tokens preventing the proper
     *                              formation of a representative body statement
     */
    public BodyStmtNode(ArrayList<Token> tokens) throws ParseSyntaxError {
        if(tokens.get(0).getToken().equals("elseif")){
            printErrorParser("elseif without an if", tokens.get(0).getFilename(), tokens.get(0).getLineNum());
        }else if(tokens.get(0).getToken().equals("else")){
            printErrorParser("else without an if", tokens.get(0).getFilename(), tokens.get(0).getLineNum());
        } else if(tokens.get(0).getToken().equals("if")){
           statement = new IfStmtNode(tokens);
        } else if(tokens.get(0).getToken().equals("while")){
           statement = new WhileStmtNode(tokens);
        } else if(tokens.get(0).getTokenType() == ID_KEYWORD){
           statement = new StmtNode(tokens);
        } else{
            printErrorParser(tokens, ID_KEYWORD, tokens.get(0), "body stmt missing stmt", "expected stmt got ");
        }
    }

    /**
     * Responsible for parsing a body statement
     *
     * @return this node's respective proper Jott representation as a string
     */
    public String convertToJott(){
        return statement.convertToJott();
    }

    // functions to be in future phases
    public String convertToJava(){return null;}
    public String convertToC(){return null;}
    public String convertToPython(){return null;}
    public boolean validateTree(){return false;}
}
