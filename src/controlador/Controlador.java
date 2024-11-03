package controlador;

import modelo.coreJuego.Tienda;
import vista.coreJuegoGUI.TiendaGUI;
import vista.interfaz.IControlador;
import modelo.coreJuego.Juego;
import modelo.coreJuego.Jugador;
import vista.coreJuegoGUI.PokemonAutoChessGUI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Controlador extends UnicastRemoteObject implements IControlador {
    private Juego juego;
    private PokemonAutoChessGUI vista;
    private ArrayList<Jugador> jugadores;
    private Tienda tienda;
    private TiendaGUI tiendaGUI;

    public Controlador(Juego juego, PokemonAutoChessGUI vista, ArrayList<Jugador> jugadores) throws RemoteException {
        this.juego = juego;
        this.vista = vista;
        this.jugadores = jugadores;

        // Suscripción de botones en la vista
        vista.addListenerBotonRonda(e -> {
            try {
                jugadorPresionaBotonRonda(vista.getJugador());
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        });
    }

    // Método que se ejecuta cuando un jugador presiona el botón para avanzar de ronda
    public void jugadorPresionaBotonRonda(Jugador jugador) throws RemoteException {
        jugador.setListoParaRonda(true);  // Marcar que este jugador está listo

        // Actualizar la vista del jugador individual
        vista.actualizarEstadoDeRonda(jugador);

        // Verificar si todos los jugadores están listos para la siguiente ronda
        if (juego.todosListosParaNuevaRonda()) {
            // Iniciar la siguiente ronda
            juego.subirRonda();

            // Resetear el estado de los jugadores para la siguiente ronda
            juego.reiniciarEstadoDeRonda();

            // Actualizar la interfaz gráfica para todos los jugadores
            for (Jugador j : jugadores) {
                vista.actualizarLabelRonda();
                tiendaGUI.actualizarTiendaGui();
            }
        }
    }

    // Actualizar la vista con la información de los jugadores (según vida u otro criterio)
    public void actualizarJugadoresEnVista() throws RemoteException {
        jugadores.sort((j1, j2) -> Integer.compare(j2.getVida(), j1.getVida()));  // Ordenar por vida descendente
        vista.actualizarJugadoresPorVida(jugadores);
    }

    @Override
    public void registrarJugador(Jugador jugador) throws RemoteException {
        jugadores.add(jugador);
        System.out.println("Jugador registrado: " + jugador.getNombre());
    }

    @Override
    public ArrayList<Jugador> obtenerJugadores() throws RemoteException {
        return jugadores;
    }
}

