package vista.interfaz;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import modelo.coreJuego.EstadoDelJuego;
import modelo.coreJuego.Jugador;

public interface ServidorJuegoRMI extends Remote {

    void registrarJugador(Jugador jugador) throws RemoteException;
    void actualizarEstadoDelJuego(Jugador jugador, EstadoDelJuego estado) throws RemoteException;
    EstadoDelJuego obtenerEstadoDelJuego() throws RemoteException;
    List<Jugador> obtenerJugadores() throws RemoteException;
    void marcarJugadorListo(Jugador jugador) throws RemoteException;
    boolean verificarTodosListos() throws RemoteException;
}

