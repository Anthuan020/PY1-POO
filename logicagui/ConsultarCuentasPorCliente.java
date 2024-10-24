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

public class ConsultarCuentasPorCliente extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Crear etiquetas y campos de texto
        Label idClienteLabel = new Label("ID del Cliente:");
        TextField idClienteField = new TextField();
        idClienteField.setPromptText("Ingrese ID del cliente");

        // Botón para consultar cuentas
        Button consultarButton = new Button("Consultar Cuentas");

        // Área para mostrar los resultados
        TextArea resultadosArea = new TextArea();
        resultadosArea.setEditable(false);
        resultadosArea.setPrefHeight(200);
        resultadosArea.setPromptText("Los resultados aparecerán aquí...");

        // Acción al hacer clic en el botón consultar
        consultarButton.setOnAction(e -> {
            String idCliente = idClienteField.getText();

            // Buscar el usuario por ID
            Usuario usuario = UsuarioManager.getInstancia().buscarUsuarioPorId(idCliente);
            if (usuario != null) {
                // Mostrar datos del cliente
                StringBuilder datosCliente = new StringBuilder();
                datosCliente.append("Nombre: ").append(usuario.getName()).append("\n");
                datosCliente.append("ID: ").append(usuario.getId()).append("\n");
                datosCliente.append("Teléfono: ").append(usuario.getNumberTel()).append("\n");
                datosCliente.append("Correo: ").append(usuario.getMail()).append("\n");
                datosCliente.append("Cuentas:\n");

                // Listar todas las cuentas del cliente con su saldo
                for (Cuenta cuenta : usuario.getAccounts()) {
                    datosCliente.append("Número de Cuenta: ").append(cuenta.getId())
                            .append(" - Saldo: ").append(cuenta.getBalance()).append("\n");
                }

                resultadosArea.setText(datosCliente.toString());
            } else {
                resultadosArea.setText("ID de cliente no registrado.");
            }
        });

        // Configurar el diseño
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(idClienteLabel, idClienteField, consultarButton, resultadosArea);

        // Crear la escena y mostrar la ventana
        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setTitle("Consulta de Cuentas por Cliente");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
