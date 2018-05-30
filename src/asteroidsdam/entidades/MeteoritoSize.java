package asteroidsdam.entidades;

import java.awt.Polygon;

public enum MeteoritoSize {
	
//------------------------------Atributos-------------------------------------//
    //Enumerables
	Peque(15.0, 100),
	Mediano(25.0, 50),
	Grande(40.0, 20);
	
	private static final int NUMERO_PUNTOS = 5;
	public final Polygon poligono;
	public final int puntosVida;
	public final double radioColision;
	
//------------------------------Constructor-----------------------------------//
	private MeteoritoSize(double radio, int valor) {
		this.poligono = generarPoligono(radio);
		this.radioColision = radio + 1.0;
		this.puntosVida = valor;
	}

//----------------------------Métodos públicos--------------------------------//
        //Generar poligono
	private static Polygon generarPoligono(double radio) {
		int[] x = new int[NUMERO_PUNTOS];
		int[] y = new int[NUMERO_PUNTOS];
		double angulo = (2 * Math.PI / NUMERO_PUNTOS);
		for(int i = 0; i < NUMERO_PUNTOS; i++) {
			x[i] = (int) (radio * Math.sin(i * angulo));
			y[i] = (int) (radio * Math.cos(i * angulo));
		}
		return new Polygon(x, y, NUMERO_PUNTOS); //====================>
	}

}
