package logicagui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.regex.Pattern;
import logicabancaria.Usuario;
import logicaalmacenamiento.UsuarioManager;

public class CambiarCorreoElectronicoCliente extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Crear etiquetas y campos de texto
        Label idLabel = new Label("Identificación del Cliente:");
        TextField idField = new TextField();
        idField.setPromptText("Ingrese identificación");

        Label nombreClienteLabel = new Label("Nombre del Cliente:");
        TextField nombreClienteField = new TextField();
        nombreClienteField.setEditable(false); // Solo se muestra, no se edita

        Label correoActualLabel = new Label("Correo Electrónico Actual:");
        TextField correoActualField = new TextField();
        correoActualField.setEditable(false); // Solo se muestra, no se edita

        Label nuevoCorreoLabel = new Label("Nuevo Correo Electrónico:");
        TextField nuevoCorreoField = new TextField();
        nuevoCorreoField.setPromptText("Ingrese nuevo correo electrónico");

        // Botón para cambiar el correo electrónico
        Button cambiarButton = new Button("Cambiar Correo Electrónico");

        // Área para mostrar el resultado del cambio
        TextArea resultadoArea = new TextArea();
        resultadoArea.setEditable(false);
        resultadoArea.setPrefHeight(100);
        resultadoArea.setPromptText("El resultado del cambio aparecerá aquí...");

        // Acción al hacer clic en el botón cambiar
        cambiarButton.setOnAction(e -> {
            String identificacion = idField.getText();
            String nuevoCorreo = nuevoCorreoField.getText();
            
            // Validación de identificación y nuevo correo electrónico
            if (!identificacion.isEmpty()) {
                // Consultar el usuario en el sistema
                UsuarioManager usuarioManager = UsuarioManager.getInstancia();
                Usuario usuario = usuarioManager.buscarUsuarioPorId(identificacion);
                
                if (usuario != null) {
                    // Mostrar los datos del cliente en los campos correspondientes
                    nombreClienteField.setText(usuario.getName());
                    correoActualField.setText(usuario.getMail());

                    // Validar el nuevo correo electrónico (formato básico de email)
                    if (validarCorreo(nuevoCorreo)) {
                        // Cambiar el correo electrónico en el usuario
                        usuario.setMail(nuevoCorreo); // Cambiar el correo electrónico
                        resultadoArea.setText("Estimado usuario: " + usuario.getName() + 
                            ", usted ha cambiado la dirección de correo " + usuario.getMail() + 
                            " por " + nuevoCorreo);
                    } else {
                        resultadoArea.setText("Error: El formato del nuevo correo electrónico no es válido.");
                    }
                } else {
                    resultadoArea.setText("Error: Usuario no encontrado con esa identificación.");
                }
            } else {
                resultadoArea.setText("Por favor, ingrese una identificación válida.");
            }
        });

        // Configurar el diseño
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(10);
        grid.setHgap(10);

        // Agregar componentes al diseño
        grid.add(idLabel, 0, 0);
        grid.add(idField, 1, 0);
        grid.add(nombreClienteLabel, 0, 1);
        grid.add(nombreClienteField, 1, 1);
        grid.add(correoActualLabel, 0, 2);
        grid.add(correoActualField, 1, 2);
        grid.add(nuevoCorreoLabel, 0, 3);
        grid.add(nuevoCorreoField, 1, 3);
        grid.add(cambiarButton, 1, 4);
        grid.add(resultadoArea, 0, 5, 2, 1);

        // Crear la escena y mostrar la ventana
        Scene scene = new Scene(grid, 450, 350);
        primaryStage.setTitle("Cambiar Correo Electrónico");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Método para validar el formato del correo electrónico
    private boolean validarCorreo(String correo) {
        // Expresión regular para validar correos electrónicos
        String correoRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(correoRegex);
        return pattern.matcher(correo).matches();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
