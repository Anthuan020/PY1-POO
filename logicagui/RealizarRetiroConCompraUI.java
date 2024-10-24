package logicagui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import logicabancaria.Cuenta;
import logicaalmacenamiento.UsuarioManager;
import logicacomunicacion.CodigoSecreto;
import logicabancaria.TipoCambio; // Importar la clase TipoCambio

public class RealizarRetiroConCompraUI extends Application {

    private int codigoVerificacion;

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

        // Campo para el código del correo
        Label codigoLabel = new Label("Código del Correo:");
        GridPane.setConstraints(codigoLabel, 0, 2);
        TextField codigoInput = new TextField();
        codigoInput.setPromptText("Ingrese el código enviado");
        GridPane.setConstraints(codigoInput, 1, 2);

        // Campo para el monto del retiro en dólares
        Label montoLabel = new Label("Monto a Retirar (USD):");
        GridPane.setConstraints(montoLabel, 0, 3);
        TextField montoInput = new TextField();
        montoInput.setPromptText("Ingrese el monto en dólares (sin decimales)");
        GridPane.setConstraints(montoInput, 1, 3);

        // Botón para enviar el código
        Button enviarCodigoBtn = new Button("Enviar Código");
        GridPane.setConstraints(enviarCodigoBtn, 1, 4);

        // Botón para realizar el retiro
        Button retirarBtn = new Button("Retirar");
        GridPane.setConstraints(retirarBtn, 1, 5);

        // Resultado del retiro
        Label resultadoLabel = new Label();
        GridPane.setConstraints(resultadoLabel, 1, 6);

        // Añadiendo todos los nodos al GridPane
        grid.getChildren().addAll(cuentaLabel, cuentaInput, pinLabel, pinInput,
                codigoLabel, codigoInput, montoLabel, montoInput, enviarCodigoBtn, retirarBtn, resultadoLabel);

        // Configurando la escena
        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Acción del botón para enviar el código
        enviarCodigoBtn.setOnAction(e -> {
            String cuentaStr = cuentaInput.getText();
            if (cuentaStr.isEmpty()) {
                resultadoLabel.setText("Error: Ingrese el número de cuenta.");
                return;
            }

            Cuenta cuenta = buscarCuentaPorNumero(UsuarioManager.getInstancia(), Integer.parseInt(cuentaStr));
            if (cuenta == null) {
                resultadoLabel.setText("Error: Cuenta no encontrada.");
                return;
            }

            // Generar y enviar el código
            CodigoSecreto codigoSecreto = new CodigoSecreto();
            codigoVerificacion = codigoSecreto.CodigoSecretoCreate();
            codigoSecreto.EnviarCodigo(cuenta.getOwner().getMail(), codigoVerificacion);
            resultadoLabel.setText("Código enviado a " + cuenta.getOwner().getMail());
        });

        // Acción del botón de retiro
        retirarBtn.setOnAction(e -> {
            try {
                // Leer valores de entrada
                int cuentaId = Integer.parseInt(cuentaInput.getText());
                String pin = pinInput.getText();
                int codigoIngresado = Integer.parseInt(codigoInput.getText());
                double montoDolares = Double.parseDouble(montoInput.getText());

                // Validaciones básicas
                if (codigoInput.getText().isEmpty()) {
                    resultadoLabel.setText("Error: El código no puede estar vacío.");
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

                // Verificar el código de verificación
                if (codigoIngresado != codigoVerificacion) {
                    resultadoLabel.setText("Error: Código de verificación incorrecto.");
                    return;
                }

                // Obtener el tipo de cambio actual
                TipoCambio tipoCambio = new TipoCambio();
                double valorCompra = tipoCambio.getCompra(); // Obtener el tipo de cambio de compra

                // Realiza la conversión de USD a colones
                double montoColones = montoDolares * valorCompra;

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
