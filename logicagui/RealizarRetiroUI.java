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

public class RealizarRetiroUI extends Application {

    // Almacenar el código generado
    private int codigoGenerado = -1;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Realizar Retiro en Colones");

        // Obtener la instancia de UsuarioManager
        UsuarioManager usuarioManager = UsuarioManager.getInstancia();

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

        // Botón para generar y enviar el código secreto
        Button generarCodigoBtn = new Button("Generar Código Secreto");
        GridPane.setConstraints(generarCodigoBtn, 1, 2);

        // Campo para la palabra enviada por SMS
        Label palabraLabel = new Label("Palabra recibida por SMS:");
        GridPane.setConstraints(palabraLabel, 0, 3);
        TextField palabraInput = new TextField();
        palabraInput.setPromptText("Ingrese la palabra enviada");
        GridPane.setConstraints(palabraInput, 1, 3);

        // Campo para el monto del retiro
        Label montoLabel = new Label("Monto a Retirar:");
        GridPane.setConstraints(montoLabel, 0, 4);
        TextField montoInput = new TextField();
        montoInput.setPromptText("Ingrese el monto en colones (sin decimales)");
        GridPane.setConstraints(montoInput, 1, 4);

        // Botón para realizar el retiro
        Button retirarBtn = new Button("Retirar");
        GridPane.setConstraints(retirarBtn, 1, 5);

        // Resultado del retiro
        Label resultadoLabel = new Label();
        GridPane.setConstraints(resultadoLabel, 1, 6);

        // Añadiendo todos los nodos al GridPane
        grid.getChildren().addAll(
                cuentaLabel, cuentaInput, pinLabel, pinInput,
                generarCodigoBtn, palabraLabel, palabraInput,
                montoLabel, montoInput, retirarBtn, resultadoLabel
        );

        // Configurando la escena
        Scene scene = new Scene(grid, 400, 350);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Acción del botón para generar y enviar el código secreto
        generarCodigoBtn.setOnAction(e -> {
            String cuentaIngresada = cuentaInput.getText();
            String pinIngresado = pinInput.getText();

            // Validar campos vacíos para cuenta y PIN antes de generar el código
            if (cuentaIngresada.isEmpty() || pinIngresado.isEmpty()) {
                resultadoLabel.setText("Por favor, ingrese el número de cuenta y el PIN.");
                return;
            }

            // Buscar la cuenta por número
            Cuenta cuenta = buscarCuentaPorNumero(usuarioManager, Integer.parseInt(cuentaIngresada));
            if (cuenta == null) {
                resultadoLabel.setText("Número de cuenta no encontrado.");
                return;
            }

            // Verificar el PIN
            if (!cuenta.verificarPin(pinIngresado)) {
                resultadoLabel.setText("PIN incorrecto.");
                return;
            }

            // Generar y enviar el código secreto
            CodigoSecreto codigoSecreto = new CodigoSecreto();
            codigoGenerado = codigoSecreto.CodigoSecretoCreate();
            codigoSecreto.EnviarCodigo(cuenta.getOwner().getMail(), codigoGenerado);
            resultadoLabel.setText("El código secreto ha sido enviado a su correo.");
        });

        // Acción del botón de retirar
        retirarBtn.setOnAction(e -> {
            String palabraIngresada = palabraInput.getText();
            String montoIngresado = montoInput.getText();

            // Validar campos vacíos para palabra y monto
            if (palabraIngresada.isEmpty() || montoIngresado.isEmpty()) {
                resultadoLabel.setText("Por favor, ingrese la palabra recibida y el monto.");
                return;
            }

            // Validar monto como número positivo
            double monto;
            try {
                monto = Double.parseDouble(montoIngresado);
                if (monto <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                resultadoLabel.setText("Ingrese un monto válido.");
                return;
            }

            // Verificación de la palabra recibida por SMS
            if (!palabraIngresada.equalsIgnoreCase(String.valueOf(codigoGenerado))) {
                resultadoLabel.setText("Palabra recibida incorrecta.");
                return;
            }

            // Buscar la cuenta por número nuevamente
            String cuentaIngresada = cuentaInput.getText();
            Cuenta cuenta = buscarCuentaPorNumero(usuarioManager, Integer.parseInt(cuentaIngresada));
            if (cuenta == null) {
                resultadoLabel.setText("Número de cuenta no encontrado.");
                return;
            }

            // Verificar si hay saldo suficiente y realizar el retiro
            try {
                cuenta.cashWithdrawal(monto);
                resultadoLabel.setText("Retiro exitoso. Su nuevo saldo es: " + cuenta.getBalance() + " colones.");
            } catch (IllegalArgumentException ex) {
                resultadoLabel.setText(ex.getMessage());
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
