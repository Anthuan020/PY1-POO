package logicacomunicacion;

import java.util.Random;

public class CodigoSecreto {
    
    // Método para enviar el código por correo
    public void EnviarCodigo(String pDestinatario, int pCodigo) {
        String asuntoCorreo = "Codigo de verificacion";
        String cuerpoCorreo = "Su codigo de verificacion es: " + pCodigo;
        
        // Crear la instancia de CuentaCorreo y enviar el correo
        CuentaCorreo cuentaCorreo = new CuentaCorreo(pDestinatario, asuntoCorreo, cuerpoCorreo);
        cuentaCorreo.enviarCorreo();
    }
    
    // Método para generar un código secreto de 5 dígitos
    public int CodigoSecretoCreate() {
        int minimo = 10000; // 5 dígitos mínimo
        int maximo = 99999; // 5 dígitos máximo

        // Crear una instancia de Random
        Random random = new Random();

        // Generar un número aleatorio entre mínimo y máximo (incluye 5 dígitos)
        int codigo = random.nextInt((maximo - minimo) + 1) + minimo;

        // Retornar el código generado
        return codigo;
    }
}
