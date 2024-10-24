package logicagui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import logicabancaria.Cuenta;
import logicabancaria.Usuario;
import logicaalmacenamiento.UsuarioManager;

public class ConsultarSaldoEquivalenteDivisaInterfaz extends Application {

    // Simulación del tipo de cambio del dólar
    private static final double TIPO_CAMBIO_COMPRA = 540.00; // Simulación de tipo de cambio

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Consulta de Saldo en Divisa Extranjera");

        // Layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        // Etiquetas
        Label numeroCuentaLabel = new Label("Número de Cuenta:");
        GridPane.setConstraints(numeroCuentaLabel, 0, 0);

        TextField numeroCuentaInput = new TextField();
        GridPane.setConstraints(numeroCuentaInput, 1, 0);

        Label pinLabel = new Label("PIN:");
        GridPane.setConstraints(pinLabel, 0, 1);

        PasswordField pinInput = new PasswordField();
        GridPane.setConstraints(pinInput, 1, 1);

        // Botón de consulta
        Button consultarButton = new Button("Consultar Saldo");
        GridPane.setConstraints(consultarButton, 1, 2);

        consultarButton.setOnAction(e -> {
            String numeroCuenta = numeroCuentaInput.getText();
            String pin = pinInput.getText();

            if (numeroCuenta.isEmpty() || pin.isEmpty()) {
                mostrarAlerta("Error", "Por favor, complete todos los campos.");
            } else {
                // Verificación de cuenta y PIN
                Cuenta cuenta = buscarCuenta(numeroCuenta, pin);

                if (cuenta != null) {
                    // Obtener el saldo en colones
                    double saldoColones = cuenta.getBalance();
                    double saldoDolares = saldoColones / TIPO_CAMBIO_COMPRA;

                    // Mostrar información al usuario
                    String mensaje = String.format(
                        "Estimado usuario: %s,\n" +
                        "El saldo actual de su cuenta %d es de %.2f dólares.\n" +
                        "Para esta conversión se utilizó el tipo de cambio del dólar (compra).\n" +
                        "Según la simulación, el tipo de cambio de compra del dólar es de: %.2f",
                        cuenta.getOwner().getName(), cuenta.getId(), saldoDolares, TIPO_CAMBIO_COMPRA
                    );
                    mostrarAlerta("Saldo en Dólares", mensaje);
                } else {
                    mostrarAlerta("Error", "Número de cuenta o PIN incorrecto.");
                }
            }
        });

        // Añadir todo al layout
        grid.getChildren().addAll(numeroCuentaLabel, numeroCuentaInput, pinLabel, pinInput, consultarButton);

        // Configurar y mostrar la escena
        Scene scene = new Scene(grid, 350, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Simulación de búsqueda de cuenta por número y PIN
    private Cuenta buscarCuenta(String numeroCuenta, String pin) {
        try {
            int numeroCuentaInt = Integer.parseInt(numeroCuenta);
            UsuarioManager usuarioManager = UsuarioManager.getInstancia();

            for (Usuario usuario : usuarioManager.getUsuarios()) {
                for (Cuenta cuenta : usuario.getAccounts()) {
                    if (cuenta.getId() == numeroCuentaInt && cuenta.verificarPin(pin)) {
                        return cuenta;
                    }
                }
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Formato del número de cuenta no válido.");
        }
        return null;
    }

    // Método para mostrar alertas
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
