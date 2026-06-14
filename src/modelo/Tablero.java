package modelo;

import java.util.LinkedList;

public class Tablero {
    private final LinkedList<Ficha> fichasEnJuego;

    public Tablero() {
        this.fichasEnJuego = new LinkedList<>();
    }

    public LinkedList<Ficha> getFichasEnJuego() {
        return fichasEnJuego;
    }

    public boolean estaVacio() {
        return fichasEnJuego.isEmpty();
    }

    // Obtener los extremos libres del tablero
    public int getExtremoIzquierdo() {
        return estaVacio() ? -1 : fichasEnJuego.getFirst().ladoA();
    }

    public int getExtremoDerecho() {
        return estaVacio() ? -1 : fichasEnJuego.getLast().ladoB();
    }

    // Agregar fichas validando las reglas
    public boolean jugarPorIzquierda(Ficha ficha) {
        if (estaVacio()) {
            fichasEnJuego.addFirst(ficha);
            return true;
        }
        if (ficha.ladoB() == getExtremoIzquierdo()) {
            fichasEnJuego.addFirst(ficha);
            return true;
        } else if (ficha.ladoA() == getExtremoIzquierdo()) {
            fichasEnJuego.addFirst(ficha.voltear());
            return true;
        }
        return false;
    }

    public boolean jugarPorDerecha(Ficha ficha) {
        if (estaVacio()) {
            fichasEnJuego.addLast(ficha);
            return true;
        }
        if (ficha.ladoA() == getExtremoDerecho()) {
            fichasEnJuego.addLast(ficha);
            return true;
        } else if (ficha.ladoB() == getExtremoDerecho()) {
            fichasEnJuego.addLast(ficha.voltear());
            return true;
        }
        return false;
    }
}