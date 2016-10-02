package com.evo.game.stages;

import com.evo.game.box2d.BotUserData;

import java.util.Random;

import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.evo.game.actors.Bot;
import com.evo.game.actors.Food;
import com.evo.game.actors.Runner;
import com.evo.game.utils.BodyUtils;
import com.evo.game.utils.Constants;
import com.evo.game.utils.WorldUtils;
import com.evo.genetics.Gene;
import com.evo.networks.Network;

public class GameStage extends Stage implements ContactListener {

	// This will be our viewport measurements while working with the debug
	// renderer
	private static final int VIEWPORT_WIDTH = 20;
	private static final int VIEWPORT_HEIGHT = 13;

	private World world;
	private Body border;
	private Runner runner;
	private Array<Food> food;
	private Array<Bot> bot;

	private final float TIME_STEP = 1 / 300f;
	private float accumulator = 0f;

	private OrthographicCamera camera;
	private Box2DDebugRenderer renderer;

	private Array<Body> deletedBodies;
	private Array<Body> bodies;
	private Array<Body> growBodies;

	private int generation = 1;

	private boolean allDead = false;

	private FileHandle filehandle = Gdx.files.internal("assets/skin/uiskin.json");
	private Skin skin = new Skin(filehandle);

	private Label generationLabel = new Label("Generation", skin);
	private Label botsLabel = new Label("Bots Left", skin);
	private Label messageLabel = new Label("", skin);

	private float playTime = 0;
	private float playTimeRounded;

	private Array<Gene> geneRecord = new Array<Gene>();
	private Array<Float> timeRecord = new Array<Float>();
	
	private float lowestDistanceToBot = Float.POSITIVE_INFINITY;
	private float angleToBot;
	private float sizeOfBot;

	private float lowestDistanceToFood = Float.POSITIVE_INFINITY;
	private float angleToFood;

	public GameStage() {

		setUpWorld();
		setupCamera();
		
		Gdx.input.setInputProcessor(this);
		renderer = new Box2DDebugRenderer();
	}

	private void setupCamera() {
		camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		camera.position.set(runner.body.getPosition().x, runner.body.getPosition().x, 0);
		camera.update();
		
	}

	private void setUpWorld() {

		world = WorldUtils.createWorld();
		world.setContactListener(this);
		bodies = new Array<Body>();
		deletedBodies = new Array<Body>();
		growBodies = new Array<Body>();

		if (generation == 1) {
			bot = new Array<Bot>();
		}
		food = new Array<Food>();
		setUpBorder();
		setUpBots();
		setUpRunner();
		setUpFood();

		setUpText();
		
	}

	private void setUpBorder() {
		border = WorldUtils.createBorder(world);
	}

	private void setUpRunner() {
		runner = new Runner(WorldUtils.createRunner(world));
		addActor(runner);
		runner.setPosition(runner.body.getPosition().x, runner.body.getPosition().y);
		
	}

	private void setUpFood() {
		for (int x = 0; x < 64; x++) {
			food.add(new Food(
					WorldUtils.createFood(world, (float) Math.random() * (28) + 1, (float) Math.random() * (28) + 1)));
			food.get(x).getUserData().setID(x);
		}
	}

	private void setUpText() {
		generationLabel.setPosition(20f, 20f);
		generationLabel.setColor(Color.WHITE);
		generationLabel.setText("Generation: " + generation);

		botsLabel.setPosition(20f, 40f);
		botsLabel.setColor(Color.WHITE);
		botsLabel.setText("Bots Left: " + bot.size);

		messageLabel.setPosition(20f, 70f);
		messageLabel.setColor(Color.WHITE);
		messageLabel.setText("Game: Active");

		addActor(generationLabel);
		addActor(botsLabel);
		addActor(messageLabel);
	}

	private void setUpBots() {

		if (generation != 1) {

			Random randomno = new Random();

			for (int x = 0; x < geneRecord.size - 5; x++) {

				for (int y = 0; y < geneRecord.get(x).size; y++) {
					float uniform = (float) (randomno.nextGaussian() * Math.pow(3, -x));
					System.out.println(uniform);
					// System.out.println(uniform);
					if (geneRecord.get(x).get(y) + uniform > 1) {
						geneRecord.get(x).set(y, geneRecord.get(x).get(y) - uniform);
					} else {
						geneRecord.get(x).set(y, geneRecord.get(x).get(y) + uniform);
					}
				}
			}
		}

		for (int x = 0; x < 10; x++) {

			if (generation == 1) {
				Gene gene = new Gene();
				Network network = new Network();

				bot.add(new Bot(WorldUtils.createBot(world, (float) Math.random() * (Constants.SPAWN_RADIUS) + 1,
						(float) Math.random() * (Constants.SPAWN_RADIUS) + 1), gene, network));
				bot.get(x).getUserData().setID(x);

				// Generate a random gene

				bot.get(x).gene = new Gene();
				Random rand = new Random();

				for (int y = 0; y < 64; y++) {

					bot.get(x).gene.add(rand.nextFloat() * (1 - (-1)) + -1);

				}

				bot.get(x).network.setWeights(bot.get(x).gene);
				// System.out.println(bot.get(x).network.dumpWeights());

			}

			else {

				BodyDef bodyDef = new BodyDef();
				bodyDef.type = BodyDef.BodyType.DynamicBody;
				bodyDef.position.set(new Vector2((float) Math.random() * (Constants.SPAWN_RADIUS) + 1,
						(float) Math.random() * (Constants.SPAWN_RADIUS) + 1));
				CircleShape shape = new CircleShape();
				shape.setRadius(0.2f);
				Body botBody = world.createBody(bodyDef);
				botBody.createFixture(shape, 0.0f);
				botBody.resetMassData();
				botBody.setUserData(new BotUserData());
				bot.get(x).body = botBody;
				bot.get(x).network.setWeights(geneRecord.get(x));

			}

		}
	}

	private void updateDelete(Body body) {

		if (BodyUtils.bodyIsBot(body)) {
			// bot.get(((BotUserData)
			// body.getUserData()).getID()).addAction(Actions.removeActor());
		}
		if (BodyUtils.bodyIsFood(body)) {
			// food.get(((FoodUserData)
			// body.getUserData()).getID()).addAction(Actions.removeActor());
		}

		deletedBodies.removeValue(body, true);

		world.destroyBody(body);

	}

	private void updateGrow(Body body) {

		growBodies.removeValue(body, true);

		if (BodyUtils.bodyIsBot(body) && bot.size > 0) {
			try {
				bot.get(((BotUserData) (body.getUserData())).getID()).grow(0.02f);
			} catch (IllegalStateException name) {

			}

		}
		if (BodyUtils.bodyIsRunner(body)) {
			runner.grow(0.02f);
		}

	}

	@Override
	public void act(float delta) {

		super.act(delta);

		playTime += Gdx.graphics.getDeltaTime();
		playTimeRounded = Math.round(playTime * 100) / 100.0f;
		// Gdx.app.log("playTimeRounded", playTimeRounded + "");

		for (int x = 0; x < bot.size; x++) {
			if (bot.get(x).body.isActive()) {
				((BotUserData) bot.get(x).getUserData()).setSeconds(playTimeRounded);
				// System.out.println(((BotUserData)
				// bot.get(x).getUserData()).getSeconds() + " " + x);
			}
		}

		for (Body body : deletedBodies) {
			updateDelete(body);
		}
		for (Body body : growBodies) {
			updateGrow(body);
		}

		botsLabel.setText("Bots Left: " + bodySize(bot));

		if ((runner.body.isActive() && bodySize(bot) == 0) || (!(runner.body.isActive()) && bodySize(bot) == 1)) {
			allDead = true;
		}

		if (allDead) {
			if (runner.body.isActive() && bodySize(bot) == 0) {
				messageLabel.setText("You won this round!");
			}
			generation++;
			playTime = 0f;
			setUpWorld();
			setupCamera();
			allDead = false;
		} else {

			// Calculate for neural network
			world.getBodies(bodies);
			
			for (int x = 0; x < bot.size; x++) {

				if (bot.get(x).body.isActive()) {

					Bot currentBot = bot.get(x);

					lowestDistanceToBot = Float.POSITIVE_INFINITY;
					lowestDistanceToFood = Float.POSITIVE_INFINITY;
					
					
					
						for (int y = 0; y < bodies.size; y++) {

							Body targetBody = bodies.get(y);
							if (bodies.get(y).isActive()) {

								// If body is food
								if (BodyUtils.bodyIsFood(targetBody)) {
									if (calculateDistance(currentBot.body, targetBody) < lowestDistanceToFood) {

										lowestDistanceToFood = calculateDistance(currentBot.body, targetBody);
										angleToFood = calculateAngle(currentBot.body, targetBody);

										
										

									}

								}

								// If body is a runner and is not himself

								if ((BodyUtils.bodyIsRunner(targetBody) || BodyUtils.bodyIsBot(targetBody))
										&& targetBody != currentBot.body) {

									if (calculateDistance(currentBot.body, targetBody) < lowestDistanceToBot) {
										
										lowestDistanceToBot = calculateDistance(currentBot.body, targetBody);
										angleToBot = calculateAngle(currentBot.body, targetBody);
										sizeOfBot = calculateSize(targetBody);

										
									
									}

								}

							}

						}
						
					
					((BotUserData) (currentBot.getUserData()))
					.setDistanceToNearestPlayer(lowestDistanceToFood);
					((BotUserData) (currentBot.getUserData())).setAngleToNearestFood(angleToFood);
					((BotUserData) (currentBot.getUserData()))
					.setDistanceToNearestPlayer(lowestDistanceToBot);
					((BotUserData) (currentBot.getUserData())).setAngleToNearestPlayer(angleToBot);
					((BotUserData) (currentBot.getUserData())).setSizeOfNearestPlayer(sizeOfBot);
					
				}
				

			}
			// System.out.println(runner.body.getFixtureList().first().getShape().getRadius()
			// / 4.22);
			for (int x = 0; x < bot.size; x++) {

				if (bot.get(x).body.isActive()) {
					BotUserData botData = bot.get(x).getUserData();

					// double[] input = new double[5];
					BasicMLData input = new BasicMLData(8);

					input.add(0, botData.getDistanceToNearestFood() / 45);
					input.add(1, botData.getAngleToNearestFood() / (2 * MathUtils.PI));
					input.add(2, botData.getDistanceToNearestPlayer() / 45);
					input.add(3, botData.getAngleToNearestPlayer() / (2 * MathUtils.PI));
					input.add(4, botData.getSizeOfNearestPlayer() / 4);
					input.add(5, bot.get(x).body.getFixtureList().first().getShape().getRadius() / 4.22);
					if (bot.get(x).body.getLinearVelocity().len() > 0) {
						input.add(6, 1);
					} else {
						input.add(6, 0);
					}
					if (botData.getWallTouch() == true) {
						input.add(7, 1);
					} else {
						input.add(7, 0);
					}
					
					// inputData.setData(input);
					final MLData output = bot.get(x).network.compute(input);
					// System.out.println(output);
					float highest = -1;
					int highestID = 0;
					for (int y = 0; y < output.size(); y++) {
						if (output.getData(y) > highest) {
							highestID = y;
							highest = (float) output.getData(y);
						}
					}

					switch (highestID) {

					case 0:
						bot.get(x).moveForward();
						break;

					case 1:
						bot.get(x).turnRight();
						break;

					case 2:
						bot.get(x).turnLeft();
						break;

					case 3:
						bot.get(x).stop();
						break;

					}

				}

			}

			// Input keys
			if (leftKeyPressed) {
				runner.turnLeft();
			}
			if (rightKeyPressed) {
				runner.turnRight();
			}

			if (upKeyPressed) {
				runner.moveForward();
			} else if (!upKeyPressed) {
				runner.stop();
			}

			camera.position.set(runner.getX(), runner.getY(), 0);
			camera.update();
		}

		// Fixed timestep
		accumulator += delta;

		while (accumulator >= delta) {
			world.step(TIME_STEP, 6, 2);
			accumulator -= TIME_STEP;
		}

		// TODO: Implement interpolation

	}

	@Override
	public void draw() {
		super.draw();
		renderer.render(world, camera.combined);
	}

	// Calculation methods

	public float calculateDistance(Body a, Body target) {

		Vector2 position1 = a.getPosition();
		Vector2 position2 = target.getPosition();

		return position1.dst(position2);

	}

	public float calculateAngle(Body a, Body target) {

		Vector2 position1 = a.getPosition();
		Vector2 position2 = target.getPosition();

		float angleOfa = a.getAngle();
		float angleOfvector = position1.angleRad(position2);

		return (float) angleOfvector - angleOfa % (MathUtils.PI);

	}

	public float calculateSize(Body target) {
		return target.getFixtureList().first().getShape().getRadius();

	}

	public int bodySize(Array<Bot> b) {
		int size = 0;
		for (int x = 0; x < b.size; x++) {
			if (b.get(x).body.isActive())
				size++;
		}
		return size;
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

	@Override
	public void beginContact(Contact contact) {

		Body a = contact.getFixtureA().getBody();
		Body b = contact.getFixtureB().getBody();
		// Runner and food
		if ((BodyUtils.bodyIsRunner(a) && BodyUtils.bodyIsFood(b))
				|| (BodyUtils.bodyIsFood(a) && BodyUtils.bodyIsRunner(b))) {

			if (BodyUtils.bodyIsFood(a) && !(deletedBodies.contains(a, true))) {

				deletedBodies.add(a);

			} else if (BodyUtils.bodyIsFood(b) && !(deletedBodies.contains(b, true))) {

				deletedBodies.add(b);
			}

			runner.grow(0.02f);

		}
		// Bot and food
		if ((BodyUtils.bodyIsBot(a) && BodyUtils.bodyIsFood(b))
				|| (BodyUtils.bodyIsFood(a) && BodyUtils.bodyIsBot(b))) {

			if (BodyUtils.bodyIsFood(a) && !(deletedBodies.contains(a, true))) {

				growBodies.add(b);
				deletedBodies.add(a);

			} else if (BodyUtils.bodyIsFood(b) && !(deletedBodies.contains(b, true))) {

				growBodies.add(a);
				deletedBodies.add(b);

			}

		}
		// Runner and bot

		if ((BodyUtils.bodyIsRunner(a) && BodyUtils.bodyIsBot(b))
				|| (BodyUtils.bodyIsBot(a) && BodyUtils.bodyIsRunner(b))) {

			if (BodyUtils.bodyIsBot(a) && !(deletedBodies.contains(a, true))) {

				if (a.getFixtureList().first().getShape().getRadius() < runner.body.getFixtureList().first().getShape()
						.getRadius()) {

					geneRecord.add(bot.get(((BotUserData) (a.getUserData())).getID()).gene);
					timeRecord.add(((BotUserData) (a.getUserData())).getSeconds());

					deletedBodies.add(a);

					growBodies.add(b);

				} else if (a.getFixtureList().first().getShape().getRadius() > runner.body.getFixtureList().first()
						.getShape().getRadius()) {

					deletedBodies.add(b);
					runner.remove();
					messageLabel.setText("You lost this round!");
					growBodies.add(a);

				}

			} else if (BodyUtils.bodyIsBot(b) && !(deletedBodies.contains(b, true))) {

				if (b.getFixtureList().first().getShape().getRadius() < runner.body.getFixtureList().first().getShape()
						.getRadius()) {

					geneRecord.add(bot.get(((BotUserData) (b.getUserData())).getID()).gene);
					timeRecord.add(((BotUserData) (b.getUserData())).getSeconds());

					deletedBodies.add(b);

					growBodies.add(a);

				} else if (b.getFixtureList().first().getShape().getRadius() > runner.body.getFixtureList().first()
						.getShape().getRadius()) {

					deletedBodies.add(a);
					runner.remove();
					messageLabel.setText("You lost this round!");
					growBodies.add(b);
				}
			}

		}
		// Bot and bot
		if ((BodyUtils.bodyIsBot(a) && BodyUtils.bodyIsBot(b)) || (BodyUtils.bodyIsBot(a) && BodyUtils.bodyIsBot(b))) {

			if (!(deletedBodies.contains(a, true)) && !(deletedBodies.contains(b, true))) {
				// System.out.println(((BotUserData)
				// (a.getUserData())).getID());

				if (a.getFixtureList().first().getShape().getRadius() < b.getFixtureList().first().getShape()
						.getRadius()) {

					deletedBodies.add(a);

					geneRecord.add(bot.get(((BotUserData) (a.getUserData())).getID()).gene);
					timeRecord.add(((BotUserData) (a.getUserData())).getSeconds());

					growBodies.add(b);

				} else if (a.getFixtureList().first().getShape().getRadius() > b.getFixtureList().first().getShape()
						.getRadius()) {

					deletedBodies.add(b);

					geneRecord.add(bot.get(((BotUserData) (b.getUserData())).getID()).gene);
					timeRecord.add(((BotUserData) (b.getUserData())).getSeconds());

					growBodies.add(a);

				}

			}
		}

		if ((BodyUtils.bodyIsBot(a) && BodyUtils.bodyIsWall(b))
				|| (BodyUtils.bodyIsWall(a) && BodyUtils.bodyIsBot(b))) {

			if (BodyUtils.bodyIsBot(a) && BodyUtils.bodyIsWall(b)) {

				((BotUserData) (a.getUserData())).setWallTouch(true);

			} else if (BodyUtils.bodyIsBot(b) && BodyUtils.bodyIsWall(a)) {

				((BotUserData) (b.getUserData())).setWallTouch(true);

			}

		}
	}

	@Override
	public void endContact(Contact contact) {
		
		Body a = contact.getFixtureA().getBody();
		Body b = contact.getFixtureB().getBody();
		
		if ((BodyUtils.bodyIsBot(a) && BodyUtils.bodyIsWall(b))
				|| (BodyUtils.bodyIsWall(a) && BodyUtils.bodyIsBot(b))) {
			System.out.println("yay");
			if (BodyUtils.bodyIsBot(a) && BodyUtils.bodyIsWall(b)) {

				((BotUserData) (a.getUserData())).setWallTouch(false);
				

			} else if (BodyUtils.bodyIsBot(b) && BodyUtils.bodyIsWall(a)) {

				((BotUserData) (b.getUserData())).setWallTouch(false);

			}

		}

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

}
