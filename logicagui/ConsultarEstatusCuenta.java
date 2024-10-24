package logicagui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import logicabancaria.*;
import logicaalmacenamiento.UsuarioManager;

public class ConsultarEstatusCuenta extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Crear etiquetas y campos de texto
        Label numeroCuentaLabel = new Label("Número de Cuenta:");
        TextField numeroCuentaField = new TextField();
        numeroCuentaField.setPromptText("Ingrese el número de cuenta");

        // Botón para consultar estatus
        Button consultarButton = new Button("Consultar Estatus");

        // Área para mostrar los resultados
        TextArea resultadosArea = new TextArea();
        resultadosArea.setEditable(false);
        resultadosArea.setPrefHeight(100);
        resultadosArea.setPromptText("Los resultados aparecerán aquí...");

        // Acción al hacer clic en el botón consultar
        consultarButton.setOnAction(e -> {
            String numeroCuentaStr = numeroCuentaField.getText();
            int numeroCuenta;

            try {
                numeroCuenta = Integer.parseInt(numeroCuentaStr);
            } catch (NumberFormatException ex) {
                resultadosArea.setText("Número de cuenta inválido. Debe ser un número entero.");
                return;
            }

            // Buscar la cuenta por ID en todos los usuarios
            Cuenta cuenta = null;
            for (Usuario usuario : UsuarioManager.getInstancia().getUsuarios()) {
                for (Cuenta c : usuario.getAccounts()) {
                    if (c.getId() == numeroCuenta) {
                        cuenta = c;
                        break;
                    }
                }
                if (cuenta != null) break;
            }

            if (cuenta != null) {
                String estatus = cuenta.getStatus() ? "activa" : "inactiva";
                resultadosArea.setText("La cuenta número " + numeroCuenta + " está " + estatus + ".");
            } else {
                resultadosArea.setText("Número de cuenta no registrado.");
            }
        });

        // Configurar el diseño
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(numeroCuentaLabel, numeroCuentaField, consultarButton, resultadosArea);

        // Crear la escena y mostrar la ventana
        Scene scene = new Scene(vbox, 400, 200);
        primaryStage.setTitle("Consulta de Estatus de Cuenta");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
