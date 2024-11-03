package cliente;

import vista.interfaz.ServidorJuegoRMI;
import modelo.coreJuego.EstadoDelJuego;
import modelo.coreJuego.Jugador;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.util.List;

public class ClienteJuego {
    private ServidorJuegoRMI servidor;

    public ClienteJuego(String ipServidor, int puerto) {
        try {
            Registry registry = LocateRegistry.getRegistry(ipServidor, puerto);
            servidor = (ServidorJuegoRMI) registry.lookup("ServidorJuego");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registrarJugador(Jugador jugador) {
        try {
            servidor.registrarJugador(jugador);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public List<Jugador> obtenerJugadores() {
        try {
            return servidor.obtenerJugadores();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void marcarListo(Jugador jugador) {
        try {
            servidor.marcarJugadorListo(jugador);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void actualizarEstadoDelJuego(Jugador jugador, EstadoDelJuego estado) {
        try {
            servidor.actualizarEstadoDelJuego(jugador, estado);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
