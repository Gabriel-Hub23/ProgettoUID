package com.pptattoo.pptattoo.Controller;

import com.pptattoo.pptattoo.Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardController {

    @FXML
    private TableView<Appuntamento> tableGiornliera;

    @FXML
    private TableColumn<Appuntamento, Integer> colonnaId;

    @FXML
    private TableColumn<Appuntamento, String> colonnaEmail;

    @FXML
    private TableColumn<Appuntamento, String> colonnaData;

    @FXML
    private TableColumn<Appuntamento, String> colonnaPrezzo;

    @FXML
    private TableColumn<Appuntamento, String> colonnaDipendente;

    @FXML
    private TableColumn<Appuntamento, String> colonnaNomeCognome;

    @FXML
    private TableColumn<Appuntamento, String> colonnaNumero;

    @FXML
    private TableColumn<Appuntamento, String> colonnaServizio;

    @FXML
    private BarChart<String, Number> chartGiornaliero;

    @FXML
    private Text txtNumeroCalcolare;

    @FXML
    private Text txtGuadagnoCalcolare;

    @FXML
    public void initialize() {
        setCellValue();
        populateTable();
        populateChart();
        calculateSummary();
    }

    private void setCellValue() {
        colonnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colonnaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colonnaData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colonnaPrezzo.setCellValueFactory(new PropertyValueFactory<>("prezzo"));
        colonnaDipendente.setCellValueFactory(new PropertyValueFactory<>("dipendente"));
        colonnaNomeCognome.setCellValueFactory(new PropertyValueFactory<>("identificativo"));
        colonnaNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colonnaServizio.setCellValueFactory(new PropertyValueFactory<>("servizio"));
    }

    public void populateTable() {
        try {
            ArrayList<Appuntamento> app = GestoreAppuntamenti.getInstance().listaAppuntamenti(false, "", "");
            ObservableList<Appuntamento> observableApp = FXCollections.observableArrayList(app);
            tableGiornliera.setItems(observableApp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void populateChart() {
        chartGiornaliero.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Appuntamenti");

        try {
            ArrayList<Appuntamento> app = GestoreAppuntamenti.getInstance().listaAppuntamenti(false, "", "");
            Map<String, Long> dateCounts = app.stream()
                    .collect(Collectors.groupingBy(Appuntamento::getData, Collectors.counting()));

            for (Map.Entry<String, Long> entry : dateCounts.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        chartGiornaliero.getData().add(series);
    }

    public void calculateSummary() {
        try {
            ArrayList<Appuntamento> app = GestoreAppuntamenti.getInstance().listaAppuntamenti(false, "", "");

            int numeroAppuntamenti = app.size();
            txtNumeroCalcolare.setText(String.valueOf(numeroAppuntamenti));

            double guadagnoTotale = app.stream()
                    .mapToDouble(Appuntamento::getPrezzo)
                    .sum();

            String guadagnoFormattato = String.format("%.2f €", guadagnoTotale);
            txtGuadagnoCalcolare.setText(guadagnoFormattato);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void avviaNotifica(MouseEvent event) {
        // Logica dell'evento
    }
}