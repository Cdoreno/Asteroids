package asteroidsdam;

public class Vector {

//------------------------------Atributos-------------------------------------//
    public double x;
    public double y;

//------------------------------Constructor-----------------------------------//
    public Vector(double angulo) {
        this.x = Math.cos(angulo);
        this.y = Math.sin(angulo);
    }

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector(Vector vec) {
        this.x = vec.x;
        this.y = vec.y;
    }

//----------------------------Métodos públicos--------------------------------//    
    public Vector normalizar() {
        double longitud = getLongitud();
        if (longitud != 0.0f && longitud != 1.0f) {
            longitud = Math.sqrt(longitud);
            this.x /= longitud;
            this.y /= longitud;
        }
        return this;
    }

    public Vector nuevo(Vector vec) {
        this.x += vec.x;
        this.y += vec.y;
        return this;
    }

    public Vector size(double sized) {
        this.x *= sized;
        this.y *= sized;
        return this;
    }

//------------------------------Gets & Sets-----------------------------------//
    public double getDistancia(Vector vec) {
        double dx = this.x - vec.x;
        double dy = this.y - vec.y;
        return (dx * dx + dy * dy);
    }

    public double getLongitud() {
        return (x * x + y * y);
    }

    public Vector set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }
}
