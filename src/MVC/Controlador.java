package MVC;

import java.awt.event.*;
import java.util.List;
import java.util.Optional;

public class Controlador implements ActionListener {
    private final Vista vista;
    private final Modelo modelo;

    public Controlador(Vista vista, Modelo modelo) {
        this.vista = vista;
        this.modelo = modelo;

        this.vista.btnNuevaPartida.addActionListener(this);
        this.vista.btnVerRankingMenu.addActionListener(this);
        this.vista.btnAyuda.addActionListener(this);
        this.vista.btnAceptarNombre.addActionListener(this);
        this.vista.btnCrear.addActionListener(this);
        this.vista.btnAceptarSala.addActionListener(this);
        this.vista.btnPedir.addActionListener(this);
        this.vista.btnVerRankingVic.addActionListener(this);
        this.vista.btnVolverMenu.addActionListener(this);
        this.vista.btnOkMensaje.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == vista.btnNuevaPartida) {
            vista.txtNombre.setText("");
            vista.dlgNombre.setLocationRelativeTo(vista.ventana);
            vista.dlgNombre.setVisible(true);
        } 
        else if (src == vista.btnAceptarNombre) {
            String nombre = vista.txtNombre.getText().trim();
            if (!nombre.isEmpty()) {
                vista.dlgNombre.setVisible(false);
                vista.abrirPantallaSalas();
            } else {
                vista.mostrarAviso("¡Introduce un nombre!");
            }
        } 
        else if (src == vista.btnAyuda) {
            try {
                ProcessBuilder pb = new ProcessBuilder("hh.exe", "juegopr.chm");
                pb.start();
            } catch (Exception ex) {
                vista.mostrarAviso("Error al intentar abrir el archivo de ayuda.");
            }
        }
        else if (src == vista.btnCrear) { vista.mostrarCodigoSala(); } 
        else if (src == vista.btnAceptarSala) { iniciarJuego(vista.txtNombre.getText()); } 
        else if (src == vista.btnVerRankingMenu || src == vista.btnVerRankingVic) {
            cargarRankingEnVista();
            vista.mostrarPantallaRanking();
        } 
        else if (src == vista.btnVolverMenu) { vista.mostrarMenuPrincipal(); } 
        else if (src == vista.btnOkMensaje) { vista.dlgMensaje.setVisible(false); }
        else if (src == vista.btnPedir) {
            if (modelo.jugadorRobaFicha()) {
                vista.mostrarAviso("Has robao una ficha del pozo.");
                actualizarPantallaJuego();
            } else {
                verificarCierre();
            }
        } 
    }

    // ==========================================================
    // MÉTODOS DE APOYO DEL JUEGO
    // ==========================================================

    private void iniciarJuego(String nombre) {
        modelo.iniciarNuevaPartida(nombre);
        actualizarPantallaJuego();
        vista.mostrarPantallaJuego();
    }

    private void actualizarPantallaJuego() {
        vista.areaTablero.fichasEnJuego = modelo.getTablero().getFichasEnJuego();
        vista.areaTablero.repaint();

        vista.areaMano.removeAll();
        for (Modelo.Ficha f : modelo.getManoJugador()) {
            Vista.FichaCanvas fichaDibujada = vista.new FichaCanvas(f);
            fichaDibujada.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    intentarJugadaUsuario(f);
                }
            });
            vista.areaMano.add(fichaDibujada);
        }
        
        vista.btnPedir.setLabel("Pedi Ficha (" + modelo.getPozo().size() + " en pozo)");
        vista.ventana.validate();
        vista.ventana.repaint(); 
    }

    public void intentarJugadaUsuario(Modelo.Ficha ficha) {
        boolean jugadaValida = false;
        
        if (modelo.getTablero().estaVacio() || ficha.ladoA() == modelo.getTablero().getExtremoDerecho() || ficha.ladoB() == modelo.getTablero().getExtremoDerecho()) {
            modelo.getTablero().jugarPorDerecha(ficha);
            jugadaValida = true;
        } else if (ficha.ladoA() == modelo.getTablero().getExtremoIzquierdo() || ficha.ladoB() == modelo.getTablero().getExtremoIzquierdo()) {
            modelo.getTablero().jugarPorIzquierda(ficha);
            jugadaValida = true;
        }

        if (jugadaValida) {
            modelo.getManoJugador().remove(ficha);
            actualizarPantallaJuego();
            
            // Si te quedas sin fichas, ganas la partida
            if (modelo.getManoJugador().isEmpty()) finalizarPartida(true);
            else ejecutarTurnoBot();
        } else {
            vista.mostrarAviso("¡Esa ficha no encaja ahi!");
        }
    }

    private void ejecutarTurnoBot() {
        Optional<Modelo.Ficha> fichaElegida = Optional.empty();
        boolean jugarPorIzquierda = false;
        
        if (modelo.getTablero().estaVacio()) {
            fichaElegida = Optional.of(modelo.getManoBot().get(0));
            jugarPorIzquierda = true;
        } else {
            for (Modelo.Ficha f : modelo.getManoBot()) {
                if (f.ladoA() == modelo.getTablero().getExtremoDerecho() || f.ladoB() == modelo.getTablero().getExtremoDerecho()) {
                    fichaElegida = Optional.of(f); jugarPorIzquierda = false; break;
                } else if (f.ladoA() == modelo.getTablero().getExtremoIzquierdo() || f.ladoB() == modelo.getTablero().getExtremoIzquierdo()) {
                    fichaElegida = Optional.of(f); jugarPorIzquierda = true; break;
                }
            }
        }
        
        if (fichaElegida.isPresent()) {
            Modelo.Ficha f = fichaElegida.get();
            if (jugarPorIzquierda) modelo.getTablero().jugarPorIzquierda(f);
            else modelo.getTablero().jugarPorDerecha(f);
            
            modelo.getManoBot().remove(f);
            actualizarPantallaJuego();
            
            // Si el bot se queda sin fichas, pierdes la partida
            if (modelo.getManoBot().isEmpty()) {
                vista.mostrarAviso("¡Ha ganao Er Bot Manue!");
                finalizarPartida(false); 
            }
        } else {
            if (modelo.botRobaFicha()) {
                vista.mostrarAviso("Bot Manue ha robao una ficha.");
                actualizarPantallaJuego();
                ejecutarTurnoBot(); 
            } else {
                verificarCierre();
            }
        }
    }

    private void verificarCierre() {
        int ptsJugador = modelo.getPuntosJugador();
        int ptsBot = modelo.getPuntosBot();
        
        // Si se cierra la partida (nadie puede robar), gana el que menos puntos sume en mano
        if (ptsJugador < ptsBot) {
            vista.mostrarAviso("Fin. ¡Ganaste por tener menos puntos!");
            finalizarPartida(true);
        } else {
            vista.mostrarAviso("Fin. Ha ganao Bot Manue por puntos.");
            finalizarPartida(false);
        }
    }

    // --- NUEVO MÉTODO UNIFICADO ---
    private void finalizarPartida(boolean victoria) {
        // Puntos variables: ganas entre 300-500 si ganas, y te llevas 50-100 de consolación si pierdes
        int puntos;
        if (victoria) {
            puntos = (int) (Math.random() * 200) + 300; 
        } else {
            puntos = (int) (Math.random() * 50) + 50;   
        }
        
        boolean guardadoExitoso = modelo.guardarPuntuacion(puntos);
        
        if (guardadoExitoso) {
            vista.mostrarAviso("¡Partida registrada en el Top 10!");
        } else {
            vista.mostrarAviso("Error: No se ha podido conectar con la Base de Datos.");
        }
        
        // Mostramos la pantalla final pasándole los puntos y si hemos ganado o no
        vista.mostrarPantallaFin(puntos, victoria);
    }

    private void cargarRankingEnVista() {
        vista.lstRanking.removeAll();
        List<String[]> top = modelo.obtenerTop10();
        if (top.isEmpty()) {
            vista.lstRanking.add("¡No hay partidas registradas!");
        } else {
            int pos = 1;
            for (String[] j : top) {
                vista.lstRanking.add(pos + ". " + j[0] + " - " + j[1] + " pts");
                pos++;
            }
        }
    }
}