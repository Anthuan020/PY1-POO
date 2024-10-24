package logicacomunicacion;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * Clase para enviar correos electrónicos.
 * 
 * @author Nabyell Leon Valerin
 * @version 1.0
 */
public class CuentaCorreo {

    private static final String USUARIO_CORREO = "bcroficinas@gmail.com";
    private static final String CLAVE_CORREO = "inku kvpq tnby wkpr";
    private String destinatarioCorreo;
    private String asuntoCorreo;
    private String cuerpoCorreo;

    private Properties propiedades;
    private Session sesion;

    public CuentaCorreo(String destinatario, String asunto, String cuerpo) {
        this.destinatarioCorreo = destinatario;
        this.asuntoCorreo = asunto;
        this.cuerpoCorreo = cuerpo;

        propiedades = new Properties();
        propiedades.put("mail.smtp.host", "smtp.gmail.com");
        propiedades.put("mail.smtp.port", "587");
        propiedades.put("mail.smtp.auth", "true");
        propiedades.put("mail.smtp.starttls.enable", "true");

        sesion = Session.getInstance(propiedades, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USUARIO_CORREO, CLAVE_CORREO);
            }
        });
    }

    public void enviarCorreo() {
        try {
            Message mensaje = new MimeMessage(sesion);
            mensaje.setFrom(new InternetAddress(USUARIO_CORREO));
            mensaje.setRecipient(Message.RecipientType.TO, new InternetAddress(destinatarioCorreo));
            mensaje.setSubject(asuntoCorreo);
            mensaje.setText(cuerpoCorreo);

            Transport.send(mensaje);
            System.out.println("Correo enviado con éxito.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
