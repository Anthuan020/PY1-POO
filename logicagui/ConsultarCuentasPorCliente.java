package logicagui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import logicabancaria.*;
import logicaalmacenamiento.UsuarioManager; // Importar UsuarioManager

public class ConsultarCuentasPorCliente extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Crear etiquetas y campos de texto
        Label idClienteLabel = new Label("Identificación del Cliente:");
        TextField idClienteField = new TextField();
        idClienteField.setPromptText("Ingrese identificación");

        Label cuentasLabel = new Label("Cuentas del Cliente:");
        TextArea cuentasArea = new TextArea();
        cuentasArea.setEditable(false);
        cuentasArea.setPrefHeight(150);

        // Botón para consultar cuentas del cliente
        Button consultarButton = new Button("Consultar Cuentas");

        // Acción al hacer clic en el botón consultar
        consultarButton.setOnAction(e -> {
            String idCliente = idClienteField.getText();

            // Validar si se ingresó una identificación
            if (!idCliente.isEmpty()) {
                // Usar UsuarioManager para verificar la existencia del cliente
                UsuarioManager usuarioManager = UsuarioManager.getInstancia(); // Obtener la instancia de UsuarioManager
                Usuario cliente = usuarioManager.buscarUsuarioPorId(idCliente); // Buscar usuario por ID

                // Verificar si el cliente está registrado
                if (cliente != null) {
                    // Mostrar datos del cliente
                    String nombreCliente = cliente.getName();
                    StringBuilder sb = new StringBuilder();
                    sb.append("Datos del Cliente: ").append(nombreCliente).append(" (ID: ").append(idCliente).append(")\n");
                    sb.append("Cuentas:\n");

                    // Consultar las cuentas del cliente
                    for (Cuenta cuenta : cliente.getAccounts()) {
                        sb.append("Número de Cuenta: ").append(cuenta.getId())
                          .append(", Saldo Actual: ").append(cuenta.getBalance()).append(" colones\n");
                    }
                    cuentasArea.setText(sb.toString());
                } else {
                    cuentasArea.setText("Error: La identificación " + idCliente +
                        " no está registrada en el sistema.");
                }
            } else {
                cuentasArea.setText("Por favor, ingrese una identificación válida.");
            }
        });

        // Configurar el diseño
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(10);
        grid.setHgap(10);

        // Agregar componentes al diseño
        grid.add(idClienteLabel, 0, 0);
        grid.add(idClienteField, 1, 0);
        grid.add(consultarButton, 1, 1);
        grid.add(cuentasLabel, 0, 2);
        grid.add(cuentasArea, 1, 2);

        // Crear la escena y mostrar la ventana
        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setTitle("Consultar Cuentas por Cliente");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
