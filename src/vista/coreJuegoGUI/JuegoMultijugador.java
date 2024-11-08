package vista.coreJuegoGUI;

import modelo.coreJuego.Juego;
import modelo.coreJuego.Jugador;
import modelo.coreJuego.Tienda;
import vista.coreJuegoGUI.PokemonAutoChessGUI;

import java.util.ArrayList;

public class JuegoMultijugador {
    private static ArrayList<Jugador> jugadores = new ArrayList<>();

    public static void llenarListaJugadores(Jugador jugador){
        jugadores.add(jugador);
    }

    public static void main(String[] args) {
        Tienda tienda = new Tienda();
        Jugador jugador1 = new Jugador("Jugador 1");
        Jugador jugador2 = new Jugador("Jugador 2");
        llenarListaJugadores(jugador1);
        llenarListaJugadores(jugador2);

        Juego juego = new Juego(tienda);
        juego.setJugadores(jugadores);

        PokemonAutoChessGUI ventana1 = new PokemonAutoChessGUI(jugador1,jugadores,juego,tienda);
        PokemonAutoChessGUI ventana2 = new PokemonAutoChessGUI(jugador2,jugadores,juego,tienda);

        ventana1.setVisible(true);
        ventana2.setVisible(true);
    }
}
