package MVC;

import java.awt.*;
import java.awt.event.*;

public class Vista extends WindowAdapter {
    public Frame ventana = new Frame("ER DOMINÓ");

    // ==========================================================
    // COMPONENTES DE LA INTERFAZ (Pantallas y controles)
    // ==========================================================
    
    // Pantalla 1: Menú Principal
    public Panel panelMenu = new Panel(null);
    public Label labelTitulo = new Label("ER DOMINÓ", Label.CENTER);
    public Button btnNuevaPartida = new Button("Nueba Partida");
    public Button btnVerRankingMenu = new Button("Ver Top 10");
    public Button btnAyuda = new Button("Ayuda"); 
    public Canvas fichaDibujoMenu = new CanvasMenu(); 

    // Diálogo emergente para pedir el nombre del jugador
    public Dialog dlgNombre = new Dialog(ventana, "Identificacion", true);
    public TextField txtNombre = new TextField(15);
    public Button btnAceptarNombre = new Button("Aceptar");

    // Pantalla 2: Selección y creación de salas
    public Panel panelSalas = new Panel(null);
    public Button btnCrear = new Button("Creah cala");
    public Panel panelCodigo = new Panel(null);
    public Label lblCodigo = new Label("Codigo Cala: 123456", Label.CENTER);
    public Button btnAceptarSala = new Button("Acettah");

    // Pantalla 3: Mesa principal de juego
    public Panel panelJuego = new Panel(null);
    public Panel bandaGris = new Panel();
    public CanvasTablero areaTablero = new CanvasTablero(); 
    public Panel areaMano = new Panel(new FlowLayout(FlowLayout.LEFT, 5, 5));
    public Button btnPedir = new Button("Pedi Ficha");

    // Pantalla 4: Fin de partida (Victoria o Derrota)
    public Panel panelVictoria = new Panel(null);
    public Label lblPuntosVic = new Label("", Label.CENTER);
    public Label lblGanaste = new Label("", Label.CENTER); 
    public Button btnVerRankingVic = new Button("Ver Top 10");

    // Pantalla 5: Ranking Histórico
    public Panel panelRanking = new Panel(null);
    public Label lblTituloRanking = new Label("Ranking (Top 10)", Label.CENTER);
    public java.awt.List lstRanking = new java.awt.List();
    public Button btnVolverMenu = new Button("Menu Principal");

    // Diálogo genérico para mostrar mensajes de aviso o error
    public Dialog dlgMensaje = new Dialog(ventana, "Aviso", true);
    public Label lblMensaje = new Label("", Label.CENTER);
    public Button btnOkMensaje = new Button("OK");

    // ==========================================================
    // INICIALIZACIÓN DE LA VISTA
    // ==========================================================
    public Vista() {
        ventana.setSize(450, 500);
        ventana.setLocationRelativeTo(null);
        ventana.setResizable(false);
        ventana.addWindowListener(this);

        configurarDialogos();
        configurarPaneles();
    }

    // Ubica, dimensiona y da estilo a todos los elementos en sus respectivos paneles
    private void configurarPaneles() {
        panelMenu.setBackground(Color.WHITE);
        labelTitulo.setFont(new Font("Impact", Font.BOLD, 36));
        labelTitulo.setBounds(50, 40, 350, 50);
        
        btnNuevaPartida.setBounds(80, 110, 280, 45);
        btnVerRankingMenu.setBounds(80, 170, 280, 45);
        btnAyuda.setBounds(80, 230, 280, 45); 
        fichaDibujoMenu.setBounds(145, 300, 160, 90); 
        
        panelMenu.add(labelTitulo); 
        panelMenu.add(btnNuevaPartida); 
        panelMenu.add(btnVerRankingMenu); 
        panelMenu.add(btnAyuda); 
        panelMenu.add(fichaDibujoMenu);

        panelSalas.setBackground(Color.WHITE);
        btnCrear.setBounds(80, 150, 280, 45);
        panelSalas.add(btnCrear);

        panelCodigo.setBackground(Color.WHITE);
        lblCodigo.setFont(new Font("Arial", Font.BOLD, 20));
        lblCodigo.setBounds(60, 140, 320, 50);
        btnAceptarSala.setBounds(60, 210, 320, 45);
        panelCodigo.add(lblCodigo); panelCodigo.add(btnAceptarSala);

        panelJuego.setBackground(Color.WHITE);
        bandaGris.setBackground(Color.GRAY);
        bandaGris.setBounds(40, 30, 370, 30);
        areaTablero.setBackground(Color.WHITE);
        areaTablero.setBounds(40, 70, 370, 220);
        areaMano.setBackground(Color.LIGHT_GRAY);
        areaMano.setBounds(40, 300, 370, 85);
        btnPedir.setBounds(40, 395, 370, 40);
        panelJuego.add(bandaGris); panelJuego.add(areaTablero); 
        panelJuego.add(areaMano); panelJuego.add(btnPedir);

        panelVictoria.setBackground(Color.WHITE);
        lblPuntosVic.setFont(new Font("Arial", Font.BOLD, 18));
        lblPuntosVic.setBounds(50, 60, 350, 30);
        lblGanaste.setFont(new Font("Impact", Font.PLAIN, 32));
        lblGanaste.setBounds(50, 100, 350, 50);
        btnVerRankingVic.setBounds(100, 380, 250, 45);
        panelVictoria.add(lblPuntosVic); panelVictoria.add(lblGanaste); panelVictoria.add(btnVerRankingVic);

        panelRanking.setBackground(Color.WHITE);
        lblTituloRanking.setFont(new Font("Arial", Font.BOLD, 22));
        lblTituloRanking.setBounds(20, 40, 410, 40);
        lstRanking.setBounds(50, 100, 350, 250);
        btnVolverMenu.setBounds(100, 380, 250, 40);
        panelRanking.add(lblTituloRanking); panelRanking.add(lstRanking); panelRanking.add(btnVolverMenu);
    }

    // Prepara las ventanitas emergentes (pop-ups)
    private void configurarDialogos() {
        dlgNombre.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 15)); 
        dlgNombre.setSize(250, 130);
        dlgNombre.add(new Label("Escribe tu nombre (Hurado):")); 
        dlgNombre.add(txtNombre);
        dlgNombre.add(btnAceptarNombre); 
        dlgNombre.addWindowListener(this);

        dlgMensaje.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20)); 
        dlgMensaje.setSize(300, 120);
        dlgMensaje.add(lblMensaje); 
        dlgMensaje.add(btnOkMensaje); 
        dlgMensaje.addWindowListener(this);
    }

    // ==========================================================
    // ENRUTAMIENTO VISUAL (Manejo de pantallas)
    // ==========================================================
    // Vacían la ventana principal y cargan el panel correspondiente
    public void mostrarMenuPrincipal() { ventana.removeAll(); ventana.add(panelMenu); ventana.validate(); ventana.repaint(); }
    public void abrirPantallaSalas() { ventana.removeAll(); ventana.add(panelSalas); ventana.validate(); ventana.repaint(); }
    public void mostrarCodigoSala() { ventana.removeAll(); ventana.add(panelCodigo); ventana.validate(); ventana.repaint(); }
    public void mostrarPantallaJuego() { ventana.removeAll(); ventana.add(panelJuego); ventana.validate(); ventana.repaint(); }
    public void mostrarPantallaRanking() { ventana.removeAll(); ventana.add(panelRanking); ventana.validate(); ventana.repaint(); }
    
    // Configura los textos finales dependiendo de si ganas o pierdes antes de mostrar el panel
    public void mostrarPantallaFin(int puntos, boolean ganaste) { 
        lblPuntosVic.setText("Puntuacion: " + puntos + "p"); 
        if (ganaste) {
            lblGanaste.setText("¡Ganatte!");
        } else {
            lblGanaste.setText("¡Has perdio!");
        }
        ventana.removeAll(); 
        ventana.add(panelVictoria); 
        ventana.validate(); 
        ventana.repaint(); 
    }

    // Lanza el pop-up genérico con un mensaje personalizado
    public void mostrarAviso(String texto) {
        lblMensaje.setText(texto);
        dlgMensaje.setLocationRelativeTo(ventana);
        dlgMensaje.setVisible(true);
    }

    // Controla el comportamiento al pulsar la 'X' de las ventanas
    @Override
    public void windowClosing(WindowEvent e) {
        if (e.getSource() == dlgNombre) dlgNombre.setVisible(false);
        else if (e.getSource() == dlgMensaje) dlgMensaje.setVisible(false);
        else System.exit(0); // Cierra el juego entero si es la ventana principal
    }

    // ==========================================================
    // SISTEMA DE DIBUJO AWT (Fichas de dominó)
    // ==========================================================
    
    // Dibuja los puntitos negros en las coordenadas correctas según el valor (1 al 6)
    public void dibujarPuntos(Graphics g, int x, int y, int valor) {
        g.setColor(Color.BLACK);
        int dp = 4; // Tamaño de cada puntito
        switch (valor) {
            case 1: g.fillOval(x + 8, y + 10, dp, dp); break;
            case 2: g.fillOval(x + 4, y + 5, dp, dp); g.fillOval(x + 12, y + 16, dp, dp); break;
            case 3: g.fillOval(x + 4, y + 5, dp, dp); g.fillOval(x + 8, y + 10, dp, dp); g.fillOval(x + 12, y + 16, dp, dp); break;
            case 4: g.fillOval(x + 4, y + 5, dp, dp); g.fillOval(x + 12, y + 5, dp, dp); g.fillOval(x + 4, y + 16, dp, dp); g.fillOval(x + 12, y + 16, dp, dp); break;
            case 5: g.fillOval(x + 4, y + 5, dp, dp); g.fillOval(x + 12, y + 5, dp, dp); g.fillOval(x + 8, y + 10, dp, dp); g.fillOval(x + 4, y + 16, dp, dp); g.fillOval(x + 12, y + 16, dp, dp); break;
            case 6: g.fillOval(x + 4, y + 4, dp, dp); g.fillOval(x + 12, y + 4, dp, dp); g.fillOval(x + 4, y + 11, dp, dp); g.fillOval(x + 12, y + 11, dp, dp); g.fillOval(x + 4, y + 18, dp, dp); g.fillOval(x + 12, y + 18, dp, dp); break;
        }
    }

    // Lienzo decorativo para el menú principal (muestra siempre un 5 doble fijo)
    public class CanvasMenu extends Canvas {
        @Override public void paint(Graphics g) {
            g.setColor(Color.BLACK);
            g.drawRect(5, 5, 140, 70);
            g.drawLine(75, 5, 75, 75);
            dibujarPuntos(g, 10, 10, 5);
            dibujarPuntos(g, 80, 10, 5);
        }
    }

    // Lienzo de la mesa: recorre todas las fichas jugadas y las dibuja en formato horizontal
    public class CanvasTablero extends Canvas {
        public java.util.List<Modelo.Ficha> fichasEnJuego = new java.util.ArrayList<>();
        @Override public void paint(Graphics g) {
            g.setColor(Color.BLACK);
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            if (fichasEnJuego == null) return;
            int index = 0;
            for (Modelo.Ficha f : fichasEnJuego) {
                // Matemáticas simples para colocarlas en filas y columnas
                int x = 15 + ((index % 7) * 46);
                int y = 15 + ((index / 7) * 35);
                g.setColor(Color.WHITE); g.fillRect(x, y, 40, 25);
                g.setColor(Color.BLACK); g.drawRect(x, y, 40, 25);
                g.drawLine(x + 20, y, x + 20, y + 25);
                dibujarPuntos(g, x, y, f.ladoA());
                dibujarPuntos(g, x + 20, y, f.ladoB());
                index++;
            }
        }
    }

    // Lienzo individual clickeable (sustituto del Button) para representar cada ficha de la mano en vertical
    public class FichaCanvas extends Canvas {
        public final Modelo.Ficha ficha;
        public FichaCanvas(Modelo.Ficha ficha) {
            this.ficha = ficha;
            setPreferredSize(new Dimension(35, 55));
            setBackground(Color.WHITE);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        @Override public void paint(Graphics g) {
            g.setColor(Color.BLACK);
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            g.drawLine(0, getHeight() / 2, getWidth() - 1, getHeight() / 2);
            dibujarPuntos(g, (getWidth() / 2) - 10, (getHeight() / 4) - 12, ficha.ladoA());
            dibujarPuntos(g, (getWidth() / 2) - 10, (3 * getHeight() / 4) - 12, ficha.ladoB());
        }
    }
}