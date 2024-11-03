package vista.coreJuegoGUI;

import modelo.coreJuego.Jugador;
import vista.coreJuegoGUI.PokemonAutoChessGUI;

import java.util.ArrayList;

public class JuegoMultijugador {
    private static ArrayList<Jugador> jugadores = new ArrayList<Jugador>();

    public static void llenarListaJugadores(Jugador jugador){
        jugadores.add(jugador);
    }

    public static void main(String[] args) {
        Jugador jugador1 = new Jugador("Jugador 1");
        //Jugador jugador2 = new Jugador("Jugador 2");
        llenarListaJugadores(jugador1);
        //llenarListaJugadores(jugador2);

        PokemonAutoChessGUI ventana1 = new PokemonAutoChessGUI(jugador1,jugadores);
        //PokemonAutoChessGUI ventana2 = new PokemonAutoChessGUI(jugador2,jugadores);


        ventana1.setVisible(true);
        //ventana2.setVisible(true);
    }
}
