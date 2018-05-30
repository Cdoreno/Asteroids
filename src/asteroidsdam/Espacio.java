package asteroidsdam;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.Iterator;
import javax.swing.JPanel;
import asteroidsdam.entidades.Entidad;

public class Espacio extends JPanel {

//------------------------------Atributos-------------------------------------//
    private static final Font FUENTE_SUBTITULO = new Font("Dialog", Font.PLAIN, 15);
    private static final Font FUENTE_TITULO = new Font("Dialog", Font.PLAIN, 25);
    private final Juego juego;
    public static final int SIZE_ESPACIO = 550;

//------------------------------Constructor-----------------------------------//
    public Espacio(Juego juego) {
        this.juego = juego;

        setPreferredSize(new Dimension(SIZE_ESPACIO, SIZE_ESPACIO));
        setBackground(Color.BLACK);
    }

//----------------------------Métodos públicos--------------------------------//
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        AffineTransform identity = g2d.getTransform();

        Iterator<Entidad> bucle = juego.getEntities().iterator();
        while (bucle.hasNext()) {
            Entidad entidad = bucle.next();
            if (entidad != juego.getPlayer() || juego.pintarJugador()) {
                Vector pos = entidad.getPos();
                dibujarEntidad(g2d, entidad, pos.x, pos.y);
                g2d.setTransform(identity);
                double radio = entidad.getRadioColision();
                double x = (pos.x < radio) ? pos.x + SIZE_ESPACIO
                        : (pos.x > SIZE_ESPACIO - radio) ? pos.x - SIZE_ESPACIO : pos.x;
                double y = (pos.y < radio) ? pos.y + SIZE_ESPACIO
                        : (pos.y > SIZE_ESPACIO - radio) ? pos.y - SIZE_ESPACIO : pos.y;
                if (x != pos.x || y != pos.y) {
                    dibujarEntidad(g2d, entidad, x, y);
                    g2d.setTransform(identity);
                }
            }
        }
        if (!juego.juegoFinalizado()) {
            g.drawString("Puntuación: " + juego.getPuntuacion(), 10, 15);
        }
        if (juego.juegoFinalizado()) {
            dibujarTextoCentrado("Juego terminado", FUENTE_TITULO, g2d, -25);
            dibujarTextoCentrado("Puntuación final: " + juego.getPuntuacion(), FUENTE_SUBTITULO, g2d, 10);
        } else if (juego.pausa()) {
            dibujarTextoCentrado("Pausa", FUENTE_TITULO, g2d, -25);
        } else if (juego.nivelMostrado()) {
            dibujarTextoCentrado("Nivel: " + juego.getNivel(), FUENTE_TITULO, g2d, -25);
        }
        
        g2d.translate(15, 30);
        g2d.scale(0.85, 0.85);
        for (int i = 0; i < juego.getVidas(); i++) {
            g2d.drawLine(-8, 10, 0, -10);
            g2d.drawLine(8, 10, 0, -10);
            g2d.drawLine(-6, 6, 6, 6);
            g2d.translate(30, 0);
        }
    }
    
//----------------------------Métodos privados--------------------------------//
    private void dibujarEntidad(Graphics2D g2d, Entidad entity, double x, double y) {
        g2d.translate(x, y);
        double rotation = entity.getRotacion();
        if (rotation != 0.0f) {
            g2d.rotate(entity.getRotacion());
        }
        entity.pintarEnEspacio(g2d, juego);
    }

    private void dibujarTextoCentrado(String text, Font font, Graphics2D g, int y) {
        g.setFont(font);
        g.drawString(text, SIZE_ESPACIO / 2 - g.getFontMetrics().stringWidth(text) / 2, SIZE_ESPACIO / 2 + y);
    }

}
