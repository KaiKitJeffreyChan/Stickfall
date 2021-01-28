package com.mygdx.game.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PauseState extends StateGame{

    private Texture background;

    public PauseState(GSM gsm) {
        super(gsm);
        background = new Texture("back.jpg");
    }

    @Override
    protected void handleInput() {

        if(Gdx.input.isKeyPressed(Input.Keys.Q)){
            Gdx.app.exit();
        }

        if(Gdx.input.isKeyPressed(Input.Keys.R)){
            gsm.set(new PlaySatate(gsm));
        }

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(SpriteBatch batch) {
        handleInput();
        batch.begin();
        batch.draw(background,0,0,490,800);
        batch.end();
    }

    @Override
    public void dispose() {

    }
}
