package asteroidsdam;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;
import asteroidsdam.entidades.Meteroito;
import asteroidsdam.entidades.Entidad;
import asteroidsdam.entidades.Jugador;

public class Juego extends JFrame {

//------------------------------Atributos-------------------------------------//
    private List<Entidad> entidades;
    private List<Entidad> entidadesPendientes;
    private static final int FPS = 60;
    private static final long FRAME_TIME = (long) (1000000000.0 / FPS);
    private static final int INVENCIBLE_TIEMPO = 0;
    private Jugador jugador;
    private static final int LIMITE_FPS_NIVEL = 60;
    private static final int MIN_TIEMPO_REAPARICION = 100;
    private int nivel;
    private boolean partidaFinalizada;
    private int puntuacion;
    private Random random;
    private Reloj reloj;
    private boolean reniciarJuego;
    private int tiempoReaparicion;
    private int tiempoCambioNivel;
    private int tiempoReset;
    private static final int TIEMPO_REAPARICION = 200;
    private static final int TIEMPO_RESET = 120;
    private int vidas;
    private final Espacio world;

//------------------------------Main de la clase------------------------------//
    public static void main(String[] args) {
        Juego game = new Juego();
        game.iniciarJuego();
    }

//------------------------------Constructor-----------------------------------//
    private Juego() {
        super("AsteroidsDAM");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        add(this.world = new Espacio(this), BorderLayout.CENTER);
        //Eventos de las teclas
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {

                    case KeyEvent.VK_W:
                    case KeyEvent.VK_UP:
                        if (!reniciar()) {
                            jugador.setAvance(true);
                        }
                        break;

                    case KeyEvent.VK_A:
                    case KeyEvent.VK_LEFT:
                        if (!reniciar()) {
                            jugador.setIzquierdaPulsado(true);
                        }
                        break;

                    case KeyEvent.VK_D:
                    case KeyEvent.VK_RIGHT:
                        if (!reniciar()) {
                            jugador.setDerechaPulsado(true);
                        }
                        break;

                    case KeyEvent.VK_SPACE:
                        if (!reniciar()) {
                            jugador.setDisparoPulsado(true);
                        }
                        break;

                    case KeyEvent.VK_P:
                        if (!reniciar()) {
                            reloj.setPausa(!reloj.getPausa());
                        }
                        break;

                    default:
                        reniciar();
                        break;

                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {

                    case KeyEvent.VK_W:
                    case KeyEvent.VK_UP:
                        jugador.setAvance(false);
                        break;

                    case KeyEvent.VK_A:
                    case KeyEvent.VK_LEFT:
                        jugador.setIzquierdaPulsado(false);
                        break;

                    case KeyEvent.VK_D:
                    case KeyEvent.VK_RIGHT:
                        jugador.setDerechaPulsado(false);
                        break;

                    case KeyEvent.VK_SPACE:
                        jugador.setDisparoPulsado(false);
                        break;
                }
            }
        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

//----------------------------Métodos públicos--------------------------------//
    public void addPuntuacion(int puntuacion) {
        this.puntuacion += puntuacion;
    }

    public void guardarEntidad(Entidad entidad) {
        entidadesPendientes.add(entidad);
    }

    public boolean jugadorInvencible() {
        return (tiempoReaparicion > INVENCIBLE_TIEMPO);
    }

    public boolean juegoFinalizado() {
        return partidaFinalizada;
    }

    public boolean pausa() {
        return reloj.getPausa();
    }

    public boolean pintarJugador() {
        return (tiempoReaparicion <= MIN_TIEMPO_REAPARICION);
    }

    public void matarJugador() {
        this.vidas--;

        if (vidas == 0) {
            this.partidaFinalizada = true;
            this.tiempoReset = TIEMPO_RESET;
            this.tiempoReaparicion = Integer.MAX_VALUE;
        } else {
            this.tiempoReaparicion = TIEMPO_REAPARICION;
        }

        jugador.setPuedeDisparar(false);
    }

    public boolean nivelMostrado() {
        return (tiempoCambioNivel > 0);
    }

//----------------------------Métodos privados--------------------------------//
    private void actualizarJuego() {

        entidades.addAll(entidadesPendientes);
        entidadesPendientes.clear();

        if (tiempoReset > 0) {
            this.tiempoReset--;
        }

        if (tiempoCambioNivel > 0) {
            this.tiempoCambioNivel--;
        }

        if (partidaFinalizada && reniciarJuego) {
            resetJuego();
        }

        if (!partidaFinalizada && enemigosMuertos()) {
            this.nivel++;
            this.tiempoCambioNivel = LIMITE_FPS_NIVEL;

            resetListaEntidades();

            jugador.reset();
            jugador.setPuedeDisparar(true);

            for (int i = 0; i < nivel + 2; i++) {
                guardarEntidad(new Meteroito(random));
            }
        }

        if (tiempoReaparicion > 0) {
            this.tiempoReaparicion--;
            switch (tiempoReaparicion) {

                case MIN_TIEMPO_REAPARICION:
                    jugador.reset();
                    jugador.setPuedeDisparar(false);
                    break;

                case INVENCIBLE_TIEMPO:
                    jugador.setPuedeDisparar(true);
                    break;

            }
        }

        if (tiempoCambioNivel == 0) {
            for (Entidad entity : entidades) {
                entity.actualizar(this);
            }
            for (int i = 0; i < entidades.size(); i++) {
                Entidad a = entidades.get(i);
                for (int j = i + 1; j < entidades.size(); j++) {
                    Entidad b = entidades.get(j);
                    if (i != j && a.mirarSiColision(b) && ((a != jugador && b != jugador) || tiempoReaparicion <= INVENCIBLE_TIEMPO)) {
                        a.colision(this, b);
                        b.colision(this, a);
                    }
                }
            }

            Iterator<Entidad> iter = entidades.iterator();
            while (iter.hasNext()) {
                if (iter.next().getSiRequiereEliminacion()) {
                    iter.remove();
                }
            }
        }
    }

    private boolean enemigosMuertos() {
        for (Entidad e : entidades) {
            if (e.getClass() == Meteroito.class) {
                return false;
            }
        }
        return true;
    }

    private void iniciarJuego() {
        this.random = new Random();
        this.entidades = new LinkedList<Entidad>();
        this.entidadesPendientes = new ArrayList<>();
        this.jugador = new Jugador();

        resetJuego();

        this.reloj = new Reloj(FPS);
        while (true) {
            long start = System.nanoTime();

            reloj.actualizar();
            for (int i = 0; i < 5 && reloj.tiempoTranscurrido(); i++) {
                actualizarJuego();
            }

            world.repaint();

            long aux = FRAME_TIME - (System.nanoTime() - start);
            if (aux > 0) {
                try {
                    Thread.sleep(aux / 1000000L, (int) aux % 1000000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean reniciar() {
        boolean restart = (partidaFinalizada && tiempoReset <= 0);
        if (restart) {
            reniciarJuego = true;
        }
        return restart;
    }

    private void resetJuego() {
        this.puntuacion = 0;
        this.nivel = 0;
        this.vidas = 3;
        this.tiempoReaparicion = 0;
        this.partidaFinalizada = false;
        this.reniciarJuego = false;
        resetListaEntidades();
    }

    private void resetListaEntidades() {
        entidadesPendientes.clear();
        entidades.clear();
        entidades.add(jugador);
    }

//------------------------------Gets & Sets-----------------------------------//
    public List<Entidad> getEntities() {
        return entidades;
    }

    public Jugador getPlayer() {
        return jugador;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public Random getRandom() {
        return random;
    }

    public int getNivel() {
        return nivel;
    }

    public int getVidas() {
        return vidas;
    }
}
