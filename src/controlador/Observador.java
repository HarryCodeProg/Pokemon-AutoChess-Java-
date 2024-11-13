package controlador;

public interface Observador {
    void actualizarRasgos();
    void notificarRonda();
    void actualizarTiempo(int segundos);
    void notificarMensaje(String mensaje);
    void notificarError(String mensaje);

}

