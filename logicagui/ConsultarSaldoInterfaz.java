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

public class ConsultarSaldoInterfaz extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Consulta de Saldo");

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

            // Validar que los campos no estén vacíos
            if (numeroCuenta.isEmpty() || pin.isEmpty()) {
                mostrarAlerta("Error", "Por favor, complete todos los campos.");
            } else {
                // Buscar al usuario y validar el PIN
                Usuario usuario = UsuarioManager.getInstancia().buscarUsuarioPorId(numeroCuenta);
                if (usuario != null) {
                    // Verificar el PIN en cada cuenta del usuario
                    Cuenta cuentaValida = verificarCuenta(usuario, pin);
                    if (cuentaValida != null) {
                        double saldo = cuentaValida.getBalance(); // Obtener el saldo actual de la cuenta
                        mostrarAlerta("Saldo", "Estimado usuario, el saldo actual de su cuenta es de " + saldo + " colones.");
                    } else {
                        mostrarAlerta("Error", "PIN incorrecto o no se encontraron cuentas.");
                    }
                } else {
                    mostrarAlerta("Error", "Usuario no encontrado.");
                }
            }
        });

        // Añadir todo al layout
        grid.getChildren().addAll(numeroCuentaLabel, numeroCuentaInput, pinLabel, pinInput, consultarButton);

        // Configurar y mostrar la escena
        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Método para mostrar alertas
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Método para verificar la cuenta del usuario y el PIN
    private Cuenta verificarCuenta(Usuario usuario, String pin) {
        for (Cuenta cuenta : usuario.getAccounts()) {
            if (cuenta.verificarPin(pin)) {
                return cuenta; // Retorna la cuenta si el PIN es correcto
            }
        }
        return null; // Si no se encuentra ninguna cuenta válida
    }

    public static void main(String[] args) {
        launch(args);
    }
}
