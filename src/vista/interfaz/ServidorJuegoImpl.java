package vista.interfaz;

import modelo.coreJuego.EstadoDelJuego;
import modelo.coreJuego.Jugador;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ServidorJuegoImpl extends UnicastRemoteObject implements ServidorJuegoRMI {
    private ArrayList<Jugador> jugadores;
    private EstadoDelJuego estadoDelJuego;

    public ServidorJuegoImpl() throws RemoteException {
        jugadores = new ArrayList<>();
        estadoDelJuego = new EstadoDelJuego(jugadores); // Mantiene el estado del juego
    }

    @Override
    public synchronized void registrarJugador(Jugador jugador) throws RemoteException {
        jugadores.add(jugador);
        System.out.println(jugador.getNombre() + " se ha unido al juego.");
    }

    @Override
    public synchronized void actualizarEstadoDelJuego(Jugador jugador, EstadoDelJuego estado) throws RemoteException {
        // Actualiza el estado general del juego (p.ej., tras una ronda)
        this.estadoDelJuego = estado;
        notificarJugadores();
    }

    @Override
    public synchronized EstadoDelJuego obtenerEstadoDelJuego() throws RemoteException {
        return estadoDelJuego;
    }

    @Override
    public List<Jugador> obtenerJugadores() throws RemoteException {
        return jugadores;
    }

    @Override
    public synchronized void marcarJugadorListo(Jugador jugador) throws RemoteException {
        jugador.setListoParaRonda(true);
        if (verificarTodosListos()) {
            iniciarNuevaRonda();
        }
    }

    @Override
    public boolean verificarTodosListos() throws RemoteException {
        for (Jugador jugador : jugadores) {
            if (!jugador.estaListoParaRonda()) {
                return false;
            }
        }
        return true;
    }

    private void iniciarNuevaRonda() {
        // Lógica para empezar la siguiente ronda
        System.out.println("Todos los jugadores están listos. Empezando nueva ronda...");
    }

    private void notificarJugadores() {
        // Notifica a los jugadores que el estado del juego ha cambiado
    }
}

