package org.academiadecodigo.hackathon.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.*;
import org.academiadecodigo.hackathon.Cave;
import org.academiadecodigo.hackathon.Sprites.Bob;

public class MyContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        // Check if either fixture has user data "deadly"
        if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("deadly")) {
            handleDeadlyCollision(fixtureB);
        } else if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("deadly")) {
            handleDeadlyCollision(fixtureA);
        }

        // Check if either fixture has user data "win"
        if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("win")) {
            handleWinCollision(fixtureB);
        } else if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("win")) {
            handleWinCollision(fixtureA);
        }
    }


    @Override
    public void endContact(Contact contact) {}

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}

    private void handleDeadlyCollision(Fixture otherFixture) {
        Cave.manager.get("anotherone.wav", Music.class).stop();
        Cave.manager.get("dead.wav", Sound.class).play();
        Bob.setDead(true);
    }

    private void handleWinCollision(Fixture otherFixture) {
        Cave.manager.get("anotherone.wav", Music.class).stop();
        Cave.manager.get("win.wav", Sound.class).play();
        Bob.setWon(true);
    }
}