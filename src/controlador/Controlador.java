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

        // suscripcion de botones en la vista
        vista.addListenerBotonRonda(e -> {
            try {
                jugadorPresionaBotonRonda(vista.getJugador());
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        });
    }

    public void jugadorPresionaBotonRonda(Jugador jugador) throws RemoteException {
        jugador.setListoParaRonda(true);

        vista.actualizarEstadoDeRonda(jugador);

        if (juego.todosListosParaNuevaRonda()) {
            juego.subirRonda();

            juego.reiniciarEstadoDeRonda();

            for (Jugador j : jugadores) {
                vista.actualizarLabelRonda();
                tiendaGUI.actualizarTiendaGui();
            }
        }
    }

    public void actualizarJugadoresEnVista() throws RemoteException {
        jugadores.sort((j1, j2) -> Integer.compare(j2.getVida(), j1.getVida()));  // ordenar por vida descendente
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

