package controlador;

public interface Observador {
    void actualizarRasgos();
    void notificar();
    void notificarMensaje(String mensaje);
    void notificarError(String mensaje);
}

