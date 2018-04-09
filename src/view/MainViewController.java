package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import umlKurs.ChartCreator;
import umlKurs.Main;

import java.io.IOException;
import java.sql.SQLException;

public class MainViewController {

    public TextField k;
    public TextField l;
    public TextField D;
    public TextField c;
    public TextField alpha;
    public TextField T;
    public TextField N;
    public TextField nx;
    public TextField nt;
    private Main main;

    @FXML
    private void initialize() { }

    public void setMain(Main main) {
        this.main = main;
    }

    public void buildChart(ActionEvent actionEvent) throws IOException, SQLException {
        ChartCreator chartCreator = new ChartCreator(Double.parseDouble(k.getText()), Double.parseDouble(l.getText()),
                Double.parseDouble(D.getText()), Double.parseDouble(c.getText()), Double.parseDouble(alpha.getText()), Double.parseDouble(T.getText()), 1800, Integer.parseInt(nx.getText()), Integer.parseInt(nt.getText()));
        main.initChartForm();
        main.setChartCreator(chartCreator);
    }
}
