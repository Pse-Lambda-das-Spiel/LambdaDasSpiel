package lambda.viewcontroller.profiles;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;

import lambda.model.profiles.ProfileManager;
import lambda.viewcontroller.AudioManager;
import lambda.viewcontroller.StageViewController;
import lambda.viewcontroller.mainmenu.MainMenuViewController;

/**
 * Represents the screen of the profile-selection. It allows the user to choose,
 * add, remove or edit profiles.
 * 
 * @author Kai Fieger
 */
public class ProfileSelection extends StageViewController {

    private final String skinJson = "data/skins/ProfileSelectionSkin.json";
    private List<TextButton> profileButtons;
    private List<ImageButton> editButtons;
    private ImageButton addButton;
    private AssetManager manager;
    private final float space;

    /**
     * Creates a object of the class without initializing the screen.
     */
    public ProfileSelection() {
        ProfileManager.getManager().addObserver(this);
        space = getStage().getWidth() / 64;
    }

    @Override
    public void queueAssets(AssetManager assets) {
        assets.load(skinJson, Skin.class, new SkinLoader.SkinParameter(
                "data/skins/MasterSkin.atlas"));
        assets.load("data/skins/DialogTemp.json", Skin.class,
                new SkinLoader.SkinParameter("data/skins/MasterSkin.atlas"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show() {
        InputProcessor backProcessor = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Keys.BACK) {
                    removeLastDialog();
                    final Skin dialogSkin = manager.get(
                            "data/skins/DialogTemp.json", Skin.class);
                    final float size = getStage().getHeight() / 5;
                    showDialog(new Dialog("", dialogSkin) {
                        {
                            clear();
                            pad(space * 3);
                            String exitString = "";
                            // English as standard language if no profile was
                            // selected
                            if (ProfileManager.getManager().getCurrentProfile() == null) {
                                exitString = manager.get(
                                        "data/i18n/StringBundle_en",
                                        I18NBundle.class).get("exitString");
                            } else {
                                exitString = manager.get(
                                        ProfileManager.getManager()
                                                .getCurrentProfile()
                                                .getLanguage(),
                                        I18NBundle.class).get("exitString");
                            }
                            Label nameLabel = new Label(exitString, dialogSkin);
                            add(nameLabel).colspan(2);
                            nameLabel.setFontScale(ProfileSelection.this.getStage().getHeight() / 720 * nameLabel.getFontScaleY());
                            row().space(10);
                            // yes
                            ImageButton yesButton = new ImageButton(dialogSkin,
                                    "yesButton");
                            yesButton.addListener(new ClickListener() {
                                @Override
                                public void clicked(InputEvent event, float x,
                                        float y) {
                                    AudioManager.playSound("buttonClick");
                                    System.exit(0);
                                }
                            });
                            add(yesButton).size(size);
                            // no
                            ImageButton noButton = new ImageButton(dialogSkin,
                                    "noButton");
                            noButton.addListener(new ClickListener() {
                                @Override
                                public void clicked(InputEvent event, float x,
                                        float y) {
                                    AudioManager.playSound("buttonClick");
                                    remove();
                                }
                            });
                            add(noButton).size(size);
                        }
                    });
                }
                return false;
            }
        };
        InputMultiplexer multiplexer = new InputMultiplexer(getStage(),
                backProcessor);
        Gdx.input.setInputProcessor(multiplexer);
        if (ProfileManager.getManager().getNames().size() == 0) {
            ProfileManager m = ProfileManager.getManager();
            m.createProfile();
            m.setCurrentProfile("");
            getGame().setScreen(ProfileEditLang.class);
        }
    }

    @Override
    public void pause() {
        super.pause();
        ProfileManager m = ProfileManager.getManager();
        if (m.getCurrentProfile() != null) {
            m.save(m.getCurrentProfile().getName());
        }
    }

    @Override
    public void create(final AssetManager manager) {
        Image background = new Image(manager.get(
                "data/backgrounds/default.png", Texture.class));
        background.setWidth(getStage().getWidth());
        background.setHeight(getStage().getHeight());
        getStage().addActor(background);

        // without this the background of all dialogs/the window's size is never
        // smaller than 600,400
        new Dialog("", manager.get("data/skins/DialogTemp.json", Skin.class)) {
            {
                this.getBackground().setMinWidth(0.0f);
                this.getBackground().setMinHeight(0.0f);
            }
        };
        this.manager = manager;
        profileButtons = new ArrayList<TextButton>();
        editButtons = new ArrayList<ImageButton>();
        Table profileView = new Table();
        getStage().addActor(profileView);
        profileView.setFillParent(true);
        // Profilebuttons + their edit buttons
        for (int i = 0; i < ProfileManager.MAX_NUMBER_OF_PROFILES; i++) {
            float height = getStage().getHeight() * 3 / 5
                    / ProfileManager.MAX_NUMBER_OF_PROFILES;
            profileView.row().height(height);
            TextButton pButton = new TextButton("", manager.get(skinJson,
                    Skin.class));
            pButton.getLabel().setFontScale(0.7f);
            profileView.add(pButton).width(getStage().getWidth() * 0.55f)
                    .space(space);
            pButton.getLabel().setFontScale(height / (720 * 3 / 5
                    / ProfileManager.MAX_NUMBER_OF_PROFILES) * pButton.getLabel().getFontScaleY());
            profileButtons.add(pButton);
            pButton.addListener(new SelectProfileClickListener());
            ImageButton eButton = new ImageButton(manager.get(skinJson,
                    Skin.class), "editButton");
            profileView.add(eButton).width(height).align(Align.top);
            editButtons.add(eButton);
            eButton.addListener(new EditProfileClickListener());
        }
        // addProfile-Button
        addButton = new ImageButton(manager.get(skinJson, Skin.class),
                "addButton");
        Container<ImageButton> buttonContainer = new Container<ImageButton>();
        buttonContainer.pad(space * 5 / 2).maxSize(getStage().getHeight() / 5);
        buttonContainer.align(Align.bottomRight);
        buttonContainer.setActor(addButton);
        addButton.addListener(new AddProfileClickListener());
        getStage().addActor(buttonContainer);
        buttonContainer.setFillParent(true);
        changedProfileList();
    }

    @Override
    public void changedProfileList() {
        List<String> names = ProfileManager.getManager().getNames();
        int i = 0;
        while (i < names.size()) {
            profileButtons.get(i).setText(names.get(i));
            editButtons.get(i).setVisible(true);
            i++;
        }
        while (i < profileButtons.size()) {
            profileButtons.get(i).setText(null);
            editButtons.get(i).setVisible(false);
            i++;
        }
        addButton.setVisible(names.size() != profileButtons.size());
    }

    private class SelectProfileClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            AudioManager.playSound("buttonClick");
            String name = ((TextButton) event.getListenerActor()).getText()
                    .toString();
            if (!name.equals("")) {
                ProfileManager.getManager().setCurrentProfile(name);
                AudioManager.setLoggedIn(true);
                getGame().setScreen(MainMenuViewController.class);
            }
        }
    }

    private class EditProfileClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            AudioManager.playSound("buttonClick");
            ImageButton clickedButton = (ImageButton) event.getListenerActor();
            final String name = profileButtons
                    .get(editButtons.indexOf(clickedButton)).getText()
                    .toString();
            final Skin dialogSkin = manager.get("data/skins/DialogTemp.json",
                    Skin.class);
            final float size = getStage().getHeight() / 5;
            // dialog to choose between editing the profile or deleting it.
            showDialog(new Dialog("", dialogSkin) {
                {
                    clear();
                    pad(space * 3 / 2);
                    Label nameLabel = new Label(name, dialogSkin);
                    add(nameLabel).colspan(2);
                    nameLabel.setFontScale(ProfileSelection.this.getStage().getHeight() / 720 * nameLabel.getFontScaleY());
                    row().space(10);
                    // configuration/edit option
                    ImageButton configButton = new ImageButton(dialogSkin,
                            "configButton");
                    configButton.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            AudioManager.playSound("buttonClick");
                            remove();
                            ProfileManager.getManager().setCurrentProfile(name);
                            getGame().setScreen(ProfileEditLang.class);
                        }
                    });
                    add(configButton).size(size);
                    // delete option. opens confirm dialog
                    ImageButton deleteButton = new ImageButton(dialogSkin,
                            "deleteButton");
                    deleteButton.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            AudioManager.playSound("buttonClick");
                            confirm();
                        }
                    });
                    add(deleteButton).size(size);
                    final Dialog dialog = this;
                    addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            if (!(0 < x && 0 < y && x < dialog.getWidth() && y < dialog
                                    .getHeight())) {
                                remove();
                            }
                        }
                    });
                }

                // asks for confirmation if the profile should be deleted
                private void confirm() {
                    clearChildren();
                    pad(space * 3 / 2);
                    Label nameLabel = new Label(name, dialogSkin);
                    add(nameLabel).colspan(2);
                    nameLabel.setFontScale(ProfileSelection.this.getStage().getHeight() / 720 * nameLabel.getFontScaleY());
                    row().space(10);
                    // yes
                    ImageButton yesButton = new ImageButton(dialogSkin,
                            "yesButton");
                    yesButton.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            AudioManager.playSound("buttonClick");
                            remove();
                            ProfileManager.getManager().delete(name);
                        }
                    });
                    add(yesButton).size(size);
                    // no
                    ImageButton noButton = new ImageButton(dialogSkin,
                            "noButton");
                    noButton.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            AudioManager.playSound("buttonClick");
                            remove();
                        }
                    });
                    add(noButton).size(size);
                }
            });
        }
    }

    private class AddProfileClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            AudioManager.playSound("buttonClick");
            ProfileManager m = ProfileManager.getManager();
            m.createProfile();
            m.setCurrentProfile("");
            getGame().setScreen(ProfileEditLang.class);
        }
    }

}
