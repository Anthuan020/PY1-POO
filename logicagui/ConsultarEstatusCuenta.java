package logicagui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import logicabancaria.Cuenta;
import logicabancaria.Usuario;
import logicabancaria.Transaccion; // Asegúrate de importar Transaccion
import logicaalmacenamiento.UsuarioManager;

import java.util.List;

public class ConsultarEstatusCuenta extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Crear etiquetas y campos de texto
        Label numeroCuentaLabel = new Label("Número de Cuenta:");
        TextField numeroCuentaField = new TextField();
        numeroCuentaField.setPromptText("Ingrese número de cuenta");

        Label estatusLabel = new Label("Estatus de la Cuenta:");
        TextArea estatusArea = new TextArea();
        estatusArea.setEditable(false);
        estatusArea.setPrefHeight(150); // Aumentar la altura para más información

        // Botón para consultar estatus de cuenta
        Button consultarButton = new Button("Consultar Estatus");

        // Acción al hacer clic en el botón consultar
        consultarButton.setOnAction(e -> {
            String numeroCuenta = numeroCuentaField.getText();
            
            // Validar si se ingresó un número de cuenta
            if (!numeroCuenta.isEmpty()) {
                Cuenta cuenta = buscarCuentaPorNumero(numeroCuenta);

                // Verificar si la cuenta está registrada
                if (cuenta != null) {
                    // Determinar el estatus de la cuenta
                    String estatusCuenta = cuenta.getBalance() > 0 ? "Activa" : "Inactiva";
                    String fechaCreacion = cuenta.getfechaCreacion().toString(); // Usar el método getfechaCreacion
                    int numTransacciones = cuenta.getTransacciones().size();
                    double impuestos = calcularImpuestos(cuenta.getTransacciones());
                    String fechaUltimaTransaccion = (numTransacciones > 0) ?
                        cuenta.getTransacciones().get(numTransacciones - 1).getDate().toString() : "No hay transacciones";

                    // Mostrar el estatus de la cuenta
                    estatusArea.setText("La cuenta número " + numeroCuenta + "\n" +
                            "Saldo: " + String.format("%.2f", cuenta.getBalance()) + "\n" +
                            "Estado: " + estatusCuenta + "\n" +
                            "Fecha de creación: " + fechaCreacion + "\n" +
                            "Número de transacciones: " + numTransacciones + "\n" +
                            "Impuestos de transacciones: " + String.format("%.2f", impuestos) + "\n" +
                            "Fecha de última transacción: " + fechaUltimaTransaccion + ".");
                } else {
                    estatusArea.setText("Error: La cuenta número " + numeroCuenta +
                            " no está registrada en el sistema.");
                }
            } else {
                estatusArea.setText("Por favor, ingrese un número de cuenta válido.");
            }
        });

        // Configurar el diseño
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(10);
        grid.setHgap(10);

        // Agregar componentes al diseño
        grid.add(numeroCuentaLabel, 0, 0);
        grid.add(numeroCuentaField, 1, 0);
        grid.add(consultarButton, 1, 1);
        grid.add(estatusLabel, 0, 2);
        grid.add(estatusArea, 1, 2);

        // Crear la escena y mostrar la ventana
        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setTitle("Consultar Estatus de Cuenta");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Cuenta buscarCuentaPorNumero(String numeroCuenta) {
        for (Usuario usuario : UsuarioManager.getInstancia().getUsuarios()) {
            for (Cuenta cuenta : usuario.getAccounts()) {
                if (String.valueOf(cuenta.getId()).equals(numeroCuenta)) {
                    return cuenta; // Retorna la cuenta si se encuentra
                }
            }
        }
        return null; // Retorna null si no se encuentra la cuenta
    }

    private double calcularImpuestos(List<Transaccion> transacciones) {
        double totalImpuestos = 0;
        for (Transaccion transaccion : transacciones) {
            totalImpuestos += transaccion.getCommission();
        }
        return totalImpuestos;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
