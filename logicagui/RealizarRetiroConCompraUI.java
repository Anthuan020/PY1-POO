package logicagui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import logicabancaria.Cuenta;
import logicaalmacenamiento.UsuarioManager;

public class RealizarRetiroConCompraUI extends Application {

    private static final double VALOR_DOLAR = 540.0; // Tipo de cambio ficticio

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Realizar Retiro con Conversión de Moneda");

        // Layout de la interfaz
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        // Campo para el número de cuenta
        Label cuentaLabel = new Label("Número de Cuenta:");
        GridPane.setConstraints(cuentaLabel, 0, 0);
        TextField cuentaInput = new TextField();
        cuentaInput.setPromptText("Ingrese su número de cuenta");
        GridPane.setConstraints(cuentaInput, 1, 0);

        // Campo para el PIN
        Label pinLabel = new Label("PIN:");
        GridPane.setConstraints(pinLabel, 0, 1);
        PasswordField pinInput = new PasswordField();
        pinInput.setPromptText("Ingrese su PIN");
        GridPane.setConstraints(pinInput, 1, 1);

        // Campo para la palabra enviada por mensaje de texto
        Label palabraLabel = new Label("Palabra recibida por SMS:");
        GridPane.setConstraints(palabraLabel, 0, 2);
        TextField palabraInput = new TextField();
        palabraInput.setPromptText("Ingrese la palabra enviada");
        GridPane.setConstraints(palabraInput, 1, 2);

        // Campo para el monto del retiro en dólares
        Label montoLabel = new Label("Monto a Retirar (USD):");
        GridPane.setConstraints(montoLabel, 0, 3);
        TextField montoInput = new TextField();
        montoInput.setPromptText("Ingrese el monto en dólares (sin decimales)");
        GridPane.setConstraints(montoInput, 1, 3);

        // Botón para realizar el retiro
        Button retirarBtn = new Button("Retirar");
        GridPane.setConstraints(retirarBtn, 1, 4);

        // Resultado del retiro
        Label resultadoLabel = new Label();
        GridPane.setConstraints(resultadoLabel, 1, 5);

        // Añadiendo todos los nodos al GridPane
        grid.getChildren().addAll(cuentaLabel, cuentaInput, pinLabel, pinInput, 
                palabraLabel, palabraInput, montoLabel, montoInput, retirarBtn, resultadoLabel);

        // Configurando la escena
        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Acción del botón de retiro
        retirarBtn.setOnAction(e -> {
            try {
                // Leer valores de entrada
                int cuentaId = Integer.parseInt(cuentaInput.getText());
                String pin = pinInput.getText();
                String palabra = palabraInput.getText();
                double montoDolares = Double.parseDouble(montoInput.getText());

                // Validaciones básicas
                if (palabra.isEmpty()) {
                    resultadoLabel.setText("Error: La palabra recibida por SMS no puede estar vacía.");
                    return;
                }

                // Obtener la cuenta usando UsuarioManager
                Cuenta cuenta = buscarCuentaPorNumero(UsuarioManager.getInstancia(), cuentaId);
                if (cuenta == null) {
                    resultadoLabel.setText("Error: Cuenta no encontrada.");
                    return;
                }

                // Verificar el PIN
                if (!cuenta.verificarPin(pin)) {
                    resultadoLabel.setText("Error: PIN incorrecto.");
                    return;
                }

                // Realiza la conversión de USD a colones
                double montoColones = montoDolares * VALOR_DOLAR;

                // Verifica si hay saldo suficiente
                if (cuenta.getBalance() < montoColones) {
                    resultadoLabel.setText("Error: Fondos insuficientes.");
                    return;
                }

                // Realiza el retiro
                cuenta.cashWithdrawal(montoColones);
                resultadoLabel.setText("Retiro exitoso: " + montoColones + " colones.");
            } catch (NumberFormatException ex) {
                resultadoLabel.setText("Error: Ingrese valores numéricos válidos.");
            } catch (Exception ex) {
                resultadoLabel.setText("Error: " + ex.getMessage());
            }
        });
    }

    // Método para buscar una cuenta por número en todos los usuarios
    private Cuenta buscarCuentaPorNumero(UsuarioManager usuarioManager, int numeroCuenta) {
        for (var usuario : usuarioManager.getUsuarios()) {
            for (var cuenta : usuario.getAccounts()) {
                if (cuenta.getId() == numeroCuenta) {
                    return cuenta;
                }
            }
        }
        return null; // No se encontró la cuenta
    }

    public static void main(String[] args) {
        launch(args);
    }
}
