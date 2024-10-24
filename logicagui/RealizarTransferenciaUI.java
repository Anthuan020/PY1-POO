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

public class RealizarTransferenciaUI extends Application {

    private int codigoVerificacion; // Almacena el código de verificación enviado

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Realizar Transferencia");

        // Layout de la interfaz
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        // Campo para el número de cuenta origen
        Label cuentaOrigenLabel = new Label("Cuenta Origen:");
        GridPane.setConstraints(cuentaOrigenLabel, 0, 0);
        TextField cuentaOrigenInput = new TextField();
        cuentaOrigenInput.setPromptText("Ingrese el número de cuenta origen");
        GridPane.setConstraints(cuentaOrigenInput, 1, 0);

        // Campo para el PIN
        Label pinLabel = new Label("PIN:");
        GridPane.setConstraints(pinLabel, 0, 1);
        PasswordField pinInput = new PasswordField();
        pinInput.setPromptText("Ingrese su PIN");
        GridPane.setConstraints(pinInput, 1, 1);

        // Botón para enviar el código de verificación
        Button enviarCodigoBtn = new Button("Enviar Código de Verificación");
        GridPane.setConstraints(enviarCodigoBtn, 1, 2);
        enviarCodigoBtn.setMinWidth(200); // Opcional: ajustar el ancho del botón

        // Campo para el código de verificación enviado por correo
        Label codigoLabel = new Label("Código de Verificación (enviado por correo):");
        GridPane.setConstraints(codigoLabel, 0, 3);
        TextField codigoInput = new TextField();
        codigoInput.setPromptText("Ingrese el código recibido");
        GridPane.setConstraints(codigoInput, 1, 3);

        // Campo para el monto de la transferencia
        Label montoLabel = new Label("Monto a Transferir (Colones):");
        GridPane.setConstraints(montoLabel, 0, 4);
        TextField montoInput = new TextField();
        montoInput.setPromptText("Ingrese el monto en colones (sin decimales)");
        GridPane.setConstraints(montoInput, 1, 4);

        // Campo para el número de cuenta destino
        Label cuentaDestinoLabel = new Label("Cuenta Destino:");
        GridPane.setConstraints(cuentaDestinoLabel, 0, 5);
        TextField cuentaDestinoInput = new TextField();
        cuentaDestinoInput.setPromptText("Ingrese el número de cuenta destino");
        GridPane.setConstraints(cuentaDestinoInput, 1, 5);

        // Botón para realizar la transferencia
        Button transferirBtn = new Button("Transferir");
        GridPane.setConstraints(transferirBtn, 1, 6);

        // Resultado de la transferencia
        Label resultadoLabel = new Label();
        GridPane.setConstraints(resultadoLabel, 1, 7);

        // Añadiendo todos los nodos al GridPane
        grid.getChildren().addAll(cuentaOrigenLabel, cuentaOrigenInput, pinLabel, pinInput,
                enviarCodigoBtn, codigoLabel, codigoInput, montoLabel, montoInput, cuentaDestinoLabel, cuentaDestinoInput,
                transferirBtn, resultadoLabel);

        // Configurando la escena
        Scene scene = new Scene(grid, 450, 350);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Acción del botón de enviar código
        enviarCodigoBtn.setOnAction(e -> {
            try {
                // Leer el número de cuenta origen
                int cuentaOrigenId = Integer.parseInt(cuentaOrigenInput.getText());
                String pin = pinInput.getText();

                // Obtener la cuenta origen usando UsuarioManager
                Cuenta cuentaOrigen = buscarCuentaPorNumero(UsuarioManager.getInstancia(), cuentaOrigenId);
                if (cuentaOrigen == null) {
                    resultadoLabel.setText("Error: Cuenta origen no encontrada.");
                    return;
                }

                // Verificar el PIN
                if (!cuentaOrigen.verificarPin(pin)) {
                    resultadoLabel.setText("Error: PIN incorrecto.");
                    return;
                }

                // Generar y enviar código de verificación
                CodigoSecreto codigoSecreto = new CodigoSecreto();
                codigoVerificacion = codigoSecreto.CodigoSecretoCreate();
                String emailDestinatario = cuentaOrigen.getOwner().getMail();
                codigoSecreto.EnviarCodigo(emailDestinatario, codigoVerificacion);
                resultadoLabel.setText("Código de verificación enviado a " + emailDestinatario);

            } catch (Exception ex) {
                resultadoLabel.setText("Error: " + ex.getMessage());
            }
        });

        // Acción del botón de transferencia
        transferirBtn.setOnAction(e -> {
            try {
                // Leer valores de entrada
                int cuentaOrigenId = Integer.parseInt(cuentaOrigenInput.getText());
                String pin = pinInput.getText();
                double montoColones = Double.parseDouble(montoInput.getText());
                int cuentaDestinoId = Integer.parseInt(cuentaDestinoInput.getText());

                // Obtener la cuenta origen usando UsuarioManager
                Cuenta cuentaOrigen = buscarCuentaPorNumero(UsuarioManager.getInstancia(), cuentaOrigenId);
                if (cuentaOrigen == null) {
                    resultadoLabel.setText("Error: Cuenta origen no encontrada.");
                    return;
                }

                // Comprobar el código de verificación
                if (Integer.parseInt(codigoInput.getText()) != codigoVerificacion) {
                    resultadoLabel.setText("Error: Código de verificación incorrecto.");
                    return;
                }

                // Verifica si la cuenta destino existe
                Cuenta cuentaDestino = buscarCuentaPorNumero(UsuarioManager.getInstancia(), cuentaDestinoId);
                if (cuentaDestino == null) {
                    resultadoLabel.setText("Error: Cuenta destino no encontrada.");
                    return;
                }

                // Verifica si hay saldo suficiente
                if (cuentaOrigen.getBalance() < montoColones) {
                    resultadoLabel.setText("Error: Fondos insuficientes en la cuenta origen.");
                    return;
                }

                // Realiza la transferencia
                cuentaOrigen.transferir(montoColones, cuentaDestino);
                resultadoLabel.setText("Transferencia exitosa: " + montoColones + " colones.");
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
