package logicacomunicacion;

public class TestCuentaCorreo {
    public static void main(String[] args) {
        String destinatarioCorreo = "danielabarrientosch16@gmail.com";
        String asuntoCorreo = "Falla en el sinpe";
        String cuerpoCorreo = "Vaya al banco a hacer fila como ganado porque se quedo sin sinpe por sapa";

        CuentaCorreo cuentaCorreo = new CuentaCorreo(destinatarioCorreo, asuntoCorreo, cuerpoCorreo);
        cuentaCorreo.enviarCorreo();
    }
}
