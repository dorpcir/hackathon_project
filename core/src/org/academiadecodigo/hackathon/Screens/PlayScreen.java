package org.academiadecodigo.hackathon.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.*;
import org.academiadecodigo.hackathon.Cave;
import org.academiadecodigo.hackathon.Scenes.Hud;
import org.academiadecodigo.hackathon.Sprites.Bob;
import org.academiadecodigo.hackathon.Tools.B2WorldCreator;
import org.academiadecodigo.hackathon.Tools.MyContactListener;

public class PlayScreen implements Screen {
    private Cave game;
    private TextureAtlas atlas;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private World world;
    private Box2DDebugRenderer b2dr;

    private Bob bob;
    private Music music;
    private MyContactListener contactListener;
    private boolean bobHasJumped;
    private float deathTime;
    float timeSinceJump;
    private int jumpCount;


    public void handleInput(float dt) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.W) && jumpCount < 2) {
            bob.body.applyLinearImpulse(new Vector2(0, 3f), bob.body.getWorldCenter(), true);
            Cave.manager.get("jump.wav", Sound.class).play();
            jumpCount++;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D) && bob.body.getLinearVelocity().x <= 2){
            bob.body.applyLinearImpulse(new Vector2(0.1f, 0), bob.body.getWorldCenter(), true );
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A) && bob.body.getLinearVelocity().x >= -2){
            bob.body.applyLinearImpulse(new Vector2(-0.1f, 0), bob.body.getWorldCenter(), true );
        }
    }

    public void update (float dt) {
        handleInput(dt);

        gameCam.update();
        renderer.setView(gameCam);

        bob.update(dt);
        hud.update(dt);

        world.step(1/60f, 6, 2);

        gameCam.position.x = bob.body.getPosition().x;

        if (bob.isDead() && bobHasJumped) {
            timeSinceJump += dt;

        }
        if (bob.body.getLinearVelocity().y == 0){
            jumpCount = 0;
        }

    }


    public PlayScreen(Cave game){
        jumpCount = 0;
        timeSinceJump = 0f;
        bobHasJumped = false;
        bob.setDead(false);
        Bob.setWon(false);
        atlas = new TextureAtlas("Miner.pack");
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(Cave.V_WIDTH / Cave.PPM , Cave.V_HEIGHT / Cave.PPM ,gameCam);
        hud = new Hud(game.batch);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("Map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Cave.PPM);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        world = new World(new Vector2(0,-10),true);

        contactListener = new MyContactListener();
        world.setContactListener(contactListener);

        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(world, map);

        bob = new Bob(world, this);

        music = Cave.manager.get("anotherone.wav", Music.class);
        music.setLooping(true);
        music.play();

    }

    private boolean gameOver() {
        return bob.isDead() && timeSinceJump >= 2f;
    }

    private boolean gameWon() {
        return Bob.bobHaswon();
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
        //b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        bob.draw(game.batch);
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

            if (bob.isDead() && !bobHasJumped) {
                Filter filter = new Filter();
                filter.maskBits = 0;
                for (Fixture fixture : bob.body.getFixtureList()){
                    fixture.setFilterData(filter);
                }
                bob.body.applyLinearImpulse(new Vector2(0, 4f), bob.body.getWorldCenter(), true);
                bobHasJumped = true;
            }
            if(hud.getWorldTimer() == 0){
                Bob.setDead(true);
            }

        if (gameOver()) {
            game.setScreen(new GameOverScreen(game));
                dispose();
            }
        if (gameWon()) {
            game.setScreen(new WinScreen(game));
                dispose();
        }
        }


    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
