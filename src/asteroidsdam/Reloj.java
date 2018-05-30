package asteroidsdam;

public class Reloj {

//------------------------------Atributos-------------------------------------//
    private int contadorCiclos;
    private float excesoCiclos;
    private float milisegundosPorCiclo;
    private boolean pausa;
    private long ultimaActualizacion;

//------------------------------Constructor-----------------------------------//
    public Reloj(float cyclesPerSecond) {
        setCiclosPorSegundo(cyclesPerSecond);
        reset();
    }

//----------------------------Métodos públicos--------------------------------//
    public void actualizar() {
        long tiempoActual = getTiempoActual();
        float aux = (float) (tiempoActual - ultimaActualizacion) + excesoCiclos;
        if (!pausa) {
            this.contadorCiclos += (int) Math.floor(aux / milisegundosPorCiclo);
            this.excesoCiclos = aux % milisegundosPorCiclo;
        }
        this.ultimaActualizacion = tiempoActual;
    }

    public void reset() {
        this.contadorCiclos = 0;
        this.excesoCiclos = 0.0f;
        this.ultimaActualizacion = getTiempoActual();
        this.pausa = false;
    }

    public boolean tiempoTranscurrido() {
        if (contadorCiclos > 0) {
            this.contadorCiclos--;
            return true;
        }
        return false;
    }

//------------------------------Gets & Sets-----------------------------------//
    public boolean getPausa() {
        return pausa;
    }

    private static final long getTiempoActual() {
        return (System.nanoTime() / 1000000L);
    }

    public boolean getTiempoTranscurrido() {
        return (contadorCiclos > 0);
    }

    public void setCiclosPorSegundo(float cyclesPerSecond) {
        this.milisegundosPorCiclo = (1.0f / cyclesPerSecond) * 1000;
    }

    public void setPausa(boolean pausa) {
        this.pausa = pausa;
    }
}
