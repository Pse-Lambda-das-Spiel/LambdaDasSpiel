package lambda.model.lambdaterm;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import lambda.model.lambdaterm.visitor.CopyVisitor;
import lambda.model.lambdaterm.visitor.FrontInserter;
import lambda.model.lambdaterm.visitor.IsAlphaEquivalentVisitor;
import lambda.model.lambdaterm.visitor.SiblingInserter;
import lambda.model.lambdaterm.visitor.ToStringVisitor;
import lambda.model.lambdaterm.visitor.strategy.BetaReductionVisitor;
import lambda.model.lambdaterm.visitor.strategy.ReductionStrategyApplicativeOrder;
import lambda.model.lambdaterm.visitor.strategy.ReductionStrategyCallByName;
import lambda.model.lambdaterm.visitor.strategy.ReductionStrategyCallByValue;
import lambda.model.lambdaterm.visitor.strategy.ReductionStrategyNormalOrder;
import lambda.model.levels.LevelManager;

import org.junit.After;
import org.junit.AfterClass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.graphics.Color;
import com.libgdxtesting.GdxTestRunner;

/**
 * Tests lambda term visitors.
 * 
 * @author Florian Fervers
 */
@RunWith(GdxTestRunner.class)
public class LambdaTermVisitorTest {
	static Set<Color> alphaConversionColors;
	
    @BeforeClass
    public static void setUpClass() {
    	LevelManager.getLevelManager();
    	alphaConversionColors = new HashSet<>(LevelManager.getAllColors());
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
        LambdaTerm term = LambdaUtils.fromString("/x.x (y z t)");
        assertEquals("String - LambdaTerm conversion not correct!", term, LambdaUtils.fromString(term.accept(new ToStringVisitor())));
    }
    
    /**
     * Checks if LambdaTerm.equals returns true for equal terms.
     * 
     * @throws ParseException if the string could not be parsed
     */
    @Test
    public void testLambdaTermEquals() throws ParseException {
        String string = "/x.x y y /z.z";
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
    
    /**
     * Checks if IsAlphaEquivalentVisitor retuns true for alpha equivalent terms.
     * 
     * @throws ParseException if the string could not be parsed
     */
    @Test
    public void testAlphaEquivalence() throws ParseException {
        LambdaTerm term1 = LambdaUtils.fromString("(/x.x /y.y x) y");
        LambdaTerm term2 = LambdaUtils.fromString("(/x.x /y.y x) y");
        assertTrue("IsAlphaEquivalentVisitor returns false for equal terms!", term1.accept(new IsAlphaEquivalentVisitor(term2)));
        
        term1 = LambdaUtils.fromString("(/x.x /y.y x) y");
        term2 = LambdaUtils.fromString("(/v.v /z.z v) y");
        assertTrue("IsAlphaEquivalentVisitor returns false for alpha equivalent terms!", term1.accept(new IsAlphaEquivalentVisitor(term2)));
        
        term1 = LambdaUtils.fromString("(/x.x /y.y x) y");
        term2 = LambdaUtils.fromString("(/v.v /z.z v) z");
        assertTrue("IsAlphaEquivalentVisitor returns true for terms with different free variables!", !term1.accept(new IsAlphaEquivalentVisitor(term2)));
        
        term1 = LambdaUtils.fromString("(/x.x /y.y x) y");
        term2 = LambdaUtils.fromString("(/t.t /z.z s) v");
        assertTrue("IsAlphaEquivalentVisitor returns true for terms where bound variable is not correctly translated!", !term1.accept(new IsAlphaEquivalentVisitor(term2)));
    }
    
    /**
     * Performs a beta reduction and checks if an alpha conversion was performed.
     * 
     * @throws ParseException if the string could not be parsed
     */
    @Test
    public void testAlphaConversionInBetaReduction() throws ParseException {
        LambdaTerm term = LambdaUtils.fromString("(/x.x /y.y x) y");
        BetaReductionVisitor normalOrder = new ReductionStrategyNormalOrder();
        normalOrder.setAlphaConversionColors(alphaConversionColors);
        term.accept(normalOrder);
        LambdaTerm reduced = LambdaUtils.fromString("y /v.v y");

        assertTrue("No alpha conversion performed in beta reduction where free variable should stay free!", term.accept(new IsAlphaEquivalentVisitor(reduced)));
    }
    
    /**
     * Performs beta reductions with normal order strategy and checks if the result are correct.
     * 
     * @throws ParseException if the string could not be parsed
     */
    @Test
    public void testBetaReductionNormalStrategy() throws ParseException {
        LambdaTerm term = LambdaUtils.fromString("(/z.z z) (/z.z z)"); // Non-terminating term
        BetaReductionVisitor normalOrder = new ReductionStrategyNormalOrder();
        normalOrder.setAlphaConversionColors(alphaConversionColors);
        assertEquals("Term that beta-reduces to itself is modified by ReductionStrategyNormalOrder!", term, term.accept(new CopyVisitor()).accept(normalOrder));
       
        
        term = LambdaUtils.fromString("(/x.y) ((/z.z z) (/z.z z))"); // Terminating in normal order
        assertEquals("Normal order beta reduction not correct!", LambdaUtils.fromString("y"), term.accept(new CopyVisitor()).accept(normalOrder));
    }
    
    /**
     * Performs beta reductions with applicative order strategy and checks if the result are correct.
     * 
     * @throws ParseException if the string could not be parsed
     */
    @Test
    public void testBetaReductionApplicativeStrategy() throws ParseException {
    	 BetaReductionVisitor applicativeOrder = new ReductionStrategyApplicativeOrder();
         applicativeOrder.setAlphaConversionColors(alphaConversionColors);
        LambdaTerm term = LambdaUtils.fromString("(/z.z z) (/z.z z)"); // Non-terminating term
        assertEquals("Term that beta-reduces to itself is modified by ReductionStrategyApplicativeOrder!", term, term.accept(new CopyVisitor()).accept(applicativeOrder));
        
        term = LambdaUtils.fromString("(/x.y) ((/z.z z) (/z.z z))"); // Non-terminating in applicative order
        assertEquals("Applicative order beta reduction not correct!", term, term.accept(new CopyVisitor()).accept(applicativeOrder));
    }
    
    /**
     * Performs beta reductions with call-by-value strategy and checks if the result are correct.
     * 
     * @throws ParseException if the string could not be parsed
     */
    @Test
    public void testBetaReductionCallByValueStrategy() throws ParseException {
        LambdaTerm term = LambdaUtils.fromString("(/x.x) (y y)");
        BetaReductionVisitor callByValue = new ReductionStrategyCallByValue();
       callByValue.setAlphaConversionColors(alphaConversionColors);
        assertEquals("Term should not be reduced by call-by-value strategy!", term, term.accept(new CopyVisitor()).accept(callByValue));
        
        term = LambdaUtils.fromString("/x./y.y x");
        assertEquals("Term should not be reduced by call-by-value strategy!", term, term.accept(new CopyVisitor()).accept(callByValue));
    
        term = LambdaUtils.fromString("(/x.x) ((/y.y) (/z. (/v.v) z))");
        term.accept(callByValue);
        assertTrue("Call-by-value beta reduction not correct!", term.accept(new IsAlphaEquivalentVisitor(LambdaUtils.fromString("(/x.x) ((/y.y) (/z. (/v.v) z))"))));
        term.accept(new ReductionStrategyCallByValue());
        assertTrue("Call-by-value beta reduction not correct!", term.accept(new IsAlphaEquivalentVisitor(LambdaUtils.fromString("(/z. (/v.v) z)"))));
        term.accept(new ReductionStrategyCallByValue());
        assertTrue("Call-by-value beta reduction not correct!", term.accept(new IsAlphaEquivalentVisitor(LambdaUtils.fromString("(/z. (/v.v) z)"))));
    }
    
    /**
     * Performs beta reductions with call-by-name strategy and checks if the result are correct.
     * 
     * @throws ParseException if the string could not be parsed
     */
    @Test
    public void testBetaReductionCallByNameStrategy() throws ParseException {
        LambdaTerm term = LambdaUtils.fromString("/x./y.y x");
        BetaReductionVisitor callByName = new ReductionStrategyCallByName();
        callByName.setAlphaConversionColors(alphaConversionColors);
        assertEquals("Term should not be reduced by call-by-name strategy!", term, term.accept(new CopyVisitor()).accept(callByName));
    
        term = LambdaUtils.fromString("(/x.x) ((/v.v) (/z. (/y.y) z))");
        term.accept(callByName);
        assertTrue("Call-by-name beta reduction not correct!", term.accept(new IsAlphaEquivalentVisitor(LambdaUtils.fromString("(/x.x) ((/v.v) (/z. (/y.y) z))"))));
        term.accept(callByName);
        assertTrue("Call-by-name beta reduction not correct!", term.accept(new IsAlphaEquivalentVisitor(LambdaUtils.fromString("(/z. (/y.y) z)"))));
        term.accept(callByName);
        assertTrue("Call-by-name beta reduction not correct!", term.accept(new IsAlphaEquivalentVisitor(LambdaUtils.fromString("(/z. (/y.y) z)"))));
    }
    
    /**
     * Tests the sibling inserter.
     * 
     * @throws ParseException if the string could not be parsed
     */
    @Test
    public void testSiblingInserter() throws ParseException {
        LambdaRoot root = LambdaUtils.fromString("x");
        root.getChild().accept(new SiblingInserter(LambdaUtils.fromString("y").getChild(), true));
        assertEquals("Sibling was inserted incorrectly!", root, LambdaUtils.fromString("y x"));
        root.getChild().accept(new SiblingInserter(LambdaUtils.fromString("z").getChild(), false));
        assertEquals("Sibling was inserted incorrectly!", root, LambdaUtils.fromString("y x z"));
     }
    
    /**
     * Tests the front inserter.
     * 
     * @throws ParseException if the string could not be parsed
     */
    @Test
    public void testFrontInserter() throws ParseException {
        LambdaRoot root = LambdaUtils.fromString("x");
        root.accept(new FrontInserter(LambdaUtils.fromString("y").getChild()));
        assertEquals("Term was inserted incorrectly at front!", root, LambdaUtils.fromString("y"));
        
        root = LambdaUtils.fromString("x y");
        root.getChild().accept(new FrontInserter(LambdaUtils.fromString("z").getChild()));
        assertEquals("Term was inserted incorrectly at front!", root, LambdaUtils.fromString("z y"));
    }
}
