package vista.interfaz;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import vista.interfaz.ServidorJuegoImpl;

public class ServidorRMI {
    public static void main(String[] args) {
        try {
            // Crear el objeto remoto
            ServidorJuegoImpl servidorJuego = new ServidorJuegoImpl();

            // Crear y obtener el registro RMI
            Registry registry = LocateRegistry.createRegistry(1099);

            // Registrar el objeto remoto en el registro RMI con un nombre
            registry.rebind("ServidorJuego", servidorJuego);

            System.out.println("Servidor RMI está listo...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

