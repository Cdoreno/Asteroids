package asteroidsdam.entidades;

import java.awt.Graphics2D;

import asteroidsdam.Juego;
import asteroidsdam.Espacio;
import asteroidsdam.Vector;

public abstract class Entidad {
//------------------------------Atributos-------------------------------------//
    protected Vector pos;
    protected double radioColision;
    protected double rotacion;
    protected Vector vel;

    private boolean requiereEliminacion;
    private final int puntuacionAlMorir;

//------------------------------Constructor-----------------------------------//
    //Constructor, crea una entidad
    public Entidad(Vector pos, Vector vel, double radio, int puntuacionAlMorir) {
        this.pos = pos;
        this.vel = vel;
        this.radioColision = radio;
        this.rotacion = 0.0f;
        this.puntuacionAlMorir = puntuacionAlMorir;
        this.requiereEliminacion = false;
    }

//----------------------------Métodos públicos--------------------------------//
    //Actualizar entidad en el espacio
    public void actualizar(Juego game) {
        pos.nuevo(vel);
        if (pos.x < 0.0f) {
            pos.x += Espacio.SIZE_ESPACIO;
        }
        if (pos.y < 0.0f) {
            pos.y += Espacio.SIZE_ESPACIO;
        }
        pos.x %= Espacio.SIZE_ESPACIO;
        pos.y %= Espacio.SIZE_ESPACIO;
    }

    //Colisiones entre entidades
    public abstract void colision(Juego juego, Entidad otraEntidad);

    //Eliminar entidad, en caso de necesitarlo
    public void marcarParaEliminar() {
        this.requiereEliminacion = true;
    }

    //Mirar si hubiese colisión entre entidades
    public boolean mirarSiColision(Entidad entidad) {
        double radio = entidad.getRadioColision() + getRadioColision();
        return (pos.getDistancia(entidad.pos) < radio * radio);
    }

    //Dibujar entidad en el espacio
    public abstract void pintarEnEspacio(Graphics2D g, Juego juego);

    public Vector getPos() {
        return pos;
    }

    //Rotar entidad
    public void rotar(double cantidad) {
        this.rotacion += cantidad;
        this.rotacion %= Math.PI * 2;
    }

//------------------------------Gets & Sets-----------------------------------//
    public int getPuntuacion() {
        return puntuacionAlMorir;
    }

    public double getRotacion() {
        return rotacion;
    }

    public double getRadioColision() {
        return radioColision;
    }

    public boolean getSiRequiereEliminacion() {
        return requiereEliminacion;
    }

    public Vector getVel() {
        return vel;
    }
}
