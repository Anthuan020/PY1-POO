package logicagui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import logicabancaria.Usuario;
import logicaalmacenamiento.UsuarioManager;

public class CambiarNumeroTelefonoCliente extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Crear etiquetas y campos de texto
        Label idLabel = new Label("Identificación del Cliente:");
        TextField idField = new TextField();
        idField.setPromptText("Ingrese identificación");

        Label nombreClienteLabel = new Label("Nombre del Cliente:");
        TextField nombreClienteField = new TextField();
        nombreClienteField.setEditable(false); // Solo se muestra, no se edita

        Label telefonoActualLabel = new Label("Teléfono Actual:");
        TextField telefonoActualField = new TextField();
        telefonoActualField.setEditable(false); // Solo se muestra, no se edita

        Label nuevoTelefonoLabel = new Label("Nuevo Número de Teléfono:");
        TextField nuevoTelefonoField = new TextField();
        nuevoTelefonoField.setPromptText("Ingrese nuevo número de teléfono");

        // Botón para cambiar el número de teléfono
        Button cambiarButton = new Button("Cambiar Número de Teléfono");

        // Área para mostrar el resultado del cambio
        TextArea resultadoArea = new TextArea();
        resultadoArea.setEditable(false);
        resultadoArea.setPrefHeight(100);
        resultadoArea.setPromptText("El resultado del cambio aparecerá aquí...");

        // Acción al hacer clic en el botón cambiar
        cambiarButton.setOnAction(e -> {
            String identificacion = idField.getText();
            String nuevoTelefono = nuevoTelefonoField.getText();
            
            // Validación de identificación y nuevo teléfono
            if (!identificacion.isEmpty()) {
                // Simulación de la consulta de cliente en el sistema
                UsuarioManager usuarioManager = UsuarioManager.getInstancia();
                Usuario usuario = usuarioManager.buscarUsuarioPorId(identificacion);
                
                if (usuario != null) {
                    // Mostrar los datos del cliente en los campos correspondientes
                    nombreClienteField.setText(usuario.getName());
                    telefonoActualField.setText(usuario.getNumberTel());

                    // Validar el nuevo número de teléfono (que tenga 8 dígitos)
                    if (nuevoTelefono.matches("\\d{8}")) {
                        // Cambiar el número de teléfono en el usuario
                        usuario.setNumberTel(nuevoTelefono); // Cambiar el número de teléfono
                        resultadoArea.setText("Estimado usuario: " + usuario.getName() + ", su número de teléfono ha sido cambiado a " + nuevoTelefono);
                    } else {
                        resultadoArea.setText("Error: El nuevo número de teléfono debe tener 8 dígitos.");
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
        grid.add(telefonoActualLabel, 0, 2);
        grid.add(telefonoActualField, 1, 2);
        grid.add(nuevoTelefonoLabel, 0, 3);
        grid.add(nuevoTelefonoField, 1, 3);
        grid.add(cambiarButton, 1, 4);
        grid.add(resultadoArea, 0, 5, 2, 1);

        // Crear la escena y mostrar la ventana
        Scene scene = new Scene(grid, 450, 350);
        primaryStage.setTitle("Cambiar Número de Teléfono");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
