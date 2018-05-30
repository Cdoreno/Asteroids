package asteroidsdam.entidades;

import java.awt.Graphics2D;
import asteroidsdam.Juego;
import asteroidsdam.Vector;

public class Disparo extends Entidad {
    
//------------------------------Atributos-------------------------------------//
    private static final int MAX_CICLOS = 60;
    private static final double VELOCIDAD = 6.75;
    private int VIDA_DISPARO;

//------------------------------Consturctor-----------------------------------//
    public Disparo(Entidad nave, double angulo) {
        super(new Vector(nave.pos), new Vector(angulo).scale(VELOCIDAD), 2.0, 0);
        this.VIDA_DISPARO = MAX_CICLOS;
    }

//----------------------------Métodos públicos--------------------------------//
    @Override
    public void actualizar(Juego juego) {
        super.actualizar(juego);
        this.VIDA_DISPARO--;
        if (VIDA_DISPARO <= 0) {
            marcarParaEliminar();
        }
    }

    @Override
    public void colision(Juego juego, Entidad otroObjeto) {
        if (otroObjeto.getClass() != Jugador.class) {
            marcarParaEliminar();
        }
    }

    @Override
    public void pintarEnEspacio(Graphics2D g, Juego juego) {
        g.drawOval(-1, -1, 2, 2);
    }

}
