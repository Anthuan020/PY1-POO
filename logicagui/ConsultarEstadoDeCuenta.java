package logicagui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

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
            
            // Aquí iría la lógica para verificar la cuenta y el PIN
            // Simulación de la consulta (esto será reemplazado con la lógica)
            if (!numeroCuenta.isEmpty() && !pin.isEmpty()) {
                // Lógica para consultar el estado de cuenta y rellenar el área de texto
                // Simulación:
                estadoCuentaArea.setText(
                        "Estimado usuario: NOMBRE COMPLETO DEL DUEÑO\n" +
                        "Estado de Cuenta para la cuenta: " + numeroCuenta + "\n" +
                        "Saldo actual: XXXX.XX colones\n" +
                        "Transacciones recientes:\n" +
                        "1. Depósito - 200.00 colones - 01/09/2024\n" +
                        "2. Retiro - 50.00 colones - 02/09/2024\n" +
                        "...\n" +
                        "Fin del estado de cuenta."
                );
            } else {
                estadoCuentaArea.setText("Por favor, ingrese todos los datos requeridos.");
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
