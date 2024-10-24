package logicagui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import logicabancaria.Cuenta;
import logicaalmacenamiento.UsuarioManager;

public class CambiarPinUI extends Application {

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Cambiar PIN de Cuenta");

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

    // Campo para el PIN actual
    Label pinActualLabel = new Label("PIN Actual:");
    GridPane.setConstraints(pinActualLabel, 0, 1);
    PasswordField pinActualInput = new PasswordField();
    pinActualInput.setPromptText("Ingrese su PIN actual");
    GridPane.setConstraints(pinActualInput, 1, 1);

    // Campo para el nuevo PIN
    Label nuevoPinLabel = new Label("Nuevo PIN:");
    GridPane.setConstraints(nuevoPinLabel, 0, 2);
    PasswordField nuevoPinInput = new PasswordField();
    nuevoPinInput.setPromptText("Ingrese su nuevo PIN");
    GridPane.setConstraints(nuevoPinInput, 1, 2);

    // Botón para cambiar el PIN
    Button cambiarPinBtn = new Button("Cambiar PIN");
    GridPane.setConstraints(cambiarPinBtn, 1, 3);

    // Resultado
    Label resultadoLabel = new Label();
    GridPane.setConstraints(resultadoLabel, 1, 4);

    grid.getChildren()
        .addAll(
            cuentaLabel, cuentaInput, pinActualLabel, pinActualInput, nuevoPinLabel, nuevoPinInput,
            cambiarPinBtn, resultadoLabel);

    Scene scene = new Scene(grid, 400, 250);
    primaryStage.setScene(scene);
    primaryStage.show();

    // Acción del botón
    cambiarPinBtn.setOnAction(
        e -> {
          String cuentaIngresada = cuentaInput.getText();
          String pinIngresado = pinActualInput.getText();
          String nuevoPin = nuevoPinInput.getText();

          // Validar campos vacíos
          if (cuentaIngresada.isEmpty() || pinIngresado.isEmpty() || nuevoPin.isEmpty()) {
            resultadoLabel.setText("Por favor, complete todos los campos.");
            return;
          }

          // Buscar la cuenta por número de cuenta
          Cuenta cuenta = buscarCuentaPorNumero(usuarioManager, Integer.parseInt(cuentaIngresada));

          if (cuenta == null) {
            resultadoLabel.setText("Número de cuenta incorrecto.");
          } else if (!cuenta.verificarPin(pinIngresado)) {
            resultadoLabel.setText("PIN actual incorrecto.");
          } else {
            // Cambiar el PIN
            cuenta.setPin(nuevoPin);
            resultadoLabel.setText(
                "Estimado usuario: "
                    + cuenta.getOwner().getName()
                    + ", le informamos que se ha cambiado satisfactoriamente el PIN de su cuenta "
                    + cuenta.getId());
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
