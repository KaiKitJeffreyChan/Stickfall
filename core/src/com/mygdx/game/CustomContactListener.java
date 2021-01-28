package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.audio.Music;

public class CustomContactListener implements ContactListener {
    MyGdxGame myGdxGame;
    private Sound onContact;
    public CustomContactListener(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA.getBody().getUserData() ==  "stick" && fixtureB.getBody().getUserData() == "destinationPlat" || fixtureA.getBody().getUserData() == "destinationPlat" && fixtureB.getBody().getUserData() == "stick"){
            myGdxGame.isContactStickDestin = true;
            myGdxGame.shootShort = false;
            myGdxGame.score++;
            onContact = Gdx.audio.newSound(Gdx.files.internal("zelda.mp3"));
            onContact.play();
        }
    }

    @Override
    public void endContact(Contact contact) {
        //Fixture stick = contact.getFixtureA();
        //Fixture destinationPlat = contact.getFixtureB();
        //Gdx.app.log("endContact", "between " + stick.toString() + " and " + destinationPlat.toString());
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
