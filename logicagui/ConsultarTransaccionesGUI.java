package logicagui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import logicabancaria.*;
import java.util.List;
import java.util.Optional;

public class ConsultarTransaccionesGUI extends Application {
    private List<Cuenta> listaDeCuentas; // Lista de cuentas

    @Override
    public void start(Stage primaryStage) {
        // Componentes de la interfaz
        Label cuentaLabel = new Label("Número de Cuenta:");
        TextField cuentaField = new TextField();
        Label pinLabel = new Label("PIN:");
        PasswordField pinField = new PasswordField();
        TextArea resultArea = new TextArea();
        Button consultarButton = new Button("Consultar Transacciones");

        consultarButton.setOnAction(e -> {
            String cuentaNumero = cuentaField.getText();
            String pin = pinField.getText();

            // Verificación de cuenta
            Optional<Cuenta> cuentaOpt = verificarCuenta(cuentaNumero, pin);
            if (cuentaOpt != null){
                if (cuentaOpt.isPresent()) {
                    Cuenta cuenta = cuentaOpt.get();
                    String nombreCompleto = cuenta.getOwner().getName(); // Obtener el nombre del dueño
                    List<Transaccion> transacciones = cuenta.getTransacciones();
                    StringBuilder result = new StringBuilder("Estimado usuario: " + nombreCompleto + ", el detalle de todas las transacciones es:\n");
    
                    // Mostrar transacciones
                    if (transacciones.isEmpty()) {
                        result.append("No se encontraron transacciones.");
                    } else {
                        for (Transaccion transaccion : transacciones) {
                            result.append("Tipo: ").append(transaccion.getType())
                                  .append(", Monto: ").append(transaccion.getAmount())
                                  .append(", Fecha: ").append(transaccion.getDate())
                                  .append(", Comisión: ").append(transaccion.getCommission())
                                  .append("\n");
                        }
                    }
                    resultArea.setText(result.toString());
                } else {
                    resultArea.setText("Error: Cuenta no válida o PIN incorrecto.");
                }
            }
        });

        // Crear el layout
        GridPane grid = new GridPane();
        grid.add(cuentaLabel, 0, 0);
        grid.add(cuentaField, 1, 0);
        grid.add(pinLabel, 0, 1);
        grid.add(pinField, 1, 1);
        grid.add(consultarButton, 1, 2);
        grid.add(resultArea, 0, 3, 2, 1);

        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Consultar Transacciones");
        primaryStage.show();
    }

    // Verificación sin la palabra clave
    private Optional<Cuenta> verificarCuenta(String cuentaNumero, String pin) {
        try {
            int numeroCuenta = Integer.parseInt(cuentaNumero);
            for (Cuenta cuenta : listaDeCuentas) {
                if (cuenta.getId() == numeroCuenta && cuenta.verificarPin(pin)) {
                    return Optional.of(cuenta);
                }
            }
        } catch (NumberFormatException e) {
            // Manejo del error de formato aquí si es necesario
        }
        return Optional.empty();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
