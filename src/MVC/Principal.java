package MVC;

public class Principal {
    public static void main(String[] args) {
        // 1. Creamos la vista y el modelo de forma independiente
        Vista vista = new Vista();
        Modelo modelo = new Modelo();
        
        // 2. Se los pasamos al controlador para que vigile los eventos
        new Controlador(vista, modelo);
        
        // 3. Arrancamos forzando la primera pantalla
        vista.mostrarMenuPrincipal();
        vista.ventana.setVisible(true);
    }
}