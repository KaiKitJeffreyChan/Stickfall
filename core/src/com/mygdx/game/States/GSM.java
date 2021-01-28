package com.mygdx.game.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.Stack;

public class GSM {

    private Stack<StateGame> states;

    public GSM(){
        states = new Stack<StateGame>();
    }

    public void push(StateGame state){
        states.push(state);
    }

    public void pop(){
        states.pop();
    }

    public void set(StateGame state){
        states.pop();
        states.push(state);
    }

    public void update(float delta){
        states.peek().update(delta);
    }

    public void render(SpriteBatch batch){
        states.peek().render(batch);
    }
}
