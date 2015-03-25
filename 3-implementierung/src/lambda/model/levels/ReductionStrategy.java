package lambda.model.levels;

import lambda.Supplier;
import lambda.model.lambdaterm.visitor.strategy.BetaReductionVisitor;
import lambda.model.lambdaterm.visitor.strategy.ReductionStrategyApplicativeOrder;
import lambda.model.lambdaterm.visitor.strategy.ReductionStrategyCallByName;
import lambda.model.lambdaterm.visitor.strategy.ReductionStrategyCallByValue;
import lambda.model.lambdaterm.visitor.strategy.ReductionStrategyNormalOrder;

/**
 * Enumeration of the different reduction-strategies.
 *
 * @author Kay Schmitteckert, Florian Fervers
 */
public enum ReductionStrategy {
    /**
     * Represents the reduction strategy "normal order".
     */
    NORMAL_ORDER(new Supplier<BetaReductionVisitor>() {
        @Override
        public BetaReductionVisitor get() {
            return new ReductionStrategyNormalOrder();
        }
    }),

    /**
     * Represents the reduction strategy "applicative order".
     */
    APPLICATIVE_ORDER(new Supplier<BetaReductionVisitor>() {
        @Override
        public BetaReductionVisitor get() {
            return new ReductionStrategyApplicativeOrder();
        }
    }),

    /**
     * Represents the reduction strategy "call by name".
     */
    CALL_BY_NAME(new Supplier<BetaReductionVisitor>() {
        @Override
        public BetaReductionVisitor get() {
            return new ReductionStrategyCallByName();
        }
    }),

    /**
     * Represents the reduction strategy "call by value".
     */
    CALL_BY_VALUE(new Supplier<BetaReductionVisitor>() {
        @Override
        public BetaReductionVisitor get() {
            return new ReductionStrategyCallByValue();
        }
    });

    /**
     * Creates new instances of the reduction visitor for this strategy.
     */
    private final Supplier<BetaReductionVisitor> visitorCreator;

    /**
     * Creates a new reduction strategy.
     * 
     * @param visitorCreator
     *            creates new instances of the reduction visitor for this
     *            strategy
     */
    private ReductionStrategy(Supplier<BetaReductionVisitor> visitorCreator) {
        this.visitorCreator = visitorCreator;
    }

    /**
     * Returns the reduction visitor for this reduction strategy.
     * 
     * @return the reduction visitor for this reduction strategy
     */
    public BetaReductionVisitor toVisitor() {
        return visitorCreator.get();
    }
}
