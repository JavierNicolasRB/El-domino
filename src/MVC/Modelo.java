package MVC;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Modelo {
    
    // ==========================================================
    // CLASES INTERNAS DE DATOS
    // ==========================================================
    public record Ficha(int ladoA, int ladoB) {
        public Ficha voltear() { return new Ficha(ladoB, ladoA); }
        public int getPuntos() { return ladoA + ladoB; }
        @Override public String toString() { return "[" + ladoA + "|" + ladoB + "]"; }
        @Override public boolean equals(Object o) { 
            if(o instanceof Ficha f) return (this.ladoA == f.ladoA && this.ladoB == f.ladoB); 
            return false; 
        }
    }

    public static class Tablero {
        private final LinkedList<Ficha> fichasEnJuego = new LinkedList<>();
        public LinkedList<Ficha> getFichasEnJuego() { return fichasEnJuego; }
        public boolean estaVacio() { return fichasEnJuego.isEmpty(); }
        public int getExtremoIzquierdo() { return estaVacio() ? -1 : fichasEnJuego.getFirst().ladoA(); }
        public int getExtremoDerecho() { return estaVacio() ? -1 : fichasEnJuego.getLast().ladoB(); }
        
        public boolean jugarPorIzquierda(Ficha ficha) {
            if (estaVacio() || ficha.ladoB() == getExtremoIzquierdo()) { fichasEnJuego.addFirst(ficha); return true; }
            else if (ficha.ladoA() == getExtremoIzquierdo()) { fichasEnJuego.addFirst(ficha.voltear()); return true; }
            return false;
        }
        
        public boolean jugarPorDerecha(Ficha ficha) {
            if (estaVacio() || ficha.ladoA() == getExtremoDerecho()) { fichasEnJuego.addLast(ficha); return true; }
            else if (ficha.ladoB() == getExtremoDerecho()) { fichasEnJuego.addLast(ficha.voltear()); return true; }
            return false;
        }
    }

    // ==========================================================
    // ESTADO DE LA PARTIDA
    // ==========================================================
    private Tablero tablero;
    private List<Ficha> manoJugador;
    private List<Ficha> manoBot;
    private List<Ficha> pozo;
    private String nombreJugador;

    public void iniciarNuevaPartida(String nombre) {
        this.nombreJugador = nombre;
        this.tablero = new Tablero();
        this.manoJugador = new ArrayList<>();
        this.manoBot = new ArrayList<>();
        this.pozo = new ArrayList<>();
        
        List<Ficha> todasLasFichas = new ArrayList<>();
        for (int i = 0; i <= 6; i++) {
            for (int j = i; j <= 6; j++) todasLasFichas.add(new Ficha(i, j));
        }
        Collections.shuffle(todasLasFichas);

        for (int k = 0; k < 7; k++) {
            manoJugador.add(todasLasFichas.remove(0));
            manoBot.add(todasLasFichas.remove(0));
        }
        pozo = todasLasFichas; 
    }

    public Tablero getTablero() { return tablero; }
    public List<Ficha> getManoJugador() { return manoJugador; }
    public List<Ficha> getManoBot() { return manoBot; }
    public List<Ficha> getPozo() { return pozo; }
    public String getNombreJugador() { return nombreJugador; }

    public boolean jugadorRobaFicha() {
        if (!pozo.isEmpty()) { manoJugador.add(pozo.remove(0)); return true; }
        return false;
    }

    public boolean botRobaFicha() {
        if (!pozo.isEmpty()) { manoBot.add(pozo.remove(0)); return true; }
        return false;
    }

    public int getPuntosJugador() { return manoJugador.stream().mapToInt(Ficha::getPuntos).sum(); }
    public int getPuntosBot() { return manoBot.stream().mapToInt(Ficha::getPuntos).sum(); }

    // ==========================================================
    // BASE DE DATOS (Ranking)
    // ==========================================================
    // Asegúrate de que el puerto (3306), el usuario (root) y la clave (1234) son los tuyos.
    private final String urlBD = "jdbc:mysql://localhost:3306/PrJuego";
    private final String userBD = "root";
    private final String passBD = "1234";

    // AHORA DEVUELVE BOOLEAN PARA SABER SI HA IDO BIEN
    public boolean guardarPuntuacion(int puntuacion) {
        String sql = "INSERT INTO jugadores (nombreJugador, puntuacionJugador) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(urlBD, userBD, passBD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombreJugador);
            pstmt.setInt(2, puntuacion);
            pstmt.executeUpdate();
            return true; // Se guardó con éxito
        } catch (Exception e) { 
            System.err.println("Error al guardar BD: " + e.getMessage()); 
            return false; // Falló al guardar
        }
    }

    public List<String[]> obtenerTop10() {
        List<String[]> ranking = new ArrayList<>();
        String sql = "SELECT nombreJugador, puntuacionJugador FROM jugadores ORDER BY puntuacionJugador DESC LIMIT 10";
        try (Connection conn = DriverManager.getConnection(urlBD, userBD, passBD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ranking.add(new String[]{rs.getString("nombreJugador"), String.valueOf(rs.getInt("puntuacionJugador"))});
            }
        } catch (Exception e) { 
            System.err.println("Error al leer Top 10: " + e.getMessage()); 
        }
        return ranking;
    }
}