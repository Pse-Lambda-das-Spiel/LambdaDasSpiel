package lambda.viewcontroller.editor;

import lambda.model.levels.LevelContext;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class TargetDialog extends Dialog {

    public TargetDialog(Skin skin, LevelContext context, float stageWidth, float stageHeight) {
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
