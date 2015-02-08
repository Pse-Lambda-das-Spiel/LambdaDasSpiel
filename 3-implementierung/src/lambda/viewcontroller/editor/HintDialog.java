package lambda.viewcontroller.editor;

import lambda.model.levels.LevelContext;
import lambda.viewcontroller.lambdaterm.LambdaTermViewController;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class HintDialog extends Dialog {

    public HintDialog(Skin skin, LevelContext context, float stageWidth, float stageHeight) {
        super("", skin);
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setVisible(false);
                hide();
            }
        });
    }
}
