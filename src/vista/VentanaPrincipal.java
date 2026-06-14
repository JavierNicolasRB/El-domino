package vista;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import controlador.BotInteligencia;
import modelo.Ficha;
import modelo.Tablero;

public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("ER DOMINÓ");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        mostrarMenuPrincipal();
    }

    private void mostrarMenuPrincipal() {
        JPanel panelMenu = new JPanel();
        panelMenu.setBackground(Color.WHITE);
        panelMenu.setLayout(null);
        
        JLabel labelTitulo = new JLabel("ER DOMINÓ", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Impact", Font.BOLD, 36));
        labelTitulo.setBounds(50, 20, 350, 50);
        panelMenu.add(labelTitulo);
        
        // Botón 1: Nueva Partida
        JButton btnNuevaPartida = crearBotonEstilizado("Nueba Partida");
        btnNuevaPartida.setBounds(80, 90, 280, 45);
        panelMenu.add(btnNuevaPartida);

        // NUEVO BOTÓN: Ver Top 10 (Añadido directamente en el menú principal)
        JButton btnVerRankingMenu = crearBotonEstilizado("Ver Top 10");
        btnVerRankingMenu.setBounds(80, 150, 280, 45);
        panelMenu.add(btnVerRankingMenu);
        
        // Botón 3: Ayuda e instrucciones
        JButton btnOpciones = crearBotonEstilizado("Ayuda / Inftruçiónê");
        btnOpciones.setBounds(80, 210, 280, 45);
        panelMenu.add(btnOpciones);
        
        // El dibujo animado de la ficha de dominó en el menú
        JPanel fichaDibujo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(3));
                g2.drawRect(5, 5, 140, 70);
                g2.drawLine(75, 5, 75, 75);
                dibujarPuntos(g2, 10, 10, 5);
                dibujarPuntos(g2, 80, 10, 5);
            }
        };
        fichaDibujo.setBackground(Color.WHITE);
        fichaDibujo.setBounds(145, 280, 160, 90);
        panelMenu.add(fichaDibujo);

        // Acciones de los botones del menú
        btnNuevaPartida.addActionListener(e -> {
            String nombre = JOptionPane.showInputDialog(this, "Escribe tu nombre (Nombre huradô):", "Identificación", JOptionPane.PLAIN_MESSAGE);
            if (nombre != null && !nombre.trim().isEmpty()) {
                abrirPantallaSalas(nombre);
            }
        });

        btnVerRankingMenu.addActionListener(e -> mostrarPantallaRanking());

        btnOpciones.addActionListener(e -> mostrarPantallaAyuda());

        setContentPane(panelMenu);
        revalidate();
        repaint();
    }

    private void mostrarPantallaAyuda() {
        this.getContentPane().removeAll();
        JPanel panelAyuda = new JPanel(null);
        panelAyuda.setBackground(Color.WHITE);
        panelAyuda.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));

        JLabel lblTitulo = new JLabel("Manual de Inftruçiónê", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial Black", Font.BOLD, 20));
        lblTitulo.setBounds(20, 20, 410, 40);
        lblTitulo.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLACK));
        panelAyuda.add(lblTitulo);

        JTextArea txtAyuda = new JTextArea();
        txtAyuda.setText(
            "1. El ohetibo é quedartte çin fichâ antttê que 'Er Bot Manué'.\n\n" +
            "2. En tu turno, cansa una ficha de tu mano que coincida con " +
            "uno de lo doh extremo' libre' del tablero.\n\n" +
            "3. Si la ficha deha de encahâh, el huego la boltteará çola.\n\n" +
            "4. Si no tieni' movimiento poçible, tieni' que pinchâh en " +
            "'Pedî Ficha' para chupâh del poço.\n\n" +
            "5. Si el poço se queda vaçío y ni tú ni el Bot podéi' hugar, " +
            "ganará quien tarría menô' puntuación total en la mano."
        );
        txtAyuda.setFont(new Font("Arial", Font.PLAIN, 13));
        txtAyuda.setLineWrap(true);
        txtAyuda.setWrapStyleWord(true);
        txtAyuda.setEditable(false);
        txtAyuda.setBackground(Color.WHITE);

        JScrollPane scrollTxt = new JScrollPane(txtAyuda);
        scrollTxt.setBounds(30, 80, 390, 300);
        scrollTxt.setBorder(null);
        panelAyuda.add(scrollTxt);

        JButton btnVolver = crearBotonEstilizado("Menú Principal");
        btnVolver.setBounds(100, 400, 250, 40);
        btnVolver.addActionListener(e -> mostrarMenuPrincipal());
        panelAyuda.add(btnVolver);

        setContentPane(panelAyuda);
        revalidate();
        repaint();
    }

    private JButton crearBotonEstilizado(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial Black", Font.BOLD, 18));
        boton.setBackground(Color.WHITE);
        boton.setForeground(Color.BLACK);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        
        boton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                boton.setBackground(new Color(220, 220, 220));
            }
            public void mouseExited(MouseEvent evt) {
                boton.setBackground(Color.WHITE);
            }
        });
        return boton;
    }
    
    private void abrirPantallaSalas(String nombreJugador) {
        this.getContentPane().removeAll();
        JPanel panelSalas = new JPanel(null);
        panelSalas.setBackground(Color.WHITE);
        
        JButton btnCrear = crearBotonEstilizado("Creâh çala");
        btnCrear.setBounds(80, 150, 280, 45);
        btnCrear.addActionListener(e -> mostrarCodigoSala(nombreJugador));
        
        panelSalas.add(btnCrear);
        setContentPane(panelSalas);
        revalidate();
        repaint();
    }

    private void mostrarCodigoSala(String nombreJugador) {
        this.getContentPane().removeAll();
        JPanel panelCodigo = new JPanel(null);
        panelCodigo.setBackground(Color.WHITE);
        
        JLabel lblCodigo = new JLabel("Código Çala: 123456", SwingConstants.CENTER);
        lblCodigo.setFont(new Font("Arial Black", Font.BOLD, 20));
        lblCodigo.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        lblCodigo.setBounds(60, 140, 320, 50);
        
        JButton btnAceptar = new JButton("Açêttâh");
        btnAceptar.setFont(new Font("Arial Black", Font.BOLD, 18));
        btnAceptar.setBackground(new Color(200, 200, 200)); 
        btnAceptar.setForeground(Color.BLACK);
        btnAceptar.setFocusPainted(false);
        btnAceptar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        btnAceptar.setBounds(60, 210, 320, 45);
        
        btnAceptar.addActionListener(e -> {
            try {
                iniciarPartidaContraBot(nombreJugador);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error crítico al iniciar la partida: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        panelCodigo.add(lblCodigo);
        panelCodigo.add(btnAceptar);
        setContentPane(panelCodigo);
        revalidate();
        repaint();
    }

    private void iniciarPartidaContraBot(String nombreJugador) {
        this.getContentPane().removeAll();
        Tablero tablero = new Tablero();
        BotInteligencia bot = new BotInteligencia();
        
        List<Ficha> todasLasFichas = new ArrayList<>();
        for (int i = 0; i <= 6; i++) {
            for (int j = i; j <= 6; j++) {
                todasLasFichas.add(new Ficha(i, j));
            }
        }
        
        Collections.shuffle(todasLasFichas);
        
        List<Ficha> manoJugador = new ArrayList<>();
        List<Ficha> manoBot = new ArrayList<>();
        for (int k = 0; k < 7; k++) {
            manoJugador.add(todasLasFichas.remove(0));
            manoBot.add(todasLasFichas.remove(0));
        }
        
        List<Ficha> pozo = todasLasFichas;

        JPanel panelJuego = new JPanel(null);
        panelJuego.setBackground(Color.WHITE);
        panelJuego.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));

        JPanel bandaGris = new JPanel();
        bandaGris.setBackground(Color.GRAY);
        bandaGris.setBounds(40, 10, 370, 30);
        panelJuego.add(bandaGris);

        JPanel areaTablero = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(3));
                g2.setColor(Color.BLACK);
                g2.drawRoundRect(10, 10, 350, 220, 40, 40);
                
                int margenIzquierdo = 25;
                int margenSuperior = 25;
                int espacioHorizontal = 46; 
                int espacioVertical = 35;   
                
                int index = 0;
                for (Ficha f : tablero.getFichasEnJuego()) {
                    int columna = index % 7;
                    int fila = index / 7;
                    int currentX = margenIzquierdo + (columna * espacioHorizontal);
                    int currentY = margenSuperior + (fila * espacioVertical);
                    
                    g2.setColor(Color.WHITE);
                    g2.fillRect(currentX, currentY, 40, 25);
                    g2.setColor(Color.BLACK);
                    g2.drawRect(currentX, currentY, 40, 25);
                    g2.drawLine(currentX + 20, currentY, currentX + 20, currentY + 25);
                    
                    dibujarPuntos(g2, currentX, currentY, f.ladoA());
                    dibujarPuntos(g2, currentX + 20, currentY, f.ladoB());
                    index++;
                }
            }
        };
        areaTablero.setBackground(Color.WHITE);
        areaTablero.setBounds(40, 50, 370, 240);
        panelJuego.add(areaTablero);

        JPanel areaMano = new JPanel();
        areaMano.setLayout(new BoxLayout(areaMano, BoxLayout.X_AXIS));
        areaMano.setBackground(Color.WHITE);
        areaMano.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JScrollPane scrollMano = new JScrollPane(areaMano);
        scrollMano.setBounds(40, 300, 370, 85);
        scrollMano.setBackground(Color.WHITE);
        scrollMano.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        scrollMano.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollMano.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        scrollMano.addMouseWheelListener(e -> {
            JScrollBar horizontalBar = scrollMano.getHorizontalScrollBar();
            int muescas = e.getWheelRotation();
            horizontalBar.setValue(horizontalBar.getValue() + (muescas * 20));
        });
        panelJuego.add(scrollMano);

        JButton btnPedir = new JButton("Pedî Ficha (" + pozo.size() + " en poço)");
        btnPedir.setFont(new Font("Arial Black", Font.BOLD, 12));
        btnPedir.setBackground(Color.WHITE);
        btnPedir.setForeground(new Color(0, 128, 255)); 
        btnPedir.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        btnPedir.setBounds(40, 395, 370, 40);
        
        btnPedir.addActionListener(e -> {
            if (!pozo.isEmpty()) {
                Ficha nuevaFicha = pozo.remove(0);
                manoJugador.add(nuevaFicha);
                areaMano.add(Box.createHorizontalStrut(6));
                JButton btnNueva = crearBotonFicha(nuevaFicha, manoJugador, manoBot, tablero, areaMano, areaTablero, nombreJugador, bot, pozo, btnPedir);
                areaMano.add(btnNueva);
                
                btnPedir.setText("Pedî Ficha (" + pozo.size() + " en poço)");
                areaMano.revalidate();
                areaMano.repaint();
                
                SwingUtilities.invokeLater(() -> {
                    JScrollBar horizontal = scrollMano.getHorizontalScrollBar();
                    horizontal.setValue(horizontal.getMaximum());
                });
                JOptionPane.showMessageDialog(this, "Has robao una ficha del pozo.", "Pozo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                verificarCierrePartida(manoJugador, manoBot, tablero, nombreJugador);
            }
        });
        panelJuego.add(btnPedir);

        for (int i = 0; i < manoJugador.size(); i++) {
            if (i > 0) areaMano.add(Box.createHorizontalStrut(6));
            Ficha f = manoJugador.get(i);
            JButton btnFicha = crearBotonFicha(f, manoJugador, manoBot, tablero, areaMano, areaTablero, nombreJugador, bot, pozo, btnPedir);
            areaMano.add(btnFicha);
        }
        
        setContentPane(panelJuego);
        revalidate();
        repaint();
    }

    private JButton crearBotonFicha(Ficha f, List<Ficha> manoJugador, List<Ficha> manoBot, Tablero tablero, 
                                    JPanel areaMano, JPanel areaTablero, String nombreJugador, BotInteligencia bot, 
                                    List<Ficha> pozo, JButton btnPedir) {
        JButton btnFicha = new JButton();
        btnFicha.setIcon(new IconDominoConPuntos(f)); 
        btnFicha.setBackground(Color.WHITE);
        btnFicha.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        btnFicha.setPreferredSize(new Dimension(45, 55));
        btnFicha.setMaximumSize(new Dimension(45, 55)); 
        
        btnFicha.addActionListener(e -> {
            boolean jugadaValida = false;
            if (tablero.estaVacio() || f.ladoA() == tablero.getExtremoDerecho() || f.ladoB() == tablero.getExtremoDerecho()) {
                tablero.jugarPorDerecha(f);
                jugadaValida = true;
            } else if (f.ladoA() == tablero.getExtremoIzquierdo() || f.ladoB() == tablero.getExtremoIzquierdo()) { 
                tablero.jugarPorIzquierda(f);
                jugadaValida = true;
            }

            if (jugadaValida) {
                int index = -1;
                Component[] componentes = areaMano.getComponents();
                for (int i = 0; i < componentes.length; i++) {
                    if (componentes[i] == btnFicha) {
                        index = i;
                        break;
                    }
                }
                
                manoJugador.remove(f);
                areaMano.remove(btnFicha);
                if (index > 0 && componentes[index - 1] instanceof Box.Filler) {
                    areaMano.remove(componentes[index - 1]);
                }
                
                areaTablero.repaint();
                areaMano.revalidate();
                areaMano.repaint();
                
                if (manoJugador.isEmpty()) {
                    // Si te quedas sin fichas ganas! Calculamos un bonus aleatorio basado en tu éxito
                    int bonusPuntos = (int) (Math.random() * 200) + 300;
                    mostrarPantallaVictoria(nombreJugador, bonusPuntos); 
                    return;
                }
                
                ejecutarTurnoBot(bot, manoBot, tablero, areaTablero, nombreJugador, manoJugador, pozo, btnPedir);
            } else {
                JOptionPane.showMessageDialog(this, "¡Eça ficha no encaha aquí!", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });
        return btnFicha;
    }

    private void ejecutarTurnoBot(BotInteligencia bot, List<Ficha> manoBot, Tablero tablero, JPanel areaTablero, 
                                  String nombreJugador, List<Ficha> manoJugador, List<Ficha> pozo, JButton btnPedir) {
        
        var decision = bot.decidirJugada(manoBot, tablero);
        if (decision.isPresent()) {
            BotInteligencia.Jugada j = decision.get();
            if (j.esIzquierda()) {
                tablero.jugarPorIzquierda(j.ficha());
            } else {
                tablero.jugarPorDerecha(j.ficha());
            }
            manoBot.remove(j.ficha());
            areaTablero.repaint();
            
            if (manoBot.isEmpty()) {
                JOptionPane.showMessageDialog(this, "¡Ha ganao 'Er Bot Manué'!", "Fin de la partida", JOptionPane.INFORMATION_MESSAGE);
                mostrarMenuPrincipal(); 
            }
        } else {
            if (!pozo.isEmpty()) {
                Ficha fRobada = pozo.remove(0);
                manoBot.add(fRobada);
                btnPedir.setText("Pedî Ficha (" + pozo.size() + " en poço)");
                JOptionPane.showMessageDialog(this, "'Er Bot Manué' no tenía fichas y ha robao una.", "Turno del Bot", JOptionPane.INFORMATION_MESSAGE);
                ejecutarTurnoBot(bot, manoBot, tablero, areaTablero, nombreJugador, manoJugador, pozo, btnPedir);
            } else {
                verificarCierrePartida(manoJugador, manoBot, tablero, nombreJugador);
            }
        }
    }

    private void verificarCierrePartida(List<Ficha> manoJugador, List<Ficha> manoBot, Tablero tablero, String nombreJugador) {
        int puntosJugador = manoJugador.stream().mapToInt(f -> f.ladoA() + f.ladoB()).sum();
        int puntosBot = manoBot.stream().mapToInt(f -> f.ladoA() + f.ladoB()).sum();

        String mensajeCierre = "Fin de Partida (Pozo vacío).\n" +
                               "Tus puntos: " + puntosJugador + "\n" +
                               "Puntos de Er Bot Manué: " + puntosBot;

        if (puntosJugador < puntosBot) {
            JOptionPane.showMessageDialog(this, mensajeCierre + "\n\n¡Ganaste por tener menos puntos!", "Fin de la Partida", JOptionPane.INFORMATION_MESSAGE);
            mostrarPantallaVictoria(nombreJugador, 250);
        } else {
            JOptionPane.showMessageDialog(this, mensajeCierre + "\n\nHa ganao 'Er Bot Manué' por tener menos puntos.", "Fin de la Partida", JOptionPane.INFORMATION_MESSAGE);
            mostrarMenuPrincipal();
        }
    }

    private void mostrarPantallaVictoria(String nombreJugador, int puntuacionObtenida) {
        // CONEXIÓN DIRECTA CON TU BASE DE DATOS 'PrJuego'
        try {
            datos.ConexionBD.guardarPuntuacion(nombreJugador, puntuacionObtenida);
        } catch (Exception ex) {
            System.err.println("Error al guardar en la base de datos. " + ex.getMessage());
        }

        this.getContentPane().removeAll();
        JPanel panelVictoria = new JPanel(null);
        panelVictoria.setBackground(Color.WHITE);
        panelVictoria.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));

        JLabel lblPuntos = new JLabel("Puntuación: " + puntuacionObtenida + "p", SwingConstants.CENTER);
        lblPuntos.setFont(new Font("Arial Black", Font.BOLD, 18));
        lblPuntos.setBounds(50, 40, 350, 30);
        panelVictoria.add(lblPuntos);

        JLabel lblGanaste = new JLabel("¡Ganâtte!", SwingConstants.CENTER);
        lblGanaste.setFont(new Font("Impact", Font.PLAIN, 32));
        lblGanaste.setBounds(50, 80, 350, 50);
        panelVictoria.add(lblGanaste);

        JButton btnVerRanking = crearBotonEstilizado("Ver Top 10");
        btnVerRanking.setBounds(100, 380, 250, 45);
        btnVerRanking.addActionListener(e -> mostrarPantallaRanking());
        panelVictoria.add(btnVerRanking);

        setContentPane(panelVictoria);
        revalidate();
        repaint();
    }

    private void mostrarPantallaRanking() {
        this.getContentPane().removeAll();
        JPanel panelRanking = new JPanel(null);
        panelRanking.setBackground(Color.WHITE);
        panelRanking.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));

        JLabel lblTitulo = new JLabel("Ranking (Top 10)", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial Black", Font.BOLD, 22));
        lblTitulo.setBounds(20, 20, 410, 40);
        lblTitulo.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLACK));
        panelRanking.add(lblTitulo);

        // EXTRACCIÓN DINÁMICA DE LA BASE DE DATOS MYSQL 'PrJuego'
        try {
            List<String[]> listaTop10 = datos.ConexionBD.obtenerTop10(); // Llama a obtenerTop10() de tu clase
            if (listaTop10 == null) {
                // Pequeño fallback por compatibilidad de nombres de métodos
                listaTop10 = datos.ConexionBD.obtenerTop10();
            }
            
            int yOffset = 80;
            int posicion = 1;

            if (listaTop10 == null || listaTop10.isEmpty()) {
                JLabel lblVacio = new JLabel("¡No hay partías registrás todavía!", SwingConstants.CENTER);
                lblVacio.setFont(new Font("Arial", Font.ITALIC, 14));
                lblVacio.setBounds(20, 150, 410, 30);
                panelRanking.add(lblVacio);
            } else {
                for (String[] jugador : listaTop10) {
                    JLabel lblJugador = new JLabel(posicion + ". " + jugador[0]);
                    lblJugador.setFont(new Font("Arial Black", Font.PLAIN, 14));
                    lblJugador.setBounds(50, yOffset, 200, 25);
                    
                    JLabel lblPuntos = new JLabel(jugador[1] + " pts", SwingConstants.RIGHT);
                    lblPuntos.setFont(new Font("Arial Black", Font.PLAIN, 14));
                    lblPuntos.setBounds(280, yOffset, 100, 25);

                    panelRanking.add(lblJugador);
                    panelRanking.add(lblPuntos);
                    yOffset += 28;
                    posicion++;
                }
            }
        } catch (Exception ex) {
            JLabel lblFallback = new JLabel("Error al cargâh la baçe de datô", SwingConstants.CENTER);
            lblFallback.setFont(new Font("Arial", Font.ITALIC, 14));
            lblFallback.setBounds(20, 150, 410, 30);
            panelRanking.add(lblFallback);
        }

        JButton btnVolver = crearBotonEstilizado("Menú Principal");
        btnVolver.setBounds(100, 410, 250, 40);
        btnVolver.setFont(new Font("Arial Black", Font.BOLD, 14));
        btnVolver.addActionListener(e -> mostrarMenuPrincipal());
        panelRanking.add(btnVolver);

        setContentPane(panelRanking);
        revalidate();
        repaint();
    }

    private void dibujarPuntos(Graphics2D g2, int x, int y, int valor) {
        g2.setColor(Color.BLACK);
        int diametroPip = 4; 
        
        switch (valor) {
            case 1:
                g2.fillOval(x + 10 - 2, y + 12 - 2, diametroPip, diametroPip); 
                break;
            case 2:
                g2.fillOval(x + 4, y + 5, diametroPip, diametroPip); 
                g2.fillOval(x + 12, y + 16, diametroPip, diametroPip); 
                break;
            case 3:
                g2.fillOval(x + 4, y + 5, diametroPip, diametroPip); 
                g2.fillOval(x + 10 - 2, y + 12 - 2, diametroPip, diametroPip); 
                g2.fillOval(x + 12, y + 16, diametroPip, diametroPip); 
                break;
            case 4:
                g2.fillOval(x + 4, y + 5, diametroPip, diametroPip); 
                g2.fillOval(x + 12, y + 5, diametroPip, diametroPip); 
                g2.fillOval(x + 4, y + 16, diametroPip, diametroPip); 
                g2.fillOval(x + 12, y + 16, diametroPip, diametroPip); 
                break;
            case 5:
                g2.fillOval(x + 4, y + 5, diametroPip, diametroPip); 
                g2.fillOval(x + 12, y + 5, diametroPip, diametroPip); 
                g2.fillOval(x + 10 - 2, y + 12 - 2, diametroPip, diametroPip); 
                g2.fillOval(x + 4, y + 16, diametroPip, diametroPip); 
                g2.fillOval(x + 12, y + 16, diametroPip, diametroPip); 
                break;
            case 6:
                g2.fillOval(x + 4, y + 4, diametroPip, diametroPip); 
                g2.fillOval(x + 12, y + 4, diametroPip, diametroPip); 
                g2.fillOval(x + 4, y + 11, diametroPip, diametroPip); 
                g2.fillOval(x + 12, y + 11, diametroPip, diametroPip); 
                g2.fillOval(x + 4, y + 18, diametroPip, diametroPip); 
                g2.fillOval(x + 12, y + 18, diametroPip, diametroPip); 
                break;
            default:
                break;
        }
    }

    private class IconDominoConPuntos implements Icon {
        private final Ficha ficha;
        private final int anchoIcono = 45; 
        private final int altoIcono = 55;

        public IconDominoConPuntos(Ficha ficha) {
            this.ficha = ficha;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);
            g2.drawRect(x, y, anchoIcono - 1, altoIcono - 1);
            g2.drawLine(x, y + altoIcono / 2, x + anchoIcono - 1, y + altoIcono / 2);

            dibujarPuntos(g2, x + (anchoIcono / 2) - 10, y + (altoIcono / 4) - 12, ficha.ladoA());
            dibujarPuntos(g2, x + (anchoIcono / 2) - 10, y + (3 * altoIcono / 4) - 12, ficha.ladoB());
        }

        @Override
        public int getIconWidth() { return anchoIcono; }
        @Override
        public int getIconHeight() { return altoIcono; }
    }

    // ==========================================
    // MÉTODO MAIN DE ARRANQUE PARA ECLIPSE
    // ==========================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }
}