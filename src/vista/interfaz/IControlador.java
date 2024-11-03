package vista.interfaz;

import modelo.coreJuego.Jugador;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IControlador extends Remote {
    void jugadorPresionaBotonRonda(Jugador jugador) throws RemoteException;
    void actualizarJugadoresEnVista() throws RemoteException;
    void registrarJugador(Jugador jugador) throws RemoteException;

    ArrayList<Jugador> obtenerJugadores() throws RemoteException;
}
