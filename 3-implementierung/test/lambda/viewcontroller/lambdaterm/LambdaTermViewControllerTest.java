package lambda.viewcontroller.lambdaterm;

import java.text.ParseException;
import lambda.model.lambdaterm.LambdaRoot;
import lambda.model.lambdaterm.LambdaUtils;
import lambda.model.lambdaterm.visitor.strategy.ReductionStrategyNormalOrder;
import lambda.model.levels.LevelContext;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * A class for testing the LambdaTermViewController including LambdaNodeViewController and the corresponding visitors.
 * 
 * @author Florian Fervers
 */
public class LambdaTermViewControllerTest {
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
     * Convert a lambda term model to lambda term viewcontroller, then reduce the model and convert the final lambda term to viewcontroller again. Compare both viewcontrollers for equality.
     * @throws ParseException 
     */
    @Test
    public void testBetaReduction() throws ParseException {
        LambdaRoot term = LambdaUtils.fromString("(/a.a f g h) ((/b.b) (/z. (/c.c) z)) (/f.f f) r");
        
        LambdaTermViewController vc = LambdaTermViewController.build(term, false, new LevelContext(null), null, false); // TODO Level context
        
        ReductionStrategyNormalOrder strategy = new ReductionStrategyNormalOrder();
        do {
            strategy.reset();
            term.accept(strategy);
        } while (strategy.hasReduced());
        
        assertEquals("Updated ViewController and new ViewController for lambda term are different!", vc,
                LambdaTermViewController.build(term, false, new LevelContext(null), null, false));
    }
}
