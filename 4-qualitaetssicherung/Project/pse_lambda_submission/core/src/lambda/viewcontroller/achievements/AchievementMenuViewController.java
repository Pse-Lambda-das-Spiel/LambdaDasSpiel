package lambda.viewcontroller.achievements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;

import lambda.model.achievements.AchievementManager;
import lambda.model.profiles.ProfileManager;
import lambda.viewcontroller.AudioManager;
import lambda.viewcontroller.StageViewController;
import lambda.viewcontroller.mainmenu.MainMenuViewController;

/**
 * Represents the achievement menu.
 * 
 * @author Robert Hochweiss
 *
 */
public class AchievementMenuViewController extends StageViewController {

    private static AssetManager manager;
    private static final int ACHIEVEMENTS_PER_ROW = 5;
    private AchievementManager achievementManager;
    private List<AchievementViewController> achievementVCList;
    private Map<String, Label> labelMap;
    private Label titleLabel;
    private Table achievementTable;
    private static Skin skin;
    private final float buttonSize;
    private final float space;

    /**
     * Creates a new instance of this class.
     */
    public AchievementMenuViewController() {
        achievementVCList = new ArrayList<>();
        labelMap = new HashMap<>();
        achievementManager = AchievementManager.getManager();
        achievementManager.loadAchievements(achievementVCList);
        buttonSize = getStage().getWidth() / 10;
        space = getStage().getHeight() / 36;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changedProfile() {
        // It also adds all achievements as observer for the new StatisticModel
        achievementManager.resetAchievements(manager);
        /*
         * Since the achievements are not to be saved and the statistic values
         * are already set after a profile change this has to be called after
         * every profile change
         */
        achievementManager.checkAllAchievements();
        // Set the label texts according to the current language
        I18NBundle stringBundle = manager.get(ProfileManager.getManager()
                .getCurrentProfile().getLanguage(), I18NBundle.class);
        titleLabel.setText(stringBundle.get("achievementMenuTitle"));
        for (String str : achievementManager.getAchievementTypeList()) {
            labelMap.get(str).setText(stringBundle.get(str + "Label") + ":");
        }

    }

    /**
     * Returns the style for an image button with the given icon as image.
     * 
     * @param icon
     *            the identifier of the icon in the texture atlas
     * @return the image button style
     */
    public static ImageButtonStyle getImageButtonStyle(String icon) {
        ImageButtonStyle style = new ImageButtonStyle(
                skin.get(ImageButtonStyle.class));
        style.imageUp = skin.getDrawable(icon);
        return style;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void queueAssets(AssetManager assets) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(final AssetManager manager) {
        Image background = new Image(manager.get(
                "data/backgrounds/default.png", Texture.class));
        background.setWidth(getStage().getWidth());
        background.setHeight(getStage().getHeight());
        getStage().addActor(background);

        setLastViewController(MainMenuViewController.class);
        AchievementMenuViewController.manager = manager;
        ProfileManager.getManager().addObserver(this);
        skin = manager.get("data/skins/MasterSkin.json", Skin.class);
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        getStage().addActor(mainTable);
        titleLabel = new Label("Initial string", skin, "roboto");
        achievementTable = new Table();
        achievementTable.pad(space);
        achievementTable.defaults().space(space * 1.5f, space * 1.5f,
                space * 2.5f, space * 1.5f);
        int tmpIndex = 0;
        List<String> achievementTypeList = achievementManager
                .getAchievementTypeList();
        for (int i = 0; i < achievementTypeList.size(); i++) {
            // the labels have to be stored to change their text at a later time
            Label label = new Label("Initial string", skin, "robotoLight");
            // only tmp, does not look so nice with scaling
            labelMap.put(achievementTypeList.get(i), label);
            achievementTable.add(label).center().colspan(ACHIEVEMENTS_PER_ROW);
            int n = achievementManager.getAchievementNumberPerType().get(
                    achievementTypeList.get(i));
            for (int j = 0; j < n; j++) {
                if (j % ACHIEVEMENTS_PER_ROW == 0) {
                    achievementTable.row();
                }
                AchievementViewController achievementVC = achievementVCList
                        .get(tmpIndex + j);
                achievementVC.initializeButton();
                achievementVC.addListener(new AchievementClickListener());
                achievementTable.add(achievementVC).size(buttonSize);
            }
            achievementTable.row();
            tmpIndex += n;
        }
        ScrollPane scrollPane = new ScrollPane(achievementTable);
        ImageButton back = new ImageButton(getImageButtonStyle("back"));
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.playSound("buttonClick");
                getGame().setScreen(MainMenuViewController.class);
            }
        });
        mainTable.add(titleLabel).colspan(2).center().row();
        mainTable.add(back).align(Align.bottomLeft).pad(space).size(buttonSize);
        mainTable.add(scrollPane).expand().fill().left();
    }

    private class AchievementClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            AudioManager.playSound("buttonClick");
            final AchievementViewController clickedActor = (AchievementViewController) event
                    .getListenerActor();
            final float height = getStage().getHeight();
            final float width = getStage().getWidth();
            new Dialog("", skin) {
                {
                    clear();
                    pad(height / 20);
                    // getImage() removes the image from its button so the
                    // button has to be copied before that
                    Image image = (new ImageButton(clickedActor.getStyle())
                            .getImage());
                    image.sizeBy(buttonSize * 1.5f);
                    add(image).top().pad(space * 1.5f, space / 2, space * 1.5f,
                            space * 1.5f);
                    Label label = new Label(clickedActor.getText(), skin,
                            "robotoLight");
                    label.setWrap(true);
                    add(label).width(width / 2);
                    addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            setVisible(false);
                            hide();
                        }
                    });
                }
            }.show(getStage());
        }
    }

}
