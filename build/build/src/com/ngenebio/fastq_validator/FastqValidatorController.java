package com.ngenebio.fastq_validator;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FastqValidatorController {
	@FXML
	private Button fileSelectionButton;
	
	@FXML
	private	ListView<String> selectedFilesListView;
	
	@FXML
	private ProgressBar validationProgressBar;
	
	@FXML
	private	ListView<String> validationResultListView;
	
	@FXML
	private Label validationStatusLabel;
	
	private List<File> fastqFiles;
	
	private Stage primaryStage;
	
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	public void show() {
		fileSelectionButton.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();			
			fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Fastq files(*.fastq.gz)", "*.fastq.gz"));
			fileChooser.setTitle("Select fastq files.");
			fileChooser.setInitialDirectory(
	                new File(System.getProperty("user.home"))
	            );
			fastqFiles = fileChooser.showOpenMultipleDialog(primaryStage);
						
		});	
	}



	@FXML
	private void startConvert() {
		Task<Void> validateTask = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				
				//this.updateProgress(0, outputFiles.size());
				this.updateMessage("Start");
				int completeCount = 0;
				String completeMessage = "Complete. \n";
				int fileCount = fastqFiles.size();
				this.updateProgress(completeCount, fileCount);
				
				for(File fastqFile : fastqFiles) {					
					final BufferedInputStream br;
					br = new BufferedInputStream(new GZIPInputStream(new FileInputStream(fastqFile)));
					br.mark(0);
					BufferedReader reader = new BufferedReader(new InputStreamReader(br));
					long fastqTotalLine = reader.lines().count();
					
				}
				this.updateMessage(completeMessage);
				return null;
				
			}

			@Override
			protected void succeeded() {
				// TODO Auto-generated method stub
				super.succeeded();
			}

			@Override
			protected void cancelled() {
				// TODO Auto-generated method stub
				super.cancelled();
			}

			@Override
			protected void failed() {
				// TODO Auto-generated method stub
				super.failed();
			}			
		};
		validationProgressBar.progressProperty().bind(validateTask.progressProperty());
		validationStatusLabel.textProperty().bind(validateTask.messageProperty());
		Thread t = new Thread(validateTask);
		t.start();
	}
}
