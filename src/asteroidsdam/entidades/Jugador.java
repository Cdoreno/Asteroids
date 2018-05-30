package asteroidsdam.entidades;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import asteroidsdam.Juego;
import asteroidsdam.Espacio;
import asteroidsdam.Vector;

public class Jugador extends Entidad {

//------------------------------Atributos-------------------------------------//
    private int animationFrame;
    private boolean AVANCE_PULSADO;
    private boolean DERECHA_PULSADO;
    private boolean DISPARO_PULSADO;
    private int disparosConsecutivos;
    private static final double FUERZA_AVANCE = 0.0385;
    private boolean IZQUIERDA_PULSADO;
    private static final int MAX_DISPAROS = 4;
    private static final int MAX_DISPAROS_CONSECUTIVOS = 8;
    private static final double MAX_FUERZA_AVANCE = 6.5;
    private static final int MAX_TIEMPO_SOBRECALENTAMIENTO = 30;
    private final List<Disparo> misDisparos;
    private boolean puedeDisparar;
    private static final double ROTACION_POR_DEFECTO = -Math.PI / 2.0;
    private int tiempoCalentamiento;
    private int tiempoEnfriamiento;
    private static final int TIEMPO_DISPAROS = 4;
    private static final double TIEMPO_FRENADO = 0.995;
    private static final double VEL_ROTACION = 0.052;

//------------------------------Consturctor-----------------------------------//
    public Jugador() {
        super(new Vector(Espacio.SIZE_ESPACIO / 2.0, Espacio.SIZE_ESPACIO / 2.0), new Vector(0.0, 0.0), 10.0, 0);
        this.misDisparos = new ArrayList<>();
        this.rotacion = ROTACION_POR_DEFECTO;
        this.AVANCE_PULSADO = false;
        this.IZQUIERDA_PULSADO = false;
        this.DERECHA_PULSADO = false;
        this.DISPARO_PULSADO = false;
        this.puedeDisparar = true;
        this.tiempoEnfriamiento = 0;
        this.tiempoCalentamiento = 0;
        this.animationFrame = 0;
    }

//----------------------------Métodos públicos--------------------------------//
    @Override
    public void actualizar(Juego game) {
        super.actualizar(game);
        this.animationFrame++;

        if (IZQUIERDA_PULSADO != DERECHA_PULSADO) {
            rotar(IZQUIERDA_PULSADO ? -VEL_ROTACION : VEL_ROTACION);
        }

        if (AVANCE_PULSADO) {
            vel.add(new Vector(rotacion).scale(FUERZA_AVANCE));
            if (vel.getLengthSquared() >= MAX_FUERZA_AVANCE * MAX_FUERZA_AVANCE) {
                vel.normalize().scale(MAX_FUERZA_AVANCE);
            }
        }

        if (vel.getLengthSquared() != 0.0) {
            vel.scale(TIEMPO_FRENADO);
        }

        Iterator<Disparo> iter = misDisparos.iterator();
        while (iter.hasNext()) {
            Disparo bullet = iter.next();
            if (bullet.getSiRequiereEliminacion()) {
                iter.remove();
            }
        }

        this.tiempoEnfriamiento--;
        this.tiempoCalentamiento--;
        if (puedeDisparar && DISPARO_PULSADO && tiempoEnfriamiento <= 0 && tiempoCalentamiento <= 0) {
            if (misDisparos.size() < MAX_DISPAROS) {
                this.tiempoEnfriamiento = TIEMPO_DISPAROS;

                Disparo bullet = new Disparo(this, rotacion);
                misDisparos.add(bullet);
                game.registerEntity(bullet);
            }
            this.disparosConsecutivos++;
            if (disparosConsecutivos == MAX_DISPAROS_CONSECUTIVOS) {
                this.disparosConsecutivos = 0;
                this.tiempoCalentamiento = MAX_TIEMPO_SOBRECALENTAMIENTO;
            }
        } else if (disparosConsecutivos > 0) {
            this.disparosConsecutivos--;
        }
    }

    @Override
    public void colision(Juego game, Entidad other) {
        if (other.getClass() == Meteroito.class) {
            game.matarJugador();
        }
    }

    @Override
    public void pintarEnEspacio(Graphics2D g, Juego game) {
        if (!game.jugadorInvencible() || game.pausa() || animationFrame % 20 < 10) {
            g.drawLine(-10, -8, 10, 0);
            g.drawLine(-10, 8, 10, 0);
            g.drawLine(-6, -6, -6, 6);

            if (!game.pausa() && AVANCE_PULSADO && animationFrame % 6 < 3) {
                g.drawLine(-6, -6, -12, 0);
                g.drawLine(-6, 6, -12, 0);
            }
        }
    }

    //Resetear la nave en caso de muerte o paso de nivel
    public void reset() {
        this.rotacion = ROTACION_POR_DEFECTO;
        pos.set(Espacio.SIZE_ESPACIO / 2.0, Espacio.SIZE_ESPACIO / 2.0);
        vel.set(0.0, 0.0);
        misDisparos.clear();
    }

//------------------------------Gets & Sets-----------------------------------//
    public void setAvance(boolean state) {
        this.AVANCE_PULSADO = state;
    }

    public void setDerechaPulsado(boolean state) {
        this.DERECHA_PULSADO = state;
    }

    public void setDisparoPulsado(boolean state) {
        this.DISPARO_PULSADO = state;
    }

    public void setIzquierdaPulsado(boolean state) {
        this.IZQUIERDA_PULSADO = state;
    }

    public void setPuedeDisparar(boolean state) {
        this.puedeDisparar = state;
    }
}
