package program;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Contractor;
import model.EquipmentList;
import model.Job;

import java.io.IOException;
import java.util.ArrayList;

public class AddEquipmentController {

    @FXML
    private Button submit, back;

    @FXML
    private TextField equipment_name, price, amount, brand, detail;

    @FXML
    private ComboBox<String> JobBox;

    private Contractor contractor;

    private EquipmentList equipmentList;

    private ArrayList<Job> jobArrayList;

    private ArrayList<EquipmentList> equipmentListArrayList;

    private int JobID, ContractorID;

    public void SetID(int jobID, int contractorID){
        this.JobID = jobID;
        this.ContractorID = contractorID;
    }

    public void setContractor(Contractor contractor){
        this.contractor = contractor;
    }

    public void initialize(){

        equipmentListArrayList = DBConnect.ReadEquipmentList();
        jobArrayList = DBConnect.ReadJob();
        for(Job job : jobArrayList){
            JobBox.getItems().add(job.getAddress());
        }
    }

    @FXML
    private void GoBackOnAction(Event event)throws IOException {
        FXMLLoader loader = SceneChanger.GetLoaderOnAction(getClass(), "EquipmentList.fxml");
        SceneChanger.ChangeSceneWithLoaderOnAction(back, "EquipmentList.fxml", loader);
        EquipmentListController equipmentListController = loader.getController();
        equipmentListController.SetContractor(contractor);
        equipmentListController.SetID(JobID, ContractorID);
    }

    @FXML
    private void SubmitOnAction(Event event)throws IOException{

        for(Job job : jobArrayList){
            if(JobBox.getValue().equals(job.getAddress())){
                for(EquipmentList equipmentList : equipmentListArrayList){
                    if(equipmentList.getJob_id()==job.getJobID()){
                        this.equipmentList = equipmentList;
                    }
                }
            }
        }

        int Price = Integer.parseInt(price.getText());
        int Amount = Integer.parseInt(amount.getText());
        int TotalPrice = Price*Amount;
        int cost = equipmentList.getTotal_cost();
        int TotalCost = cost+TotalPrice;
        //System.out.println(equipmentList.getTotal_cost());
        DBConnect.WriteEquipment(equipment_name.getText(),Price,Amount,TotalPrice,brand.getText(),detail.getText(), equipmentList.getEquipmentlist_id());
        System.out.println(TotalCost);
        DBConnect.UpdateEquipmentList(TotalCost,equipmentList.getEquipmentlist_id());

        equipment_name.setText("");
        price.setText("");
        amount.setText("");
        brand.setText("");
        detail.setText("");

        Alert alert = new Alert(Alert.AlertType.INFORMATION,"Add Equipment Complete", ButtonType.OK);
        alert.showAndWait();
    }

}
