package com.mygdx.game.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.MyGdxGame;

import static com.mygdx.game.MyGdxGame.HIEGHT;

public class DeathState extends StateGame {
    MyGdxGame myGdxGame;

    private Texture background;
    private Texture gameOver;

    public DeathState(GSM gsm) {
        super(gsm);
        background = new Texture("Back.jpg");
        gameOver = new Texture("button2.png");
    }



    @Override
    protected void handleInput(){
        if(Gdx.input.isKeyPressed(Input.Keys.Q)){
            Gdx.app.exit();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
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
        batch.draw(background,0, 0, 490, 800);
        batch.draw(gameOver, 180, HIEGHT/2, 125, 60);
        batch.end();
        batch.begin();
//        myGdxGame.font.draw(batch, "SCORE", 230, Gdx.graphics.getHeight()/2 + Gdx.graphics.getHeight()/2 - 20);
//        myGdxGame.font.draw(batch, String.valueOf(myGdxGame.score - 2 ) , 250, Gdx.graphics.getHeight()/2 + Gdx.graphics.getHeight()/2 - 40);
        batch.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        gameOver.dispose();
    }
}
