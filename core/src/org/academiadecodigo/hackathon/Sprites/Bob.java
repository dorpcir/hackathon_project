package org.academiadecodigo.hackathon.Sprites;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import org.academiadecodigo.hackathon.Cave;
import org.academiadecodigo.hackathon.Screens.PlayScreen;

public class Bob extends Sprite{
    public World world;
    public Body body;
    private TextureRegion bobStand;
    public enum State {JUMPING, STANDING, RUNNING, DEAD};
    public State currentState;
    public State previousState;
    private Animation<TextureRegion> bobRun;
    private Animation<TextureRegion> bobJump;
    private Animation<TextureRegion> bobDead;
    private float stateTimer;
    private boolean runningRight;
    private static boolean isDead;
    private static boolean haswon;



    public Bob(World world, PlayScreen screen) {

        super(screen.getAtlas().findRegion("Miner"));
        this.world = world;
        defineBob();
        bobStand = new TextureRegion(getTexture(),0,0,40,39);
        setBounds(0,0,41/ Cave.PPM, 41 / Cave.PPM);
        setRegion(bobStand);
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;


        Array<TextureRegion> runFrames = new Array<TextureRegion>();
        for (int i = 1; i < 5; i++) {
            runFrames.add(new TextureRegion(getTexture(), i * 40, 0, 40, 39));
        }
        bobRun = new Animation<>(0.1f, runFrames);

        Array<TextureRegion> jumpFrames = new Array<TextureRegion>();
        for (int i = 5; i < 6; i++) {
            jumpFrames.add(new TextureRegion(getTexture(), i * 40, 0, 40, 39));
        }
        bobJump = new Animation<>(0.1f, jumpFrames);
    }

    public void update(float df){
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 3);
        setRegion(getFrame(df));

    }

    public TextureRegion getFrame (float dt){
        currentState = getState();

        TextureRegion region;
        switch (currentState){
            case JUMPING:
                region = bobJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = bobRun.getKeyFrame(stateTimer, true);
                break;
            default:
                region = bobStand;
        }
        if ((body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }
        else if ((body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true,false);
            runningRight = true;
        }
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState(){
        if (body.getLinearVelocity().y > 0 || (body.getLinearVelocity().y < 0 && previousState == State.JUMPING)){
            return State.JUMPING;
        }
        else if(body.getLinearVelocity().x != 0){
            return State.RUNNING;
        }
        else {
            return State.STANDING;
        }
    }
    public void defineBob(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(128 / Cave.PPM, 256 / Cave.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(15 / Cave.PPM);

        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);
    }

    public boolean isDead() {
        return isDead;
    }

    public static void setDead(boolean dead) {
        isDead = dead;
    }

    public float getStateTimer() {
        return stateTimer;
    }

    public static boolean bobHaswon() {
        return haswon;
    }

    public static void setWon(boolean haswon) {
        Bob.haswon = haswon;
    }
}
