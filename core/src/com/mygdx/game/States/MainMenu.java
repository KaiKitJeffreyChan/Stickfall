package com.mygdx.game.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static com.mygdx.game.MyGdxGame.HIEGHT;
import static com.mygdx.game.MyGdxGame.WIDTH;

public class MainMenu extends StateGame {

    private Texture background;
    private Texture playbutton;
    private Texture controls;
    private Texture exit;
    private Texture control;
    private boolean showControl;

    public MainMenu(GSM gsm) {
        super(gsm);
        background = new Texture("Back.jpg");
        playbutton = new Texture("button.png");
        controls = new Texture("button1.png");
        control = new Texture("control.png");
        exit = new Texture("button2.png");
    }

    @Override
    public void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)){
            gsm.set(new PlaySatate(gsm));
            //whilePlay = true;
            dispose();
        }

        if(Gdx.input.isKeyPressed(Input.Keys.C)){
            showControl = true;
        }else {
            showControl = false;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.Q)){
            Gdx.app.exit();
        }
    }

    @Override
    public void update(float delta) {
        handleInput();

    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        batch.draw(background,0, 0, 490, 800);
        batch.draw(playbutton, 170, HIEGHT/2, 150,80 );
        batch.draw(controls, 180, HIEGHT/2 - 50, 130, 60);
        batch.draw(exit, 180, HIEGHT/2 - 106, 125, 60);
        batch.end();
        batch.begin();
        if (showControl){
            batch.draw(control,0,HIEGHT/2-50,480,284);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        playbutton.dispose();
    }


}
