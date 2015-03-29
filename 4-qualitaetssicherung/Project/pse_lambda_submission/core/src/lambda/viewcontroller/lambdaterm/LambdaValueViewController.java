package lambda.viewcontroller.lambdaterm;

import com.badlogic.gdx.Gdx;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import lambda.model.lambdaterm.LambdaValue;

/**
 * Represents a viewcontroller value node (abstraction or variable) in a
 * LambdaTermViewController.
 *
 * @author Florian Fervers
 */
public abstract class LambdaValueViewController extends
        LambdaNodeViewController {
    /**
     * The duration of the color change animation.
     */
    public static final float COLOR_ANIMATION_DURATION = 0.5f;
    /**
     * The color of this variable before the animation started.
     */
    private Color initialColor;
    /**
     * The target color of this variable after the animation.
     */
    private Color targetColor;
    /**
     * The time since the start of the animation or zero if the animation hasn't
     * started yet.
     */
    private float colorStateTime;

    /**
     * Creates a new instance of LambdaValueViewController.
     *
     * @param linkedTerm
     *            the value displayed by this node
     * @param parent
     *            the parent viewcontroller node
     * @param viewController
     *            the viewcontroller on which this node will be displayed
     * @param canHaveChildren
     *            true if this node can have children, false otherwise
     */
    public LambdaValueViewController(LambdaValue linkedTerm,
            LambdaNodeViewController parent,
            final LambdaTermViewController viewController,
            boolean canHaveChildren) {
        super(linkedTerm, parent, viewController, canHaveChildren);
        initialColor = linkedTerm.getColor();
        targetColor = linkedTerm.getColor();
        colorStateTime = COLOR_ANIMATION_DURATION;
        if (!linkedTerm.isLocked() && viewController.isEditable()) {
            this.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (!getLinkedTerm().isLocked()
                            && viewController.isEditable()) {
                        final Skin dialogSkin = viewController.getAssets().get(
                                "data/skins/DialogTemp.json", Skin.class);
                        final float height = getStage().getHeight();
                        new Dialog("", dialogSkin) {
                            {
                                clear();
                                List<Color> colors = viewController
                                        .getContext().getLevelModel()
                                        .getAvailableColors();
                                int size = (int) Math.ceil(Math.sqrt(colors
                                        .size()));
                                pad(height / 72 * size);
                                int i = 0;
                                for (final Color color : colors) {
                                    if (i++ % size == 0) {
                                        row().size(height / 9).space(10);
                                    }
                                    ImageButton colorButton = new ImageButton(
                                            dialogSkin, "colorButton");
                                    colorButton.setColor(color);
                                    colorButton
                                            .addListener(new ClickListener() {
                                                @Override
                                                public void clicked(
                                                        InputEvent event,
                                                        float x, float y) {
                                                    ((LambdaValue) getLinkedTerm())
                                                            .setColor(color);
                                                    remove();
                                                }
                                            });
                                    add(colorButton);
                                }
                                final Dialog dialog = this;
                                addListener(new ClickListener() {
                                    @Override
                                    public void clicked(InputEvent event,
                                            float x, float y) {
                                        if (!(0 < x && 0 < y
                                                && x < dialog.getWidth() && y < dialog
                                                .getHeight())) {
                                            remove();
                                        }
                                    }
                                });
                            }
                        }.show(getStage());
                        getViewController().getDragAndDrop().resetTouchState();
                    }
                }
            });
        }
    }

    /**
     * Updates the color animation.
     */
    protected synchronized void updateColorAnimation() {
        synchronized (getViewController()) {
            if (colorStateTime < COLOR_ANIMATION_DURATION) {
                colorStateTime += Gdx.graphics.getDeltaTime();
                if (isColorAnimationFinished()) {
                    getViewController().notifyAll();
                }
            }
        }
    }

    /**
     * Returns the color of this value.
     *
     * @return the color of this value
     */
    public synchronized Color getCurrentColor() {
        Color c0 = initialColor.cpy().mul(
                1.0f - colorStateTime / COLOR_ANIMATION_DURATION);
        Color c1 = targetColor.cpy().mul(
                colorStateTime / COLOR_ANIMATION_DURATION);
        return c0.add(c1);
    }

    /**
     * Sets the target color of this value and starts the animation.
     *
     * @param color
     *            the new color
     * @param animated
     *            true if the color change should be animated, false otherwise
     */
    public synchronized void setTargetColor(Color color, boolean animated) {
        colorStateTime = animated ? 0.0f : COLOR_ANIMATION_DURATION;
        initialColor = getCurrentColor();
        targetColor = color;
    }

    /**
     * Returns whether the color change animation is fininshed.
     *
     * @return true if the color change animation is finished, false otherwise
     */
    public synchronized boolean isColorAnimationFinished() {
        return colorStateTime >= COLOR_ANIMATION_DURATION;
    }
}
