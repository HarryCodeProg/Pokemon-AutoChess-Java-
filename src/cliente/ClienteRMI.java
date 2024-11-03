package cliente;

import vista.interfaz.IControlador;
import modelo.coreJuego.Jugador;
import vista.coreJuegoGUI.PokemonAutoChessGUI;

import javax.swing.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClienteRMI {
    public static void main(String[] args) {
        try {
            // Conexión al registro RMI en el servidor (localhost para pruebas)
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            IControlador controlador = (IControlador) registry.lookup("Controlador");

            // Crear un nombre para el jugador y la instancia del objeto Jugador
            String nombreJugador = JOptionPane.showInputDialog("Ingresa tu nombre de jugador:");
            Jugador jugador = new Jugador(nombreJugador);

            // Registrar al jugador en el servidor usando el controlador
            controlador.registrarJugador(jugador);

            // Iniciar la GUI del juego con el controlador y el jugador
            SwingUtilities.invokeLater(() -> {
                PokemonAutoChessGUI guiJugador = null;
                try {
                    guiJugador = new PokemonAutoChessGUI(jugador, controlador.obtenerJugadores());
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                guiJugador.setVisible(true);
            });

            // Aquí puedes agregar más lógica para recibir actualizaciones del controlador
            // y comunicar los eventos del jugador al controlador
            // Ejemplo: sincronizar rondas, actualizar estado, etc.

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
