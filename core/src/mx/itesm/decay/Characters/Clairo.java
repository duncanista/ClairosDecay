package mx.itesm.decay.Characters;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.Animation;

import mx.itesm.decay.Screens.TestScreen;


public class Clairo extends Sprite {

    // Clairo's State
    public enum State { IDLE, WALKING, RUNNING, DEAD, JUMPING}
    public State currentState;
    public State previousState;

    // Box2d
    public World world;
    public Body body;

    // Animations
    private Animation<TextureRegion> clairoStand;
    private Animation<TextureRegion> clairoRun;
    private Animation<TextureRegion> clairoJump;
    private Animation<TextureRegion> clairoWalk;
    private Animation<TextureRegion> clairoShoot;


    // Animation Details
    public float timer;
    public boolean isRunningRight;
    public boolean isJumping;
    public boolean walkRight;
    public boolean isChasing;

    private final TestScreen screen;

    public Clairo(TestScreen screen) {
        this.screen = screen;
        this.world = screen.getWorld();
        currentState = State.IDLE;
        previousState = State.IDLE;

        timer = 0;
        // TO DO

        Array<TextureRegion> frames = new Array<TextureRegion>();

        for(int i = 0; i < 13; i++)
            frames.add(new TextureRegion(new Texture("Characters/Detective/Run/Detective_Run.png"), i * 284, 0, 284, 268));
        clairoStand = new Animation(0.1f, frames);


        frames.clear();


        for(int i = 0; i < 6; i++){
            frames.add(new TextureRegion(new Texture("Characters/Detective/Run/Detective_Run.png"), i * 284, 0, 284, 268));
        }
        clairoRun = new Animation<TextureRegion>(0.1f, frames);

        frames.clear();

         /*
         for(int i = 1; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("clairo_jump"), i * 16, 0, 16, 16));
        clairoStand = new Animation(0.1f, frames);
        */

        // frames.clear();

         /*
         for(int i = 1; i < 4; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("clairo_walk"), i * 16, 0, 16, 16));
        clairoStand = new Animation(0.1f, frames);
        */
        setBounds(400,400,71, 67);

        defineClairo();

        setRegion(new TextureRegion(new Texture("Characters/DetectiveRun.png")));

    }

    public void update(float dt){
        updateMovement(dt);
        setPosition(body.getPosition().x-getWidth()/2, body.getPosition().y-getHeight()/2);
        setRegion(getFrame(dt));

    }

    private void updateMovement(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            body.applyLinearImpulse(new Vector2(body.getLinearVelocity().y, 40000f), body.getWorldCenter(), true);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && body.getLinearVelocity().x <= 1501f)
            body.applyLinearImpulse(new Vector2(1500f, 0), body.getWorldCenter(), true);
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && body.getLinearVelocity().x >= -1501)
            body.applyLinearImpulse(new Vector2(-1500f, 0), body.getWorldCenter(), true);
    }

    public TextureRegion getFrame(float dt){
        TextureRegion region;
        timer += dt;
        switch (currentState){
            case IDLE:
                region = clairoStand.getKeyFrame(timer, true);
                break;
            case WALKING:
                region = clairoRun.getKeyFrame(timer, true);
                break;
            case RUNNING:
                region = clairoRun.getKeyFrame(timer, true);
                break;
            default:
                region = clairoRun.getKeyFrame(timer, true);
                break;
        }
        return region;
    }

    // Box2d initialization
    public void defineClairo() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(400, 400);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth(), getHeight());
        FixtureDef fix = new FixtureDef();
        fix.shape = shape;
        Fixture fixture = body.createFixture(fix);
    }

    public void draw(SpriteBatch batch){
        super.draw(batch);
    }
}
