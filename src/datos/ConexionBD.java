package datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConexionBD {
	private final static String url = "jdbc:mysql://localhost:3306/prjuego";
	private final static String user = "root";
	private final static String password = "1234";

	private Connection getConexion() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}

	// --- COMPROBACIÓN DE LOGIN CONTRA LA TABLA DE LA BD ---
	public String validarLogin(String usuario, String clave) {
		String sql = "SELECT TipoUsuario FROM Usuarios WHERE NombreUsuario = ? AND ClaveEncriptadaUsuario = ?";
		try (Connection con = getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, usuario);
			ps.setString(2, clave);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					// Devuelve el rol de la tabla: 'administrador' o 'basico'
					return rs.getString("TipoUsuario"); 
				}
			}
		} catch (SQLException e) { 
			System.out.println("Error Login DB: " + e.getMessage()); 
		}
		return null; // Si las credenciales no existen en la tabla, devuelve null
	}

    /**
     * Guarda el nombre y los puntos directamente en la tabla 'jugadores' de PrJuego
     */
    public static void guardarPuntuacion(String nombre, int puntuacion) {
        String sql = "INSERT INTO jugadores (nombreJugador, puntuacionJugador) VALUES (?, ?)";
        
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nombre);
            pstmt.setInt(2, puntuacion);
            pstmt.executeUpdate();
            System.out.println("Partida guardada con éxito en PrJuego.");
            
        } catch (Exception e) {
            System.err.println("Error al guardar la puntuación en PrJuego: " + e.getMessage());
        }
    }

    /**
     * Recupera el Top 10 ordenando las mejores puntuaciones de PrJuego
     */
    public static List<String[]> obtenerTop10() {
        List<String[]> ranking = new ArrayList<>();
        String sql = "SELECT nombreJugador, puntuacionJugador FROM jugadores ORDER BY puntuacionJugador DESC LIMIT 10";
                     
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String nombre = rs.getString("nombreJugador");
                String puntos = String.valueOf(rs.getInt("puntuacionJugador"));
                ranking.add(new String[]{nombre, puntos});
            }
        } catch (Exception e) {
            System.err.println("Error al obtener el ranking de PrJuego: " + e.getMessage());
        }
        return ranking;
    }
}