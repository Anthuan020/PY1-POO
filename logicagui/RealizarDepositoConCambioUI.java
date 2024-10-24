package logicagui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import logicabancaria.Cuenta;
import logicabancaria.Transaccion;
import logicaalmacenamiento.UsuarioManager;

import java.time.LocalDate;

public class RealizarDepositoConCambioUI extends Application {

    private static final double VALOR_DOLAR = 565.0;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Realizar Depósito en Dólares");

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

        // Campo para el monto en dólares
        Label montoLabel = new Label("Monto en Dólares:");
        GridPane.setConstraints(montoLabel, 0, 1);
        TextField montoInput = new TextField();
        montoInput.setPromptText("Ingrese el monto en dólares");
        GridPane.setConstraints(montoInput, 1, 1);

        // Botón para realizar el depósito
        Button depositarBtn = new Button("Depositar");
        GridPane.setConstraints(depositarBtn, 1, 2);

        // Resultado del depósito
        Label resultadoLabel = new Label();
        GridPane.setConstraints(resultadoLabel, 1, 3);

        // Añadiendo todos los nodos al GridPane
        grid.getChildren().addAll(cuentaLabel, cuentaInput, montoLabel, montoInput, depositarBtn, resultadoLabel);

        // Configurando la escena
        Scene scene = new Scene(grid, 400, 250);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Acción del botón
        depositarBtn.setOnAction(e -> {
            String cuentaIngresada = cuentaInput.getText();
            String montoIngresado = montoInput.getText();

            // Validar que los campos no estén vacíos
            if (cuentaIngresada.isEmpty() || montoIngresado.isEmpty()) {
                resultadoLabel.setText("Por favor, complete todos los campos.");
                return;
            }

            // Validar que el monto ingresado sea numérico y positivo
            double monto;
            try {
                monto = Double.parseDouble(montoIngresado);
                if (monto <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                resultadoLabel.setText("Ingrese un monto válido en dólares.");
                return;
            }

            // Buscar la cuenta por número
            Cuenta cuenta = buscarCuentaPorNumero(usuarioManager, Integer.parseInt(cuentaIngresada));
            if (cuenta == null) {
                resultadoLabel.setText("Número de cuenta no encontrado.");
                return;
            }

            // Realizar el depósito y registrar la transacción
            realizarDeposito(cuenta, monto);
            resultadoLabel.setText("Depósito realizado exitosamente.");
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

    // Método para realizar el depósito y registrar la transacción
    private void realizarDeposito(Cuenta cuenta, double monto) {
        LocalDate fecha = LocalDate.now();
        double comision = 0;

        // Verificar si se aplica comisión después de 5 transacciones
        if (cuenta.getTransacciones().size() >= 5) {
            comision = monto * VALOR_DOLAR * 0.02;
        }

        // Actualizar el saldo de la cuenta
        cuenta.cashDeposit((monto * VALOR_DOLAR) - comision);

        // Registrar la transacción
        Transaccion transaccion = new Transaccion("Depósito", monto, fecha, comision);
        cuenta.getTransacciones().add(transaccion);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
