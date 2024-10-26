package logicagui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import logicaalmacenamiento.*;
import logicabancaria.Usuario;
import logicabancaria.Cuenta;
import java.util.ArrayList;
import java.util.List;
import XmlEditor.XmlController;

public class InterfazPrincipal extends Application {
        
    
    
    public void start(Stage primaryStage) {
        Label instruccionesLabel = new Label("Ingrese un número del 1 al 21 para elegir una opción:");
        cargar();
        // Listado de opciones disponibles
        TextArea opcionesArea = new TextArea(
            "1. Crear cliente\n" +
            "2. Crear cuenta\n" +
            "3. Cambiar PIN\n" +
            "4. Realizar depósito\n" +
            "5. Realizar depósito con tipo de cambio\n" +
            "6. Realizar retiro\n" +
            "7. Realizar retiro con tipo de cambio\n" +
            "8. Realizar transferencia\n" +
            "9. Consultar transacciones\n" +
            "10. Consultar tipo de cambio de compra\n" +
            "11. Consultar tipo de cambio de venta\n" +
            "12. Consultar saldo\n" +
            "13. Consultar saldo equivalente en divisa\n" +
            "14. Consultar estado de cuenta\n" +
            "15. Consultar estado de cuenta dolarizado\n" +
            "16. Cambiar número de teléfono del cliente\n" +
            "17. Cambiar correo electrónico del cliente\n" +
            "18. Consultar estatus de la cuenta\n" +
            "19. Consultar cuentas por cliente\n" +
            "20. Eliminar cuenta del cliente\n" +
            "21. Salir"
        );
        opcionesArea.setEditable(false);
        opcionesArea.setPrefHeight(300); // Ajusta el tamaño según sea necesario

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
                    if (opcion == 21) {
                        guardar();
                        primaryStage.close(); // Cierra la aplicación si se selecciona la opción 21
                    } else {
                        abrirInterfaz(opcion);
                    }
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
        grid.add(opcionesArea, 0, 1);
        grid.add(opcionField, 0, 2);
        grid.add(seleccionarButton, 0, 3);
        grid.add(mensajeArea, 0, 4, 2, 1);

        Scene scene = new Scene(grid, 400, 600);
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
                new ConsultarTipoCambioGUI().start(nuevaVentana);
                break;
            case 11:
                new ConsultarTipoCambioVentaGUI().start(nuevaVentana);
                break;
            case 12:
                new ConsultarSaldoInterfaz().start(nuevaVentana);
                break;
            case 13:
                new ConsultarSaldoEquivalenteDivisaInterfaz().start(nuevaVentana);
                break;
            case 14:
                new ConsultarEstadoDeCuenta().start(nuevaVentana);
                break;
            case 15:
                new ConsultarEstadoDeCuentaDolarizado().start(nuevaVentana);
                break;
            case 16:
                new CambiarNumeroTelefonoCliente().start(nuevaVentana);
                break;
            case 17:
                new CambiarCorreoElectronicoCliente().start(nuevaVentana);
                break;
            case 18:
                new ConsultarEstatusCuenta().start(nuevaVentana);
                break;
            case 19:
                new ConsultarCuentasPorCliente().start(nuevaVentana);
                break;
            case 20:
                new EliminarCuentaCliente().start(nuevaVentana);
                break;
            default :
                break;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    public void guardar (){
        UsuarioManager usuarioManager = UsuarioManager.getInstancia();
        List<Usuario> Users = usuarioManager.getUsuarios();
        System.out.println(Users.size());
        if (Users.size() > 0 && !Users.isEmpty()){
            XmlController lest = new XmlController("Xmls/Usuarios.XML");
            XmlController lestC = new XmlController("Xmls/Cuentas.XML");
            for (Usuario usuario: Users){
                lest.insertarUsuario(usuario);
                List<Cuenta> cuentas = usuario.getAccounts();
                if(cuentas.size() > 0 && !cuentas.isEmpty()){
                    for (Cuenta account : cuentas){
                        lestC.insertarCuenta(account);
                    }
                    lestC.guardarCambios();
                }
            }
            lest.guardarCambios();
        }
    }
    
    public void cargar(){
        UsuarioManager usuarioManager = UsuarioManager.getInstancia();
        XmlController pars = new XmlController("Xmls/Usuarios.XML");
        usuarioManager.setUsuarios(pars.extraerUsuarios());
    }
    
    
    
    
}
