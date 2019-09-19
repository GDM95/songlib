/*
 *	[CS213] Assignment 1
 *	by Greg Melillo and Eric Kim
*/

package songlib.app;

import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SongLibController {
	
	// list of title/artist Strings for display
	private ObservableList<Song> songlist;     

	//text fields to be populated with song info when selected in listview
	@FXML Button add;
	@FXML Button edit;
	@FXML Button delete;
	@FXML TextField artist;
	@FXML TextField title;
	@FXML TextField album;
	@FXML TextField year;
	
	//read-only text area for song details
	@FXML TextArea console;
	
	@FXML ListView<Song> listView;                
	
	@FXML
	private void handleButtonAction(ActionEvent e) {
		Button b = (Button)e.getSource();
		if (b == add) {
			addSong();
		} else if (b == edit) {
			editSong();
		} else {
			deleteSong();
		}
	}
	
	//displays selected song's info in the console as well as in text fields
	private void showSelectedSong() {
		Song song = listView.getSelectionModel().getSelectedItem();
		if(song == null) return;
		String songInfo = "Song Title: " +song.title +"\nArtist: " + song.artist +"\nAlbum: " +song.album +"\nYear: " +song.year;
		title.setText(song.title);
		artist.setText(song.artist);
		album.setText(song.album);
		year.setText(song.year);
		console.setText(songInfo);
	}
	
	private void addSong() {
		if(title.getText().isEmpty() || artist.getText().isEmpty()) {
			//error message in console
			console.setText("Song must have a title and artist.");
			return;
		}
		
		try
	    {
			Integer.parseInt(year.getText());
	    }
	    catch (NumberFormatException nfe)
	    {
	    	if(!year.getText().isEmpty()) {
	    		console.setText("Year must be a number.");
	    		//System.out.println("NumberFormatException: " + nfe.getMessage());
		    	return;
	    	}
	    }
		
		Song newSong = new Song(title.getText(), artist.getText(), album.getText(), year.getText());
		
		//search songlist for duplicate
		for(Song song : songlist) {
			if(song.isDuplicate(newSong)) {
				console.setText("Song already exists in the library.");
				return;
			}
		}
		
		songlist.add(newSong);
		sortList();
		clearFields();
	}
	
	private void editSong() {
		if(listView.getSelectionModel().getSelectedItem() == null) {
			console.setText("You must select the song you wish to edit in order to apply changes.");
			return;
		}
		if(title.getText().isEmpty() || artist.getText().isEmpty()) {
			//error message in console
			console.setText("Song must have a title and artist.");
			return;
		}
		
		try
	    {
			Integer.parseInt(year.getText());
	    }
	    catch (NumberFormatException nfe)
	    {
	    	if(!year.getText().isEmpty()) {
	    		console.setText("Year must be a number.");
	    		//System.out.println("NumberFormatException: " + nfe.getMessage());
		    	return;
	    	}
	    }
		
		Song editedSong = new Song(title.getText(), artist.getText(), album.getText(), year.getText());
		Song selectedSong = listView.getSelectionModel().getSelectedItem();
		
		//search songlist for duplicate, edit said song if no match is found
		for(Song song : songlist) {
			if(song.isDuplicate(editedSong) && !song.equals(selectedSong)) {
				console.setText("Song already exists in the library.");
				return;
			}
		}
		
		int index = listView.getSelectionModel().getSelectedIndex();
		songlist.set(index, editedSong);
		sortList();
		clearFields();
	}
	
	private void deleteSong() {
		if(listView.getSelectionModel().getSelectedItem() == null) {
			console.setText("You must select the song you wish to delete first.");
			return;
		}
		int index = listView.getSelectionModel().getSelectedIndex();
		songlist.remove(index);
		clearFields();
	}
		
	
	// wipes the input fields
	private void clearFields() {
		artist.clear();
		title.clear();
		album.clear();
		year.clear();
	}
	
	private void sortList() {
		Comparator<Song> comparator = Comparator.comparing(Song::getLowerCase);
		FXCollections.sort(songlist, comparator);
	}
	
	public void start(Stage mainStage) {
		
		// create an ObservableList 
		songlist = FXCollections.observableArrayList(); 
		
		// pull the Song data from file
		Scanner fileIn;
		try {
			fileIn = new Scanner(new File("songdata.txt"));
			String s;
			
			while(fileIn.hasNextLine()){
			    s = fileIn.nextLine();
			    List<String> result = Arrays.asList(s.split(",", -1));
				System.out.println(result);
				songlist.add(new Song(result.get(0), result.get(1), result.get(2), result.get(3)));
			}
			
			fileIn.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	
		listView.setItems(songlist); 
		sortList();
		
		// select the first item
		listView.getSelectionModel().select(0);
		
		//listen for listView selection changes, TextArea console is output stream
		listView.getSelectionModel().selectedItemProperty().addListener( (obs, oldVal, newVal) -> showSelectedSong() );
		
		console.setEditable(false);
		
		
		// save the Song data to file on close
		mainStage.setOnCloseRequest(evt -> {
			// save Song array content to file
			FileWriter writer;
			try {
				writer = new FileWriter("songdata.txt");
				for(Song s: songlist) {
					  writer.write(s.toStringFull());
					}
					writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		
		
	        // execute own shutdown procedure
	        mainStage.close();
	    });	
	}
}
