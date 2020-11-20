package program;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import model.Contractor;
import model.Equipment;
import model.EquipmentList;
import model.Job;

import java.io.IOException;
import java.util.ArrayList;

public class EquipmentListController {

    @FXML
    private Button back, add, delete, create;

    @FXML
    private Label cost, address, contact;

    @FXML
    private AnchorPane anchor;

    @FXML
    private TableView<Equipment> equipmentTableView;

    @FXML
    private TableColumn<Equipment,String> name, brand, detail;

    @FXML
    private TableColumn<Equipment,Integer> total, amount;

    private Contractor contractor;

    private EquipmentList equipmentList;

    private  ArrayList<EquipmentList> equipmentListArrayList;

    private ArrayList<Equipment> equipmentArrayList;

    private ArrayList<Job> jobArrayList;

    private ArrayList<Contractor> contractorArrayList;

    private int JobID, ContractorID;

    public void SetContractor(Contractor contractor){
        this.contractor = contractor;
    }

    public void SetID(int jobID, int contractorID){
        this.JobID = jobID;
        this.ContractorID = contractorID;
    }

    public void setEquipmentList(EquipmentList equipmentList){
        this.equipmentList = equipmentList;
    }

    public void initialize(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                contractorArrayList = DBConnect.ReadContractor();
                for(Contractor contractor : contractorArrayList){
                    if(contractor.getID() == ContractorID){
                        SetContractor(contractor);
                    }
                }
                equipmentListArrayList = DBConnect.ReadEquipmentList();
                jobArrayList = DBConnect.ReadJob();
                //for(Job job : jobArrayList){
                //   JobBox.getItems().add(job.getAddress());
                //}
                for(Job job : jobArrayList){
                    if(job.getJobID() == JobID){
                        address.setText("Work Address : "+job.getAddress());
                        for(EquipmentList equipmentList : equipmentListArrayList){
                            if(equipmentList.getJob_id()==job.getJobID()){
                                setEquipmentList(equipmentList);
                            }
                        }
                    }
                }

                ArrayList<Equipment> use_equipment = new ArrayList<>();

                equipmentArrayList = DBConnect.ReadEquipment();
                for(Equipment equipment : equipmentArrayList){
                    if(equipment.getEquipmentlistid() == equipmentList.getEquipmentlist_id()){
                        use_equipment.add(new Equipment(equipment.getAmount(),equipment.getTotalprice(), equipment.getEquipmentname(), equipment.getBrand(), equipment.getDetail()));
                    }
                }

                ObservableList<Equipment> equipmentObservableList = FXCollections.observableArrayList(use_equipment);

                name.setCellValueFactory(new PropertyValueFactory<>("equipmentname"));
                brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
                amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
                total.setCellValueFactory(new PropertyValueFactory<>("totalprice"));
                detail.setCellValueFactory(new PropertyValueFactory<>("detail"));

                equipmentTableView.setItems(equipmentObservableList);

                cost.setText("Total Cost : " + equipmentList.getTotal_cost() + " Baht");
                contact.setText("Contact Tel:" +contractor.getPhone_number()+" , Email: "+contractor.getEmail());

//                create.setOnAction(new EventHandler<ActionEvent>() {
//
//                    @Override
//                    public void handle(ActionEvent event) {
//
//                        Label secondLabel = new Label("I'm a Label on new Window");
//
//                        StackPane secondaryLayout = new StackPane();
//                        secondaryLayout.getChildren().add(secondLabel);
//
//                        Scene secondScene = new Scene(secondaryLayout, 230, 100);
//
//                        // New window (Stage)
//                        Stage newWindow = new Stage();
//                        newWindow.setTitle("Second Stage");
//                        newWindow.setScene(secondScene);
//
//                        // Set position of second window, related to primary window.
//                        newWindow.setX(primaryStage.getX() + 200);
//                        newWindow.setY(primaryStage.getY() + 100);
//
//                        newWindow.show();
//                    }
//                });
            }
        });
    }

    @FXML
    private void CreateDocumentOnAction(Event event)throws IOException{

        Stage stage = (Stage) create.getScene().getWindow();

//        PrinterJob printerJob = PrinterJob.createPrinterJob();
//        if(printerJob.showPrintDialog(stage.getOwner()) && printerJob.printPage(anchor)){
//            printerJob.endJob();
//        }

        ImageView imageView =new ImageView();
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
        double scaleX = pageLayout.getPrintableWidth() / equipmentTableView.getBoundsInParent().getWidth();
        double scaleY = pageLayout.getPrintableHeight() / equipmentTableView.getBoundsInParent().getHeight();
        imageView.getTransforms().add(new Scale(scaleX, scaleY));

        address.setStyle("-fx-text-fill: #000000");
        cost.setStyle("-fx-text-fill: #000000");
        anchor.setStyle("-fx-background-color: #ffffff");
        anchor.setScaleX(0.70);
        anchor.setScaleY(0.70);
        anchor.setTranslateX(-265);
        anchor.setTranslateY(-70);

        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            boolean successPrintDialog = job.showPrintDialog(stage.getOwner());
            if(successPrintDialog){
                boolean success = job.printPage(pageLayout,anchor);
                if (success) {
                    job.endJob();
                }

            }
        }
        address.setStyle("-fx-text-fill: #ffffff");
        cost.setStyle("-fx-text-fill: #ffffff");
        anchor.setStyle("-fx-background-color: #25282A");
        anchor.setTranslateX(0);
        anchor.setTranslateY(0);
        anchor.setScaleX(1.0);
        anchor.setScaleY(1.0);

    }


    @FXML
    private void GoBackOnAction(Event event)throws IOException {
        FXMLLoader loader = SceneChanger.GetLoaderOnAction(getClass(),"JobList.fxml");
        SceneChanger.ChangeSceneWithLoaderOnAction(back,"JobList.fxml",loader);
        JobListController jobListController = loader.getController();
        jobListController.setContractor(contractor);
    }

    @FXML
    private void GoDeleteOnAction(Event event)throws IOException {
        FXMLLoader loader = SceneChanger.GetLoaderOnAction(getClass(), "DeleteEquipment.fxml");
        SceneChanger.ChangeSceneWithLoaderOnAction(delete, "DeleteEquipment.fxml", loader);
        DeleteEquipmentController deleteEquipmentController = loader.getController();
        deleteEquipmentController.SetContractor(contractor);
        deleteEquipmentController.SetID(JobID, ContractorID);
    }

    @FXML
    private void GoAddEquipmentOnAction(Event event)throws IOException {
        FXMLLoader loader = SceneChanger.GetLoaderOnAction(getClass(), "AddEquipment.fxml");
        SceneChanger.ChangeSceneWithLoaderOnAction(add, "AddEquipment.fxml", loader);
        AddEquipmentController addEquipmentController = loader.getController();
        addEquipmentController.setContractor(contractor);
        addEquipmentController.SetID(JobID,ContractorID);
    }
}
