package logicagui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import logicabancaria.Cuenta;
import logicabancaria.Usuario;
import logicaalmacenamiento.UsuarioManager;

public class EliminarCuentaCliente extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Crear etiquetas y campos de texto
        Label numeroCuentaLabel = new Label("Número de Cuenta:");
        TextField numeroCuentaField = new TextField();
        numeroCuentaField.setPromptText("Ingrese número de cuenta");

        Label pinLabel = new Label("PIN de la Cuenta:");
        PasswordField pinField = new PasswordField();
        pinField.setPromptText("Ingrese PIN");

        TextArea mensajeArea = new TextArea();
        mensajeArea.setEditable(false);
        mensajeArea.setPrefHeight(150);

        // Botón para eliminar la cuenta
        Button eliminarButton = new Button("Eliminar Cuenta");

        // Acción al hacer clic en el botón eliminar
        eliminarButton.setOnAction(e -> {
            String numeroCuenta = numeroCuentaField.getText();
            String pin = pinField.getText();

            // Validar que se ingresaron los datos
            if (!numeroCuenta.isEmpty() && !pin.isEmpty()) {
                // Obtener el usuario y su cuenta correspondiente
                UsuarioManager usuarioManager = UsuarioManager.getInstancia();
                Cuenta cuenta = buscarCuentaPorNumero(numeroCuenta, usuarioManager);

                // Verificar si la cuenta está registrada
                if (cuenta != null) {
                    // Verificar el PIN
                    if (cuenta.verificarPin(pin)) {
                        // Mostrar confirmación de eliminación
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmar Eliminación");
                        alert.setHeaderText("Estimado usuario: " + cuenta.getOwner().getName() + 
                                            ", usted está a un paso de eliminar su cuenta " + numeroCuenta);
                        alert.setContentText("El saldo actual es de " + cuenta.getBalance() + 
                                            ". ¿Está seguro que desea eliminar esta cuenta?");
                        alert.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) {
                                // Simulación de eliminación de cuenta
                                boolean cuentaEliminada = eliminarCuenta(cuenta, usuarioManager);
                                if (cuentaEliminada) {
                                    if (cuenta.getBalance() > 0) {
                                        mensajeArea.setText("La cuenta ha sido eliminada. " +
                                                "Por favor, tome el dinero que ha sido dispuesto en el dispensador.");
                                    } else {
                                        mensajeArea.setText("La cuenta ha sido eliminada exitosamente.");
                                    }
                                } else {
                                    mensajeArea.setText("Error: No se pudo eliminar la cuenta.");
                                }
                            }
                        });
                    } else {
                        mensajeArea.setText("Error: El PIN ingresado es incorrecto.");
                    }
                } else {
                    mensajeArea.setText("Error: El número de cuenta no está registrado.");
                }
            } else {
                mensajeArea.setText("Por favor, ingrese el número de cuenta y el PIN.");
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
        grid.add(pinLabel, 0, 1);
        grid.add(pinField, 1, 1);
        grid.add(eliminarButton, 1, 2);
        grid.add(mensajeArea, 0, 3, 2, 1);

        // Crear la escena y mostrar la ventana
        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setTitle("Eliminar Cuenta del Cliente");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Método para buscar una cuenta por su número
    private Cuenta buscarCuentaPorNumero(String numeroCuenta, UsuarioManager usuarioManager) {
        for (Usuario usuario : usuarioManager.getUsuarios()) {
            for (Cuenta cuenta : usuario.getAccounts()) {
                if (String.valueOf(cuenta.getId()).equals(numeroCuenta)) {
                    return cuenta; // Retorna la cuenta si se encuentra
                }
            }
        }
        return null; // Retorna null si no se encuentra la cuenta
    }

    // Método para eliminar la cuenta del usuario
    private boolean eliminarCuenta(Cuenta cuenta, UsuarioManager usuarioManager) {
        Usuario owner = cuenta.getOwner();
        boolean removed = owner.getAccounts().remove(cuenta); // Eliminar la cuenta de la lista del usuario
        // O puedes implementar lógica para eliminar de un registro centralizado si es necesario
        return removed; // Retorna verdadero si se eliminó con éxito
    }

    public static void main(String[] args) {
        launch(args);
    }
}
