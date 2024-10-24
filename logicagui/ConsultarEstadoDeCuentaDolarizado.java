package logicagui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import logicabancaria.Cuenta;
import logicabancaria.Transaccion;
import logicaalmacenamiento.UsuarioManager;
import logicabancaria.Usuario;
import logicabancaria.TipoCambio; // Importar la clase TipoCambio

public class ConsultarEstadoDeCuentaDolarizado extends Application {

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
        Button consultarButton = new Button("Consultar Estado de Cuenta Dolarizado");

        // Área para mostrar el estado de cuenta
        TextArea estadoCuentaArea = new TextArea();
        estadoCuentaArea.setEditable(false);
        estadoCuentaArea.setPrefHeight(250);
        estadoCuentaArea.setPromptText("El estado de cuenta dolarizado aparecerá aquí...");

        // Acción al hacer clic en el botón consultar
        consultarButton.setOnAction(e -> {
            String numeroCuenta = cuentaField.getText();
            String pin = pinField.getText();

            // Lógica para verificar la cuenta y el PIN
            UsuarioManager usuarioManager = UsuarioManager.getInstancia();
            Usuario usuario = usuarioManager.buscarUsuarioPorId(numeroCuenta);

            if (usuario != null && usuario.getAccounts().size() > 0) {
                // Obtener la cuenta del usuario (suponiendo que solo hay una cuenta para simplificar)
                Cuenta cuenta = usuario.getAccounts().get(0);
                
                // Verificar el PIN
                if (cuenta.verificarPin(pin)) {
                    // Obtener el tipo de cambio actual
                    TipoCambio tipoCambio = new TipoCambio();
                    double tipoCambioCompra = tipoCambio.getCompra(); // Obtener el tipo de cambio de compra

                    // Obtener saldo en colones y convertir a dólares
                    double saldoColones = cuenta.getBalance(); // Asumimos que el saldo está en colones
                    double saldoDolares = saldoColones / tipoCambioCompra; // Convertir a dólares

                    // Construir el estado de cuenta
                    StringBuilder estadoCuenta = new StringBuilder();
                    estadoCuenta.append("Estimado usuario: ").append(usuario.getName()).append("\n")
                                .append("Estado de Cuenta para la cuenta: ").append(numeroCuenta).append("\n")
                                .append("Saldo actual en dólares: ").append(String.format("%.2f", saldoDolares)).append(" USD\n")
                                .append("Tipo de cambio de compra utilizado: ").append(tipoCambioCompra).append(" CRC/USD\n\n")
                                .append("Transacciones recientes:\n");

                    // Mostrar transacciones dolarizadas
                    for (Transaccion transaccion : cuenta.getTransacciones()) {
                        // Convertir la transacción a dólares
                        double transaccionDolares = transaccion.getAmount() / tipoCambioCompra; // Asumimos que la transacción está en colones
                        estadoCuenta.append(transaccion.getType()).append(" - ")
                                    .append(String.format("%.2f", transaccionDolares)).append(" USD - ")
                                    .append(transaccion.getDate()).append("\n");
                    }
                    estadoCuenta.append("...\n").append("Fin del estado de cuenta.");
                    estadoCuentaArea.setText(estadoCuenta.toString());
                } else {
                    estadoCuentaArea.setText("PIN incorrecto. Por favor, inténtelo de nuevo.");
                }
            } else {
                estadoCuentaArea.setText("Por favor, ingrese todos los datos requeridos o cuenta no encontrada.");
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
        Scene scene = new Scene(grid, 450, 350);
        primaryStage.setTitle("Consulta de Estado de Cuenta Dolarizado");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
