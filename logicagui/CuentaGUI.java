package logicagui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import logicabancaria.Cuenta;
import logicabancaria.Usuario;
import logicaalmacenamiento.UsuarioManager;

public class CuentaGUI extends Application {

  // Obtener la instancia del singleton UsuarioManager
  private UsuarioManager usuarios = UsuarioManager.getInstancia();

  @Override
  public void start(Stage primaryStage) {
    // Crear el GridPane para la disposición de los elementos
    GridPane gridPane = new GridPane();
    gridPane.setVgap(10);
    gridPane.setHgap(10);

    // Crear los componentes de la interfaz
    Label idLabel = new Label("ID del Cliente:");
    TextField idInput = new TextField();
    gridPane.add(idLabel, 0, 0);
    gridPane.add(idInput, 1, 0);

    Label pinLabel = new Label("PIN:");
    PasswordField pinInput = new PasswordField();
    gridPane.add(pinLabel, 0, 1);
    gridPane.add(pinInput, 1, 1);

    Label amountLabel = new Label("Monto Inicial:");
    TextField amountInput = new TextField();
    gridPane.add(amountLabel, 0, 2);
    gridPane.add(amountInput, 1, 2);

    Button createButton = new Button("Crear Cuenta");
    gridPane.add(createButton, 1, 3);

    // Acción del botón
    createButton.setOnAction(
        event -> {
          String id = idInput.getText();
          String pin = pinInput.getText();
          String amountText = amountInput.getText();

          // Validación de campos vacíos
          if (id.isEmpty() || pin.isEmpty() || amountText.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "Todos los campos deben estar completos.");
            return;
          }

          try {
            // Convertir el monto a double
            double amount = Double.parseDouble(amountText);

            // Buscar usuario por ID
            Usuario user = usuarios.buscarUsuarioPorId(id);

            if (user == null) {
              showAlert(AlertType.ERROR, "Error", "Usuario no encontrado.");
            } else {
              // Crear la cuenta si el usuario existe
              Cuenta newAccount = new Cuenta(user, pin, amount);
              user.addAccount(newAccount);

              // Modificado el mensaje de éxito
              showAlert(
                  AlertType.INFORMATION,
                  "Cuenta Creada",
                  "Se ha creado una nueva cuenta:\n"
                      + "Número de cuenta: " + newAccount.getId() + "\n"
                      + "Estatus de cuenta: "
                      + (newAccount.getStatus() ? "Activa" : "Inactiva") + "\n"
                      + "Saldo actual: " + String.format("%.2f", newAccount.getBalance()) + "\n"
                      + "Nombre del dueño de la cuenta: "
                      + newAccount.getOwner().getName());
            }
          } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Error", "El monto inicial debe ser un número válido.");
          }
        });

    // Crear la escena y establecerla en el escenario
    Scene scene = new Scene(gridPane, 300, 200);
    primaryStage.setTitle("Crear Cuenta");
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  // Método para mostrar alertas
  private void showAlert(AlertType alertType, String title, String message) {
    Alert alert = new Alert(alertType);
    alert.setTitle(title);
    alert.setContentText(message);
    alert.setHeaderText(null);
    alert.showAndWait();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
