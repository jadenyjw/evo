package com.evo.game.stages;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.evo.game.actors.Food;
import com.evo.game.actors.Runner;
import com.evo.game.utils.WorldUtils;
public class GameStage extends Stage{
	 // This will be our viewport measurements while working with the debug renderer
    private static final int VIEWPORT_WIDTH = 20;
    private static final int VIEWPORT_HEIGHT = 13;

    private World world;
    private Body border;
    private Runner runner;
    private Food food;
    
    private final float TIME_STEP = 1 / 300f;
    private float accumulator = 0f;

    private OrthographicCamera camera;
    private Box2DDebugRenderer renderer;

    public GameStage() {
    	setUpWorld();
    	setupCamera();
    	Gdx.input.setInputProcessor(this);
        renderer = new Box2DDebugRenderer();
    }

    private void setupCamera() {
        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
    }
    
    private void setUpWorld() {
        world = WorldUtils.createWorld();
        setUpBorder();
        setUpFood();
        setUpRunner();
    }
    
    private void setUpBorder() {
    	border = WorldUtils.createBorder(world);
    }
    private void setUpRunner() {
        runner = new Runner(WorldUtils.createRunner(world));
        addActor(runner);
    }
    private void setUpFood(){
    	for (int x = 0; x < 100; x++){
    		food = new Food(WorldUtils.createFood(world, (float) Math.random() * (28) + 1, (float)Math.random() * (28) + 1));
    	}
    }

    @Override
    public void act(float delta) {
    	
        super.act(delta);
        if (leftKeyPressed){
        	runner.turnLeft();
        }
        if (rightKeyPressed){
        	runner.turnRight();
        	
        }
        if (upKeyPressed){
        	runner.moveForward();
        	//runner.grow();
        }
        else if(!upKeyPressed){
        	runner.stop();
        }
        
        
        camera.position.set(runner.getX(), runner.getY(), 0);
        camera.update();
        
        // Fixed timestep
        accumulator += delta;

        
        while (accumulator >= delta) {
            world.step(TIME_STEP, 6, 2);
            accumulator -= TIME_STEP;
        }

        //TODO: Implement interpolation
       
       
        
        
    }

    @Override
    public void draw() {
        super.draw();
        renderer.render(world, camera.combined);
    }
    
    public boolean leftKeyPressed;
    public boolean upKeyPressed;
    public boolean rightKeyPressed;
    
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.LEFT) {
            leftKeyPressed = true;
        }
        if (keycode == Input.Keys.RIGHT) {
            rightKeyPressed = true;
        }
        if (keycode == Input.Keys.UP) {
            upKeyPressed = true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
    	 if (keycode == Input.Keys.LEFT) {
             leftKeyPressed = false;
         }
         if (keycode == Input.Keys.RIGHT) {
             rightKeyPressed = false;
         }
         if (keycode == Input.Keys.UP) {
             upKeyPressed = false;
         }

        return false;
    }
    



}
