package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Timer;
import java.util.Random;
import com.badlogic.gdx.audio.Music;
import com.mygdx.game.States.*;

import static com.mygdx.game.Frames.Constants.PPM;


public class MyGdxGame extends ApplicationAdapter {

    public final static int WIDTH = 480;
    public final static int HIEGHT = 800;

    public BitmapFont font;
    private GSM gsm;
    private OrthographicCamera camera;
    private Box2DDebugRenderer b2dr;
    private World world;
    private Body player, stick, standingPlat, destinationPlat, nextPlat;
    private SpriteBatch batch;
    private SpriteBatch pikaSprite;
	private TextureAtlas pikaAtlas;
	private Animation pikachuAnimation;
	private TextureAtlas donaldAtlas;
	private Animation donaldAnimation;
    private TextureAtlas duckAtlas;
    private Animation duckAnimation;
    private TextureAtlas dinoAtlas;
    private Animation dinoAnimation;

    private Timer animationTimer;
    private Music soundEffect;
    private Sound gameMusic;
    private Music contactPlatNoise;
    private Sound losingSound;

    public int stickHeight = 0;
    private float x = 0;
    private float timePass = 0;
    boolean isContactStickDestin = false;
    private boolean shots = true;
    private boolean justHeld = false;
    private boolean characterIsWalking = false;
    private boolean allowRotation = false;
    private boolean growing = false;
    public boolean whilePlay = false;
    public boolean gameOVER = false;
    private String characterSwitch = "1";
    boolean shootShort = true;
    private int moveInDirectionX = 0;
    private int moveDistance = 0;
    private int saveBlock = 0;
    public int score = 0;
    public float distance;

    //Random Block variables --> width and distance of platforms
    private int xPlace = createRandom(140, 150);
    private int xPlace2 = createRandom(xPlace + 40, xPlace + 150);
    public int blockWidth = createRandom(38, 45);
    private int blockWidth2 = createRandom(38, 45);
    private int newLocation = xPlace2;
    private float lastStandingx;

    private Texture blackPicture;
    private Texture scoreBackgroundBorder;
    private Sprite blackBox, blackBox1, blackbox2, blackBox3, blackbox4, borderPicture;
    private ParallaxBackground parallaxBackground;

    @Override
    public void create () {

        //creating stick sound effect
        soundEffect = Gdx.audio.newMusic(Gdx.files.internal("song.mp3"));
        soundEffect.setLooping(true);
        soundEffect.setVolume(0.1f);
        soundEffect.play();

        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal("com/badlogic/gdx/utils/arial-15.fnt"));
        pikaSprite = new SpriteBatch();
		pikaAtlas = new TextureAtlas("PikachuSpritePackage.pack");
        pikachuAnimation = new Animation(1/7f, pikaAtlas.getRegions());
        donaldAtlas = new TextureAtlas("donald.pack");
        donaldAnimation = new Animation(1/30f, donaldAtlas.getRegions());
        duckAtlas = new TextureAtlas("duck.pack");
        duckAnimation = new Animation(1/20f, duckAtlas.getRegions());
        dinoAtlas = new TextureAtlas("dino.pack");
        dinoAnimation = new Animation(1/5f, dinoAtlas.getRegions());
        blackPicture = new Texture("Black.png");

        scoreBackgroundBorder = new Texture("border.png");
        borderPicture = new Sprite(scoreBackgroundBorder);
        blackBox = new Sprite(blackPicture);
        blackBox1 = new Sprite(blackPicture);
        blackbox2 = new Sprite(blackPicture);
        blackBox3 = new Sprite(blackPicture);
        blackbox4 = new Sprite(blackPicture);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w /2, h /2);
        world = new World(new Vector2(0, -9.8f), false);
        b2dr = new Box2DDebugRenderer(true,true,true,true,true,true);
        b2dr = new Box2DDebugRenderer();
        player = makeBox(0,10,12,12,allowRotation);
        animationTimer = Timer.instance();

        //Creating all the platforms, we only have three platforms and constantly rename our platforms so that we are able to sense collision
        //Collision is only detected between destinationPlat and the stick and no where else.
        //Change userData of the bodies to correct thing

        standingPlat = makeBox(0,-80,blockWidth,200,true);
        destinationPlat = makeBox(xPlace,-80, blockWidth,200, true);
        nextPlat = makeBox(xPlace2, -80, blockWidth2 , 200, true);
        destinationPlat.setUserData("destinationPlat");

        gsm = new GSM();
        createCollisionListener();
        gsm.push(new MainMenu(gsm));

        //creating a textureRegion for every image and passing through parallax library
        parallaxBackground = new ParallaxBackground(
                new ParallaxLayer[]{
                        new ParallaxLayer(new TextureRegion(new Texture("layers/sky.png")), new Vector2(1.6f, 0), new Vector2(0f, 0f)),
                        new ParallaxLayer(new TextureRegion(new Texture("layers/rocks_3.png")), new Vector2(0.8f, 0), new Vector2(0f, 0f)),
                        new ParallaxLayer(new TextureRegion(new Texture("layers/rocks_2.png")), new Vector2(1.4f, 0), new Vector2(0f, 0f)),
                        new ParallaxLayer(new TextureRegion(new Texture("layers/rocks_1.png")), new Vector2(0.6f, 0), new Vector2(0f, 0f)),
                        new ParallaxLayer(new TextureRegion(new Texture("layers/pines.png")), new Vector2(0.8f, 0), new Vector2(0f, 0f)),
                        new ParallaxLayer(new TextureRegion(new Texture("layers/clouds_3.png")), new Vector2(1f, 0), new Vector2(0f, 0f)),
                        new ParallaxLayer(new TextureRegion(new Texture("layers/clouds_2.png")), new Vector2(1.6f, 0), new Vector2(0f, 0f)),
                        new ParallaxLayer(new TextureRegion(new Texture("layers/clouds_1.png")), new Vector2(1.4f, 0), new Vector2(0f, 0f)),
                        new ParallaxLayer(new TextureRegion(new Texture("layers/birds.png")), new Vector2(1.3f, 0), new Vector2(0f, 0f))
                },
                300,800,
                new Vector2(50f,0f));
    }


    @Override
    public void render() {

        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render(batch);

        //rendering the parrallax background
        parallaxBackground.render(Gdx.graphics.getDeltaTime());

        //rendering scoreboard
        batch.begin();
        font.draw(batch, "SCORE", 230, Gdx.graphics.getHeight()/2 + Gdx.graphics.getHeight()/2 - 20);
        font.draw(batch, String.valueOf(score) , 250, Gdx.graphics.getHeight()/2 + Gdx.graphics.getHeight()/2 - 40);
        batch.end();

        //pikachu animations

        if(characterSwitch == "1") {
            pikaSprite.begin();
            timePass += Gdx.graphics.getDeltaTime();
            pikaSprite.draw((TextureRegion) pikachuAnimation.getKeyFrame(timePass, characterIsWalking), player.getPosition().x * PPM - 11, player.getPosition().y * PPM - 10, 25, 25);
            pikaSprite.end();
        }else if (characterSwitch == "2"){
            pikaSprite.begin();
            timePass += Gdx.graphics.getDeltaTime();
            pikaSprite.draw((TextureRegion) donaldAnimation.getKeyFrame(timePass, characterIsWalking), player.getPosition().x * PPM - 11, player.getPosition().y * PPM - 8, 25, 25);
            pikaSprite.end();
        }else if(characterSwitch == "3"){
            pikaSprite.begin();
            timePass += Gdx.graphics.getDeltaTime();
            pikaSprite.draw((TextureRegion) duckAnimation.getKeyFrame(timePass, characterIsWalking), player.getPosition().x * PPM - 11, player.getPosition().y * PPM - 8, 20, 25);
            pikaSprite.end();
        }else if(characterSwitch == "4"){
            pikaSprite.begin();
            timePass += Gdx.graphics.getDeltaTime();
            pikaSprite.draw((TextureRegion) dinoAnimation.getKeyFrame(timePass, characterIsWalking), player.getPosition().x * PPM - 11, player.getPosition().y * PPM - 8, 25, 25);
            pikaSprite.end();
        }
        batch.begin();
        borderPicture.setSize(150,120);
        borderPicture.setPosition(180, 655);
        borderPicture.draw(batch);
        batch.end();

        update(Gdx.graphics.getDeltaTime());
        //b2dr.render(world, camera.combined.scl(PPM));


        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render(batch);

    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width / 2, height /2);
    }

    @Override
    public void dispose(){
        world.dispose();
        b2dr.dispose();
        pikaAtlas.dispose();
        soundEffect.dispose();
    }

    private void update(float delta){
        world.step(1 / 60f, 6 ,2);
        moveUpdate(delta);
        cameraUpdate(delta);

        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            gsm.set(new PauseState(gsm));
        }


        pikaSprite.begin();
        //rendering a black image behind the bodies for all the objects
        //all the following renders of "blackBox" are textures put to follow all theo objects
        if (stick!=null) {
            blackBox.setSize(2f, stickHeight);
            blackBox.setOrigin( blackBox.getWidth()/2, blackBox.getHeight()/2);
            blackBox.setRotation(stick.getAngle()* MathUtils.radiansToDegrees);
            blackBox.setPosition(stick.getPosition().x * PPM , stick.getPosition().y * PPM - (blackBox.getHeight()/2));
            blackBox.draw(pikaSprite);
        }
        pikaSprite.end();

        pikaSprite.begin();
        blackBox1.setSize(blockWidth, 200);
        blackBox1.setPosition(destinationPlat.getPosition().x * PPM - (blackBox1.getWidth() / 2), destinationPlat.getPosition().y * PPM - (blackBox1.getHeight() / 2));
        blackBox1.draw(pikaSprite);
        pikaSprite.end();

        if(score <= 1){
            pikaSprite.begin();
            blackBox1.setSize(blockWidth, 200);
            blackBox1.setPosition(standingPlat.getPosition().x * PPM - (blackBox1.getWidth() / 2), standingPlat.getPosition().y * PPM - (blackBox1.getHeight() / 2));
            blackBox1.draw(pikaSprite);
            pikaSprite.end();
        }

        pikaSprite.begin();
        blackBox1.setSize(saveBlock, 200);
        blackBox1.setPosition(standingPlat.getPosition().x * PPM - (blackBox1.getWidth() / 2), standingPlat.getPosition().y * PPM - (blackBox1.getHeight() / 2));
        blackBox1.draw(pikaSprite);
        pikaSprite.end();

        pikaSprite.begin();
        blackBox1.setSize(saveBlock, 200);
        blackBox1.setPosition(lastStandingx * PPM - (blackBox1.getWidth() / 2), standingPlat.getPosition().y * PPM - (blackBox1.getHeight() / 2));
        blackBox1.draw(pikaSprite);
        pikaSprite.end();

        if(Gdx.input.isKeyJustPressed(Input.Keys.S)){
            characterSwitch = "1";
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.A)){
            characterSwitch = "2";
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.D)) {
            characterSwitch = "3";
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            characterSwitch = "4";
        }

        if (isContactStickDestin){

            //If there is collision between destinationPlat and stick, it is checked to see if the length of the stick is passed the right edge of platform
            //If it is passed the right edge, we get the character to walk the distance of the stick
            //A timer is included so it will take 4 seconds for the stick to disappear --> gravity will force the player to fall

            float distance = destinationPlat.getPosition().x * PPM  - standingPlat.getPosition().x * PPM ;
            if (stickHeight <= distance + (blockWidth/2) ){
                if (moveInDirectionX < distance) {
                    characterIsWalking = true;
                    moveDistance += 1;
                    player.setTransform(moveDistance / PPM, 30 / PPM, 0);
                    moveInDirectionX += 1;

                } else {
                    resetStick();
                    if(blockWidth != saveBlock) {
                        saveBlock = blockWidth;
                    }
                    blockWidth = blockWidth2;
                    characterIsWalking = false;
                    newLocation = createRandom(120, 150);
                    lastStandingx = standingPlat.getPosition().x;

                    //Changing the user data of the different bodies and renaming them all so that we
                    //are able to detect collision between destinationPlat and stick
                    standingPlat = destinationPlat;
                    standingPlat.setUserData(null);
                    destinationPlat = nextPlat;
                    destinationPlat.setUserData("destinationPlat");
                    blockWidth2 = createRandom(25, 45);
                    nextPlat = makeBox(newLocation + (int)destinationPlat.getPosition().x * (int) PPM, -80, blockWidth2 , 200, true);
                    moveInDirectionX = 0;
                    isContactStickDestin = false;
                }

            }else{
                //if player overshot the platform he will walk the length of the stick
                if (moveInDirectionX < stickHeight) {
                    characterIsWalking = true;
                    moveInDirectionX += 1;
                    moveDistance += 1;
                    player.setTransform(moveDistance / PPM, 30 / PPM, 0);
                    allowRotation = true;
                    System.out.println("distance" + distance);
                    System.out.println("moveInDirectionX" + moveInDirectionX);
                }else{
                    losingSound = Gdx.audio.newSound(Gdx.files.internal("losing.mp3"));
                    losingSound.play();
                    gameOVER = true;
                    if(gameOVER) {
                        float delay = (float) 1;
                        Timer.schedule(new Timer.Task() {
                            @Override
                            public void run() {
                                gsm.set(new DeathState(gsm));
                                resetStick();
                                characterIsWalking = false;
                                player.setTransform(standingPlat.getPosition().x, (float) 26.604782 / PPM, 0);
                                isContactStickDestin = false;

                            }
                        }, delay);

                    }
                }

            }
        }else{

            if(player.getPosition().y  * PPM < - 300){

                characterIsWalking = false;
                gsm.set(new DeathState(gsm));
                losingSound = Gdx.audio.newSound(Gdx.files.internal("losing.mp3"));
                losingSound.play();
                player.setTransform(standingPlat.getPosition().x, (float) 26.604782 / PPM, 0);
                justHeld = false;
            }

        }
        pikaSprite.setProjectionMatrix(camera.combined);
    }


    private void moveUpdate(float delta) {

        if(stickHeight < 200) {
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && shots) {
                //set restriction to how high the stick can be
                stickHeight += 5;
                buildStick(stickHeight);
                justHeld = true;
                growing = true;

            } else if (!Gdx.input.isKeyPressed(Input.Keys.SPACE) && justHeld && shots) {
                makeJoint();
                growing = false;
                shots = false;
                float delay = (float) 2;
                if (justHeld && stick != null) {
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            characterIsWalking = false;
                            shots = true;
                                if (shootShort) {
                                    losingSound = Gdx.audio.newSound(Gdx.files.internal("losing.mp3"));
                                    losingSound.play();
                                    gsm.set(new DeathState(gsm));
                                    characterIsWalking = false;
                                    gameOVER = false;

                                }

                            shootShort = true;

                        }
                    }, delay);
                    justHeld = false;
                }
            }
            }else{
                makeJoint();
                shots = false;
                growing = false;
                float delay = 2;
                if (justHeld && stick != null) {
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            shots = true;
                            if (shootShort) {
                                losingSound = Gdx.audio.newSound(Gdx.files.internal("losing.mp3"));
                                losingSound.play();
                                gsm.set(new DeathState(gsm));
                                characterIsWalking = false;
                                gameOVER = false;

                            }

                            shootShort = true;

                        }
                    }, delay);
                    justHeld = false;
                }
            }

    }

    private void cameraUpdate(float delta){
        Vector3 position = camera.position;

        position.x = (player.getPosition().x + 2) * PPM;
        position.y = player.getPosition().y * PPM;
        camera.position.set(position);
        camera.update();
    }

    private Body createStick(float x, float y, int blockHeight) {
        Body pStick = makeStick(x, y, 2, blockHeight, false);
        return pStick;
    }

    private Body makeBox(float x, float y, int width, int height, boolean isStatic) {
        Body pBody;
        BodyDef def = new BodyDef();

        if(isStatic)
            def.type = BodyDef.BodyType.StaticBody;
        else
            def.type = BodyDef.BodyType.DynamicBody;
            def.position.set(x / PPM , y / PPM);
            def.fixedRotation = true;
            pBody = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2f/PPM, height/2f/PPM);
        FixtureDef def2 = new FixtureDef();
        def2.shape = shape;
        def2.density = 1000f;

        pBody.createFixture(def2);
        shape.dispose();
        return pBody;
    }

    private void makeJoint(){
        RevoluteJoint rJoint;

        RevoluteJointDef jointDef = new RevoluteJointDef();
        jointDef.lowerAngle = 0f;
        jointDef.upperAngle = 1.57f;
        jointDef.enableMotor = true;
        jointDef.motorSpeed = 3;
        jointDef.maxMotorTorque = 20;
        jointDef.enableLimit = true;
        jointDef.bodyB = standingPlat;
        jointDef.bodyA = stick;
        jointDef.localAnchorA.set( 1 / PPM, -stickHeight /2f/PPM);
        jointDef.localAnchorB.set(6 / PPM, 200f/2/PPM);
        jointDef.collideConnected = false;

        rJoint=(RevoluteJoint)world.createJoint(jointDef);

    }
	private Body makeStick(float x, float y, int width, int height, boolean isStatic) {

		Body pBody;
		BodyDef def = new BodyDef();

		if(isStatic)
			def.type = BodyDef.BodyType.StaticBody;
		else
			def.type = BodyDef.BodyType.DynamicBody;
		def.position.set(x / PPM , y / PPM);
		def.fixedRotation = allowRotation;
		pBody = world.createBody(def);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width/2f/PPM, height/2f/PPM);
		FixtureDef def1 = new FixtureDef();
		def1.shape = shape;
		def1.density = 1f;
		pBody.createFixture(def1);
		shape.dispose();
		return pBody;
	}

    private int createRandom(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max-min)+1)+min;
    }

    private void buildStick(int tall){
        if (stick == null) {
            stick = createStick(player.getPosition().x*PPM + 5, player.getPosition().y * PPM, tall);
            stick.setUserData("stick");
        } else {
            stick.destroyFixture(stick.getFixtureList().first());
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(1 / PPM, tall / 2f / PPM);
            FixtureDef def = new FixtureDef();
            def.shape = shape;
			def.density = 1f;
			def.friction = 20f;
            stick.createFixture(def);
            shape.dispose();
        }
    }

    private void resetStick(){
        if (stick!= null) {
            world.destroyBody(stick);
            stick = null;
            stickHeight = 0;
        }
    }


	public TextureAtlas getAtlas(){
    	return pikaAtlas;
	}

	private void createCollisionListener(){
        world.setContactListener(new CustomContactListener(this));
    }

    private void endGame(){
        score -= 1;
    }
}



//WHAT TO DO
//score
//menu
//pause
//death
//powerup thing thats disadvantage
//black towers
//smallest stick jump off --> make him fall stright down

//have that if done moving so if(movex < __) and else if after then have stick disapear or resetStick()
//have button push playstate set score 0


//understand both creating stick and animationTimer


