package logicagui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import logicaalmacenamiento.*;

public class InterfazPrincipal extends Application {
    
    public void start(Stage primaryStage) {
        Label instruccionesLabel = new Label("Ingrese un número del 1 al 21 para elegir una opción:");
        TextField opcionField = new TextField();
        opcionField.setPromptText("Ingrese opción");

        TextArea mensajeArea = new TextArea();
        mensajeArea.setEditable(false);
        mensajeArea.setPrefHeight(150);

        Button seleccionarButton = new Button("Seleccionar Opción");

        seleccionarButton.setOnAction(e -> {
            String opcionTexto = opcionField.getText();
            try {
                int opcion = Integer.parseInt(opcionTexto);
                if (opcion >= 1 && opcion <= 21) {
                    abrirInterfaz(opcion);
                } else {
                    mensajeArea.setText("Por favor, ingrese un número entre 1 y 21.");
                }
            } catch (NumberFormatException ex) {
                mensajeArea.setText("Por favor, ingrese un número válido.");
            }
        });

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(instruccionesLabel, 0, 0);
        grid.add(opcionField, 0, 1);
        grid.add(seleccionarButton, 0, 2);
        grid.add(mensajeArea, 0, 3, 2, 1);

        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setTitle("Interfaz Principal - Banco");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void abrirInterfaz(int opcion) {
        Stage nuevaVentana = new Stage();
        switch (opcion) {
            case 1:
                new GUIClientCreate().start(nuevaVentana);
                break;
            case 2:
                new CuentaGUI().start(nuevaVentana);
                break;
            case 3:
                new CambiarPinUI().start(nuevaVentana);
                break;
            case 4:
                new RealizarDepositoUI().start(nuevaVentana);
                break;
            case 5:
                new RealizarDepositoConCambioUI().start(nuevaVentana);
                break;
            case 6:
                new RealizarRetiroUI().start(nuevaVentana);
                break;
            case 7:
                new RealizarRetiroConCompraUI().start(nuevaVentana);
                break;
            case 8:
                new RealizarTransferenciaUI().start(nuevaVentana);
                break;
            case 9:
                new ConsultarTransaccionesGUI().start(nuevaVentana);
                break;
            case 10:
                new ConsultarEstadoDeCuenta().start(nuevaVentana);
                break;
            case 11:
                new ConsultarSaldoInterfaz().start(nuevaVentana);
                break;
            case 12:
                new ConsultarSaldoEquivalenteDivisaInterfaz().start(nuevaVentana);
                break;
            case 13:
                new ConsultarEstatusCuenta().start(nuevaVentana);
                break;
            case 14:
                new ConsultarEstadoDeCuentaDolarizado().start(nuevaVentana);
                break;
            case 15:
                new CambiarNumeroTelefonoCliente().start(nuevaVentana);
                break;
            case 16:
                new CambiarCorreoElectronicoCliente().start(nuevaVentana);
                break;
            case 17:
                new ConsultarCuentasPorCliente().start(nuevaVentana);
                break;
            case 18:
                new EliminarCuentaCliente().start(nuevaVentana);
                break;
            default:
                break;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
