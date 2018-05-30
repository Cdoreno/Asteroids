package asteroidsdam.entidades;

import java.awt.Graphics2D;
import java.util.Random;

import asteroidsdam.Juego;
import asteroidsdam.Espacio;
import asteroidsdam.Vector;

public class Meteroito extends Entidad {

//------------------------------Atributos-------------------------------------//
    private static final float ACTUALIACIONES_PRE_SALIDA = 10;
    private static final double MAX_DISTANCIA = Espacio.SIZE_ESPACIO / 2.0;
    private static final double MIN_DISTANCIA = 200.0;
    private static final double MAX_ROTACION = 0.0175;
    private static final double MIN_ROTACION = 0.0075;
    private static final double MAX_VEL = 1.65;
    private static final double MIN_VEL = 0.75;
    private static final double VARIACION_DISTANCIA = MAX_DISTANCIA - MIN_DISTANCIA;
    private static final double VARIACION_ROTACION = MAX_ROTACION - MIN_ROTACION;
    private static final double VARIACION_VEL = MAX_VEL - MIN_VEL;

    private final double rotacionVel;
    private final MeteoritoSize size;

//------------------------------Consturctor-----------------------------------//
    //Grande
    public Meteroito(Random random) {
        super(calcularPos(random), calcularVel(random), MeteoritoSize.Grande.radioColision, MeteoritoSize.Grande.puntosVida);
        this.rotacionVel = -MIN_ROTACION + (random.nextDouble() * VARIACION_ROTACION);
        this.size = MeteoritoSize.Grande;
    }

    //Más pequeños que el padre
    public Meteroito(Meteroito padre, MeteoritoSize size, Random random) {
        super(new Vector(padre.pos), calcularVel(random), size.radioColision, size.puntosVida);
        this.rotacionVel = MIN_ROTACION + (random.nextDouble() * VARIACION_ROTACION);
        this.size = size;

        for (int i = 0; i < ACTUALIACIONES_PRE_SALIDA; i++) {
            actualizar(null);
        }
    }

//----------------------------Métodos públicos--------------------------------//
    @Override
    public void actualizar(Juego game) {
        super.actualizar(game);
        rotar(rotacionVel); //Rotate the image each frame.
    }

    @Override
    public void colision(Juego juego, Entidad otraEntidad) {
        if (otraEntidad.getClass() != Meteroito.class) {
            if (size != MeteoritoSize.Peque) {
                MeteoritoSize salidaSize = MeteoritoSize.values()[size.ordinal() - 1];
                for (int i = 0; i < 2; i++) {
                    juego.guardarEntidad(new Meteroito(this, salidaSize, juego.getRandom()));
                }
            }
            marcarParaEliminar();
            juego.addPuntuacion(getPuntuacion());
        }
    }

    @Override
    public void pintarEnEspacio(Graphics2D g, Juego game) {
        g.drawPolygon(size.poligono); //Draw the Meteroito.
    }

    private static Vector calcularPos(Random random) {
        Vector vec = new Vector(Espacio.SIZE_ESPACIO / 2.0, Espacio.SIZE_ESPACIO / 2.0);
        return vec.add(new Vector(random.nextDouble() * Math.PI * 2).scale(MIN_DISTANCIA + random.nextDouble() * VARIACION_DISTANCIA));
    }

    private static Vector calcularVel(Random random) {
        return new Vector(random.nextDouble() * Math.PI * 2).scale(MIN_VEL + random.nextDouble() * VARIACION_VEL);
    }

}
