package controlador;

import modelo.Ficha;
import modelo.Tablero;
import java.util.List;
import java.util.Optional;

public class BotInteligencia {
    private final String nombre;

    public BotInteligencia() {
        this.nombre = "Er Bot Manué";
    }

    /**
     * Analiza la mano del bot y decide qué ficha jugar y en qué extremo del tablero.
     * Devuelve un registro o estructura con la decisión.
     */
    public Optional<Jugada> decidirJugada(List<Ficha> manoBot, Tablero tablero) {
        if (tablero.estaVacio()) {
            // Si el tablero está vacío, el bot juega su primera ficha por la derecha
            return Optional.of(new Jugada(manoBot.get(0), true));
        }

        int izq = tablero.getExtremoIzquierdo();
        int der = tablero.getExtremoDerecho();

        for (Ficha ficha : manoBot) {
            // Comprobar si encaja en el extremo izquierdo
            if (ficha.ladoA() == izq || ficha.ladoB() == izq) {
                return Optional.of(new Jugada(ficha, true)); // true = Izquierda
            }
            // Comprobar si encaja en el extremo derecho
            if (ficha.ladoA() == der || ficha.ladoB() == der) {
                return Optional.of(new Jugada(ficha, false)); // false = Derecha
            }
        }

        // Si no tiene fichas válidas, devuelve vacío (Pasa el turno / "Chupa")
        return Optional.empty();
    }

    public String getNombre() {
        return nombre;
    }

    // Record de Java 21 para estructurar la decisión de la IA
    public record Jugada(Ficha ficha, boolean esIzquierda) {}
}