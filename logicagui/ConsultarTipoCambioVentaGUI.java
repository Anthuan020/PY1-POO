package logicagui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logicabancaria.TipoCambio;

public class ConsultarTipoCambioVentaGUI extends Application {

    private TextArea resultadoArea;
    private Button consultarButton;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Consultar Tipo de Cambio de Venta");

        // Etiqueta de título
        Label titulo = new Label("Consultar Tipo de Cambio de Venta");

        // Área de texto para mostrar el resultado
        resultadoArea = new TextArea();
        resultadoArea.setEditable(false);
        resultadoArea.setPrefHeight(150);

        // Botón para consultar tipo de cambio de venta
        consultarButton = new Button("Consultar Tipo de Cambio");

        // Acción del botón
        consultarButton.setOnAction(e -> consultarTipoCambio());

        // Layout principal
        VBox vbox = new VBox(10); // Espaciado de 10 píxeles entre elementos
        vbox.setPadding(new Insets(15, 15, 15, 15)); // Márgenes internos
        vbox.getChildren().addAll(titulo, resultadoArea, consultarButton);

        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void consultarTipoCambio() {
        // Simulación de la consulta al servicio de tipo de cambio
        TipoCambio tipoCambio = new TipoCambio();
        double tipoCambioVenta = tipoCambio.getVenta();

        // Mostrar el tipo de cambio en el área de resultados
        resultadoArea.setText("El tipo de cambio de venta del dólar es: " + tipoCambioVenta);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
