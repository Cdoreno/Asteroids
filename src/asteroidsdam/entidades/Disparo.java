package asteroidsdam.entidades;

import java.awt.Graphics2D;

import asteroidsdam.Juego;
import asteroidsdam.Vector;

/**
 * Represents a Disparo within the game world.
 * @author Brendan Jones
 *
 */
public class Disparo extends Entidad {
	
	/**
	 * The magnitude of the velocity of a Disparo.
	 */
	private static final double VELOCITY_MAGNITUDE = 6.75;
	
	/**
	 * The maximum number of cycles that a Disparo can exist.
	 */
	private static final int MAX_LIFESPAN = 60;
	
	/**
	 * The number of cycles this Disparo has existed.
	 */
	private int lifespan;

	/**
	 * Creates a new Bullet instance.
	 * @param owner The object that fired the bullet.
	 * @param angle The direction of the Bullet.
	 */
	public Disparo(Entidad owner, double angle) {
		super(new Vector(owner.position), new Vector(angle).scale(VELOCITY_MAGNITUDE), 2.0, 0);
		this.lifespan = MAX_LIFESPAN;
	}
	
	@Override
	public void update(Juego game) {
		super.update(game);
		
		//Decrement the lifespan of the bullet, and remove it if needed.
		this.lifespan--;
		if(lifespan <= 0) {
			flagForRemoval();
		}
	}

	@Override
	public void handleCollision(Juego game, Entidad other) {
		if(other.getClass() != Jugador.class) {
			flagForRemoval();
		}
	}
	
	@Override
	public void draw(Graphics2D g, Juego game) {
		g.drawOval(-1, -1, 2, 2);
	}

}
