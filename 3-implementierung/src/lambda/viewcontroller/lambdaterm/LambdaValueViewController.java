package lambda.viewcontroller.lambdaterm;

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
public abstract class LambdaValueViewController extends LambdaNodeViewController {
    /**
     * The color of this variable.
     */
    private Color color;

    /**
     * Creates a new instance of LambdaValueViewController.
     *
     * @param linkedTerm the value displayed by this node
     * @param parent the parent viewcontroller node
     * @param viewController the viewcontroller on which this node will be
     * displayed
     * @param canHaveChildren true if this node can have children, false
     * otherwise
     */
    public LambdaValueViewController(LambdaValue linkedTerm, LambdaNodeViewController parent, final LambdaTermViewController viewController, boolean canHaveChildren) {
        super(linkedTerm, parent, viewController, canHaveChildren);
        color = linkedTerm.getColor();
        if (!linkedTerm.isLocked()) {
            this.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (!getLinkedTerm().isLocked() && viewController.isEditable()) {
                        final Skin dialogSkin = viewController.getAssets().get(
                                "data/skins/DialogTemp.json", Skin.class);
                        final float height = getStage().getHeight();
                        new Dialog("", dialogSkin) {
                            {
                                clear();
                                List<Color> colors = viewController.getContext()
                                        .getLevelModel().getAvailableColors();
                                int size = (int) Math.ceil(Math.sqrt(colors.size()));
                                pad(height / 72 * size);
                                int i = 0;
                                for (final Color color : colors) {
                                    if (i++ % size == 0) {
                                        row().size(height / 9).space(10);
                                    }
                                    ImageButton colorButton = new ImageButton(
                                            dialogSkin, "colorButton");
                                    colorButton.setColor(color);
                                    colorButton.addListener(new ClickListener() {
                                        @Override
                                        public void clicked(InputEvent event,
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
                                    public void clicked(InputEvent event, float x, float y) {
                                        if (!(0 < x && 0 < y && x < dialog.getWidth() && y < dialog.getHeight())) {
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
     * Returns the color of this value.
     *
     * @return the color of this value
     */
    public Color getLambdaColor() {
        return color;
    }

    /**
     * Sets the color of this value.
     *
     * @param color the new color
     */
    public void setLambdaColor(Color color) {
        this.color = color;
    }

}
