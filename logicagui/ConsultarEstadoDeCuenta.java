package logicagui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import logicabancaria.Cuenta;
import logicabancaria.Usuario;
import logicaalmacenamiento.UsuarioManager;

public class ConsultarEstadoDeCuenta extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Crear etiquetas y campos de texto
        Label cuentaLabel = new Label("Número de Cuenta:");
        TextField cuentaField = new TextField();
        cuentaField.setPromptText("Ingrese número de cuenta");

        Label pinLabel = new Label("PIN:");
        PasswordField pinField = new PasswordField();
        pinField.setPromptText("Ingrese PIN de la cuenta");

        // Botón para consultar el estado de cuenta
        Button consultarButton = new Button("Consultar Estado de Cuenta");

        // Área para mostrar el estado de cuenta
        TextArea estadoCuentaArea = new TextArea();
        estadoCuentaArea.setEditable(false);
        estadoCuentaArea.setPrefHeight(200);
        estadoCuentaArea.setPromptText("El estado de cuenta aparecerá aquí...");

        // Acción al hacer clic en el botón consultar
        consultarButton.setOnAction(e -> {
            String numeroCuenta = cuentaField.getText();
            String pin = pinField.getText();
            
            // Buscar el usuario por número de cuenta
            Usuario usuario = UsuarioManager.getInstancia().buscarUsuarioPorId(numeroCuenta);
            if (usuario != null) {
                // Buscar la cuenta asociada al usuario
                Cuenta cuenta = usuario.getAccounts().stream()
                        .filter(c -> c.getId() == Integer.parseInt(numeroCuenta))
                        .findFirst()
                        .orElse(null);

                if (cuenta != null && cuenta.verificarPin(pin)) {
                    // Aquí se obtiene la información para mostrar en el estado de cuenta
                    StringBuilder estado = new StringBuilder();
                    estado.append("Saldo: ").append(cuenta.getBalance()).append("\n");
                    estado.append("Estado: ").append(cuenta.getStatus() ? "Activa" : "Inactiva").append("\n");
                    estado.append("Fecha de Creación: ").append(cuenta.getfechaCreacion()).append("\n");
                    estado.append("Número de Transacciones: ").append(cuenta.getTransacciones().size()).append("\n");

                    estadoCuentaArea.setText(estado.toString());
                } else {
                    estadoCuentaArea.setText("PIN incorrecto o cuenta no válida.");
                }
            } else {
                estadoCuentaArea.setText("Número de cuenta no registrado.");
            }
        });

        // Configurar el diseño
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(10);
        grid.setHgap(10);

        // Agregar componentes al diseño
        grid.add(cuentaLabel, 0, 0);
        grid.add(cuentaField, 1, 0);
        grid.add(pinLabel, 0, 1);
        grid.add(pinField, 1, 1);
        grid.add(consultarButton, 1, 2);
        grid.add(estadoCuentaArea, 0, 3, 2, 1);

        // Crear la escena y mostrar la ventana
        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setTitle("Consulta de Estado de Cuenta");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
