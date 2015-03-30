package lambda.model.statistics;

import java.util.Map;

import com.badlogic.gdx.graphics.Color;

import lambda.model.editormode.EditorModelObserver;
import lambda.model.lambdaterm.LambdaAbstraction;
import lambda.model.lambdaterm.LambdaTerm;
import lambda.model.lambdaterm.LambdaTermObserver;
import lambda.model.lambdaterm.LambdaValue;
import lambda.model.lambdaterm.LambdaVariable;
import lambda.model.lambdaterm.visitor.LambdaValueCounterVisitor;
import lambda.model.levels.ReductionStrategy;
import lambda.model.profiles.ProfileManager;
import lambda.model.reductionmode.ReductionModelObserver;

/**
 * This class saves the delta statistic data gained while playing a level. It is
 * also responsible for processing this data and saving it in the
 * StatisticModel. It only saves the statistic data if the level is finished.
 * 
 * @author Robert Hochweiss
 */
public class DeltaStatisticProcessor implements EditorModelObserver,
        ReductionModelObserver, LambdaTermObserver {

    private boolean hintUsed;
    private int gemsEnchanted;
    private int lambsEnchanted;
    private int gemsPlaced;
    private int lambsPlaced;
    private long startTime;
    private long endTime;
    private boolean inEditorMode;

    /**
     * Creates a new DeltaStatisticProcessor.
     */
    public DeltaStatisticProcessor() {
    }

    private void reset() {
        inEditorMode = true;
        hintUsed = false;
        gemsEnchanted = 0;
        lambsEnchanted = 0;
        gemsPlaced = 0;
        lambsPlaced = 0;
        startTime = 0l;
        endTime = 0l;
    }

    @Override
    public void replaceTerm(LambdaTerm oldTerm, LambdaTerm newTerm) {
        if ((oldTerm == null) && (newTerm == null)) {
            throw new IllegalArgumentException(
                    "oldterm and newTerm cannot be both null!");
        }
        if (inEditorMode) {
            // used for drag&drop placing
            if (newTerm != null) {
                LambdaValueCounterVisitor visitor = new LambdaValueCounterVisitor();
                Map<String, Integer> resultMapNew = newTerm.accept(visitor);
                if (oldTerm != null) {
                    LambdaValueCounterVisitor visitor2 = new LambdaValueCounterVisitor();
                    Map<String, Integer> resultMapOld = oldTerm
                            .accept(visitor2);
                    /*
                     * if the old term is not null the difference in the number
                     * of lambs and gems between the new and old lambda terms
                     * has to be calculated
                     */
                    int lambsResult = resultMapNew.get("lambs")
                            - resultMapOld.get("lambs");
                    lambsPlaced += lambsResult;
                    int gemsResult = resultMapNew.get("gems")
                            - resultMapOld.get("gems");
                    gemsPlaced += gemsResult;

                } else {
                    lambsPlaced += resultMapNew.get("lambs");
                    gemsPlaced += resultMapNew.get("gems");
                }
            }
        }
    }

    @Override
    public void levelStarted() {
        reset();
        startTime = System.currentTimeMillis();
    }

    @Override
    public void hintUsed() {
        hintUsed = true;
    }

    @Override
    public void termChanged(LambdaTerm term) {
        if (term == null) {
            throw new IllegalArgumentException("term cannot be null!");
        }
        term.addObserver(this);
    }

    @Override
    public void variableReplaced(LambdaVariable variable, LambdaTerm replacing) {
        if ((variable == null) || (replacing == null)) {
            throw new IllegalArgumentException(
                    "neither variable nor replacing can be null!");
        }
        if (inEditorMode) {
            inEditorMode = false;
        }
        LambdaValueCounterVisitor visitor = new LambdaValueCounterVisitor();
        Map<String, Integer> resultMap = replacing.accept(visitor);
        lambsEnchanted += resultMap.get("lambs");
        gemsEnchanted += resultMap.get("gems");
    }

    @Override
    public void reductionFinished(boolean levelComplete) {
        endTime = System.currentTimeMillis();
        StatisticModel statistic = ProfileManager.getManager()
                .getCurrentProfile().getStatistics();
        long diffTime = endTime - startTime;
        // the time has to be stored in seconds in the StatisticModel
        diffTime /= 1000;
        // update the statistic data
        statistic.setTimePlayed(statistic.getTimePlayed() + diffTime);
        statistic.setLevelTries(statistic.getLevelTries() + 1);
        if (levelComplete) {
            statistic.setSuccessfulLevelTries(statistic
                    .getSuccessfulLevelTries() + 1);
            if (!hintUsed) {
                statistic.setHintsNotUsed(statistic.getHintsNotUsed() + 1);
            }
        }
        // only update these values if they are bigger, so that the statistic
        // observers do not get notified needlessly
        if (gemsEnchanted > 0) {
            statistic.setGemsEnchanted(statistic.getGemsEnchanted()
                    + gemsEnchanted);
        }
        if (lambsEnchanted > 0) {
            statistic.setLambsEnchanted(statistic.getLambsEnchanted()
                    + lambsEnchanted);
        }
        if (gemsPlaced > 0) {
            statistic.setGemsPlaced(statistic.getGemsPlaced() + gemsPlaced);
        }
        if (lambsPlaced > 0) {
            statistic.setLambsPlaced(statistic.getLambsPlaced() + lambsPlaced);
        }
        // update per level values if the new ones are bigger
        if (gemsEnchanted > statistic.getGemsEnchantedPerLevel()) {
            statistic.setGemsEnchantedPerLevel(statistic
                    .getGemsEnchantedPerLevel() + gemsEnchanted);
        }
        if (lambsEnchanted > statistic.getLambsEnchantedPerLevel()) {
            statistic.setLambsEnchantedPerLevel(statistic
                    .getLambsEnchantedPerLevel() + lambsEnchanted);
        }
        if (gemsPlaced > statistic.getGemsPlacedPerLevel()) {
            statistic.setGemsPlacedPerLevel(statistic.getGemsPlacedPerLevel()
                    + gemsPlaced);
        }
        if (lambsPlaced > statistic.getLambsPlacedPerLevel()) {
            statistic.setLambsPlacedPerLevel(statistic.getLambsPlacedPerLevel()
                    + lambsPlaced);
        }
    }

    @Override
    public void setColor(LambdaValue term, Color color) {
    }

    @Override
    public void alphaConverted(LambdaValue term, Color color) {
    }

    @Override
    public void alphaConversionFinished() {
    }

    @Override
    public void applicationStarted(LambdaAbstraction abstraction,
            LambdaTerm applicant) {
    }

    @Override
    public void removingApplicant(LambdaTerm applicant) {
    }

    @Override
    public void stateChanged(boolean busy, int historySize, boolean paused,
            boolean pauseRequested) {
    }

    @Override
    public void strategyChanged(ReductionStrategy strategy) {
    }

}
