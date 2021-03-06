package lambda.model.lambdaterm;

import java.util.LinkedList;
import java.util.List;

import lambda.model.lambdaterm.visitor.IsValidVisitor;

import java.text.ParseException;

import lambda.model.levels.LevelManager;
import lambda.viewcontroller.lambdaterm.LambdaNodeViewController;

/**
 * A utility class with helper methods for lambda terms.
 *
 * @author Florian Fervers
 */
public final class LambdaUtils {
    /**
     * Parses the given string as a lambda term. "/x.term" represents an
     * abstraction, paranthesis are allowed, all other characters are variables.
     *
     * @param string
     *            the string to be parsed
     * @return the parsed lambda term
     * @throws ParseException
     *             if the string could not be parsed as a lambda term
     */
    public static LambdaRoot fromString(String string) throws ParseException {
        LambdaRoot result = new LambdaRoot();

        int[] index = new int[1]; // Reference to a single integer
        index[0] = 0;
        boolean[] closingParanthesis = new boolean[1]; // Reference to a single
                                                       // boolean
        closingParanthesis[0] = false;
        result.setChild(fromString(string, index, 0, -1, closingParanthesis));

        if (!result.accept(new IsValidVisitor())) {
            throw new ParseException("Parsing resulted in an invalid term!", -1);
        }

        return result;
    }

    /**
     * Parses the given string as a lambda term. "/x.term" represents an
     * abstraction, paranthesis are allowed, spaces are ignored, all other
     * characters are variables.
     *
     * @param string
     *            the string to be parsed
     * @param index
     *            the reference to the current index position in the string
     * @return the parsed lambda term
     * @throws ParseException
     *             if the string could not be parsed as a lambda term
     */
    private static LambdaTerm fromString(String string, int[] index, int depth,
            int lastParenthesisOpenDepth, boolean[] closingParanthesis)
            throws ParseException {
        List<LambdaTerm> terms = new LinkedList<>();

        // Adds all terms on this layer to list terms
        while (index[0] < string.length()
                && !(closingParanthesis[0] && depth > lastParenthesisOpenDepth)) { 
         // not at end of string and not ended by closing parenthesis   
            switch (string.charAt(index[0])) {
            case '/': { // New abstraction
                // Bound variable
                ++index[0];
                if (string.charAt(index[0]) == ' '
                        || string.charAt(index[0]) == '/'
                        || string.charAt(index[0]) == '.') {
                    throw new ParseException("Invalid lambda term format!", index[0]);
                }
                char variableName = string.charAt(index[0]);
                // Point
                ++index[0];
                if (string.charAt(index[0]) != '.') {
                    throw new ParseException("Invalid lambda term format!",
                            index[0]);
                }
                // Inside term
                ++index[0];
                LambdaAbstraction abstraction = new LambdaAbstraction(null,
                        LevelManager.convertVariableToColor(variableName), false);
                abstraction.setInside(fromString(string, index, depth + 1,
                        lastParenthesisOpenDepth, closingParanthesis));
                terms.add(abstraction);
                break;
            }
            case '(': {
                ++index[0];
                terms.add(fromString(string, index, depth + 1, depth,
                        closingParanthesis));
                break;
            }
            case ')': {
                ++index[0];
                closingParanthesis[0] = true;
                break;
            }
            case ' ': { // Skip spaces
                ++index[0];
                break;
            }
            default: { // New variable
                char variableName = string.charAt(index[0]++);
                terms.add(new LambdaVariable(null, LevelManager.convertVariableToColor(variableName), false));
                break;
            }
            }
        }
        if (closingParanthesis[0] == true
                && depth == lastParenthesisOpenDepth + 1) {
            closingParanthesis[0] = false;
        }
        // Return
        if (terms.isEmpty()) {
            throw new ParseException("String cannot be empty!", index[0]);
        } else if (terms.size() == 1) {
            return terms.get(0);
        } else {
            // Construct application hierarchy
            LambdaApplication current = new LambdaApplication(null, false, false);
            current.setLeft(terms.get(0));
            current.setRight(terms.get(1));
            for (int i = 2; i < terms.size(); i++) {
                // New application as parent of last application
                LambdaApplication application = new LambdaApplication(null,
                        false, false);
                application.setLeft(current);
                application.setRight(terms.get(i));

                current = application;
            }
            return current;
        }
    }

    /**
     * Returns the root of the given lambda term tree.
     *
     * @param term
     *            the lambda term tree
     * @return the root of the given lambda term tree
     * @throws IllegalArgumentException
     *             if term is null
     */
    public static LambdaTerm getRoot(LambdaTerm term) {
        if (term == null) {
            throw new IllegalArgumentException("Lambda term cannot be null!");
        }
        return term.getParent() == null ? term : getRoot(term.getParent());
    }
    
    /**
     * Prevents the instantiation of this class.
     */
    private LambdaUtils() {
    }
}
