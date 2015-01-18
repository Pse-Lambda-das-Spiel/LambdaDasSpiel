package lambda.model.lambdaterm;

import java.text.ParseException;
import lambda.model.lambdaterm.visitor.CopyVisitor;
import lambda.model.lambdaterm.visitor.ToStringVisitor;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests lambda term visitors.
 * 
 * @author Florian Fervers
 */
public class LambdaTermVisitorTest {
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Converts a string to lambda term and back and checks if the initial and final string are equal.
     * 
     * @throws ParseException if the string could not be parsed
     */
    @Test
    public void testStringLambdaTermConversion() throws ParseException {
        String string = "/x.x (y z t)";
        assertEquals("String - LambdaTerm conversion not correct!", LambdaUtils.fromString(string).accept(new ToStringVisitor()), string);
    }
    
    /**
     * Checks if LambdaTerm.equals returns true for equal terms.
     * 
     * @throws ParseException if the string could not be parsed
     */
    @Test
    public void testLambdaTermEquals() throws ParseException {
        String string = "/x.x y y";
        assertTrue("LambdaTerm.equals returns false for equal lambda terms!", LambdaUtils.fromString(string).equals(LambdaUtils.fromString(string)));
    }
    
    /**
     * Checks if a copied lambda term is equal to the original one.
     * 
     * @throws ParseException if the string could not be parsed
     */
    @Test
    public void testCopyVisitor() throws ParseException {
        LambdaTerm term = LambdaUtils.fromString("/x.x y y");
        assertEquals("CopyVisitor does not return an exact copy!", term.accept(new CopyVisitor()), term);
    }
}
