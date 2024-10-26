package logicagui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logicaalmacenamiento.UsuarioManager;
import logicabancaria.Usuario;
import XmlEditor.XmlController;

public class GUIClientCreate extends Application {

  private UsuarioManager usuarios = UsuarioManager.getInstancia();

  @Override
  public void start(Stage primaryStage) {
    Label lblName = new Label("Nombre completo:");
    TextField txtName = new TextField();

    Label lblId = new Label("Identificación:");
    TextField txtId = new TextField();

    Label lblPhone = new Label("Número telefónico:");
    TextField txtPhone = new TextField();

    Label lblEmail = new Label("Correo electrónico:");
    TextField txtEmail = new TextField();

    Button btnSubmit = new Button("Crear Cliente");
    Label lblResult = new Label();

    btnSubmit.setOnAction(
        event -> {
          String name = txtName.getText();
          String id = txtId.getText();
          String phone = txtPhone.getText();
          String email = txtEmail.getText();

          if (validatePhone(phone) && validateEmail(email)) {
            Usuario newUser = new Usuario(name, id, phone, email);
            usuarios.agregar(newUser);

            lblResult.setText(
                "Se ha creado un nuevo cliente en el sistema, los datos del cliente son:\n"
                    + "Nombre completo: " + name + "\n"
                    + "Identificación: " + id + "\n"
                    + "Número de teléfono: " + phone + "\n"
                    + "Dirección de correo electrónico: " + email);
          } else {
            lblResult.setText("Error: Datos inválidos.");
          }
        });

    GridPane grid = new GridPane();
    grid.setPadding(new Insets(10));
    grid.setVgap(10);
    grid.setHgap(10);

    grid.add(lblName, 0, 0);
    grid.add(txtName, 1, 0);
    grid.add(lblId, 0, 1);
    grid.add(txtId, 1, 1);
    grid.add(lblPhone, 0, 2);
    grid.add(txtPhone, 1, 2);
    grid.add(lblEmail, 0, 3);
    grid.add(txtEmail, 1, 3);
    grid.add(btnSubmit, 1, 4);

    VBox vbox = new VBox(10);
    vbox.getChildren().addAll(grid, lblResult);

    Scene scene = new Scene(vbox, 400, 300);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Crear Cliente");
    primaryStage.show();
  }

  private boolean validatePhone(String phone) {
    return phone.matches("\\d{8}");
  }

  private boolean validateEmail(String email) {
    return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
  }

  public static void main(String[] args) {
    launch(args);
  }
}
