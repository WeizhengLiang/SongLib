//CS213 2022S
// Group Member: Jiaqi He, Weizheng Liang
package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FunctionController implements Initializable{
//	private Stage stage;
//	private Scene scene;
//	private Parent root;
	
	@FXML
	private Button Add;
	@FXML
	private Label songInfo;
	@FXML
	private ListView<String> listview;
	@FXML
	private Button Edit; 
	@FXML
	private TextField AddSongName;
	@FXML
	private TextField AddArtist;
	@FXML
	private TextField AddYear;
	@FXML
	private TextField AddAlbum;
	@FXML
	private TextField EditSongName;
	@FXML
	private TextField EditArtist;
	@FXML
	private TextField EditYear;
	@FXML
	private TextField EditAlbum; 

	
	@FXML public void elementClicked(MouseEvent arg0) throws IOException {
	    if(!listview.getItems().isEmpty()) {
	    	int selectedidx = listview.getSelectionModel().getSelectedIndex();
	    	SongList_util util = new SongList_util();
			JSONArray songlist = util.ReadSongList();
			JSONObject song =(JSONObject) songlist.get(selectedidx);
	    	EditSongName.setText(song.get("name").toString());
	    	EditArtist.setText(song.get("artist").toString());
	    	EditYear.setText(song.get("Year").toString());
	    	EditAlbum.setText(song.get("Album").toString());
			showInfo();       	
	    }                             
	}
	
	
	public void Add (ActionEvent event) throws IOException {
		Alert alert = new Alert (AlertType.CONFIRMATION);
		alert.setTitle("Add");
		alert.setHeaderText("You're about to add this song");
		alert.setContentText("Are you sure you want to add it in your songlib?");
		if(alert.showAndWait().get()==ButtonType.OK) {
				String song_name = AddSongName.getText().strip();
				String artist = AddArtist.getText().strip();
				String Year = AddYear.getText().strip();
				String Album = AddAlbum.getText().strip();
				SongList_util util = new SongList_util();
				JSONArray Songlist = new JSONArray();
				if(util.FindFile("songlist.json")==true) {
				Songlist = util.ReadSongList();
				}else {
					File songlist_file = new File(".//songlist.json");
					try {
						songlist_file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				Songlist = util.ReadSongList();
				JSONArray PrimarySonglist =util.ReadSongList(); 
				JSONObject song = new JSONObject();
				song.put("name", song_name);
				song.put("artist", artist);
				song.put("Year",Year);
				song.put("Album", Album);
				boolean Is_Valid = util.IsValid(Songlist, song);
				Songlist =util.InsertSong(Songlist, song);
				
				if(Songlist.size()!=PrimarySonglist.size()) {
					 AddSongName.setText("");
					AddArtist.setText("");
					AddYear.setText("");
					AddAlbum.setText("");
					System.out.println("The song has been added successfully");
				}
				int index = 0;
				if(Is_Valid) {
					sortJson();
					Songlist = util.ReadSongList();
					index = Songlist.indexOf(song);	
				}
				Refresh(index);
		}
	}
	public void Edit (ActionEvent event) throws IOException {

		if(listview.getItems().isEmpty()) {
			Alert empty_alert = new Alert (AlertType.INFORMATION);
			empty_alert.setTitle("Empty");
			empty_alert.setHeaderText("You cannot edit because the songlist is empty");
			empty_alert.show();
			return;
		}
		int selectedidx = listview.getSelectionModel().getSelectedIndex();
		Alert alert = new Alert (AlertType.CONFIRMATION);
		alert.setTitle("Edit");
		alert.setHeaderText("You're about to edit this song");
		alert.setContentText("Are you sure you want to edit it in your songlib?");
		if(alert.showAndWait().get()==ButtonType.OK) {
			
			String song_name = EditSongName.getText().strip();
			String artist = EditArtist.getText().strip();
			String Year = EditYear.getText().strip();
			String Album = EditAlbum.getText().strip();
			SongList_util util = new SongList_util();
			JSONArray Songlist = new JSONArray();
			JSONObject song = new JSONObject();
			Songlist = util.ReadSongList();
			song.put("name", song_name);
			song.put("artist", artist);
			song.put("Year",Year);
			song.put("Album", Album);
			boolean Is_Valid =util.IsValid(Songlist, song);
			Songlist = util.EditSong(Songlist,song,selectedidx);
			if(Is_Valid) {
				sortJson();
				Songlist = util.ReadSongList();
				selectedidx = Songlist.indexOf(song);
				System.out.println("The song has been edited successfully");
			}
			Refresh(selectedidx);
		}
	}

	public void Delete (ActionEvent event) throws IOException {
		if(listview.getItems().isEmpty()) {
			Alert empty_alert = new Alert (AlertType.INFORMATION);
			empty_alert.setTitle("Empty");
			empty_alert.setHeaderText("You cannot delete because the songlist is empty");
			empty_alert.show();
			return;
		}
		Alert alert = new Alert (AlertType.CONFIRMATION);
		alert.setTitle("Delete");
		alert.setHeaderText("You're about to delete this song");
		alert.setContentText("Are you sure you want to delete it in your songlib?");
		if(alert.showAndWait().get()==ButtonType.OK) {
			int selectedidx = listview.getSelectionModel().getSelectedIndex();
			JSONArray Songlist = new JSONArray();
			SongList_util util = new SongList_util();
			Songlist = util.ReadSongList();
			if (Songlist.size()==1) {
				Songlist.remove(0);
				EditSongName.setText("");
				EditArtist.setText("");
				EditYear.setText("");
				EditAlbum.setText("");
		        FileWriter file = new FileWriter(".//songlist.json");
		        Songlist.writeJSONString(file);
		        file.close();	
				Refresh(0);
				System.out.println("The song has been deleted successfully");
			}else if(selectedidx == Songlist.size()-1) {
				Songlist.remove(selectedidx);
		        FileWriter file = new FileWriter(".//songlist.json");
		        Songlist.writeJSONString(file);
		        file.close();	
				Refresh(selectedidx-1);
				System.out.println("The song has been deleted successfully");
			}else {
				Songlist.remove(selectedidx);
		        FileWriter file = new FileWriter(".//songlist.json");
		        Songlist.writeJSONString(file);
		        file.close();	
				Refresh(selectedidx);
				System.out.println("The song has been deleted successfully");
			}
		}
	}
	
	public void Refresh (int index) throws IOException {
		SongList_util util = new SongList_util();
		if (util.FindFile("songlist.json")==true) {
			sortJson();
			JSONArray songlist = util.ReadSongList();
			
			listview.getItems().clear();
			

			 for (int i =0; i<songlist.size();i++) {
				 listview.getItems().add(util.PrintSong((JSONObject)songlist.get(i), 1));	 
			 }
			 listview.getSelectionModel().select(index);
			 showInfo();

		}
	}
	public void showInfo() throws IOException{
		int selectedidx = listview.getSelectionModel().getSelectedIndex();
		SongList_util util = new SongList_util();
		JSONArray songlist = util.ReadSongList();
		if(songlist.size()==0) {
			songInfo.setText("Song name: \n Artist: \n Year: \n Album: ");
			return;
		}
		JSONObject song =(JSONObject) songlist.get(selectedidx);
		 songInfo.setText("Song name: "+song.get("name").toString()+"\n"+"Artist: "+song.get("artist").toString()+"\n"+"Year: "+song.get("Year").toString()+"\n"+"Album: "+song.get("Album").toString());//æŠ•å°„åˆ°labelä¸Š
		 
	}
    public void initialize(URL url, ResourceBundle rb) {
        try {
			Refresh(0);
			if (listview.getItems().isEmpty()) {
				return;
			}
	    	SongList_util util = new SongList_util();
			JSONArray songlist = util.ReadSongList();
			JSONObject song =(JSONObject) songlist.get(0);
	    	EditSongName.setText(song.get("name").toString());
	    	EditArtist.setText(song.get("artist").toString());
	    	EditYear.setText(song.get("Year").toString());
	    	EditAlbum.setText(song.get("Album").toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @SuppressWarnings("unchecked")
	public void sortJson() {
		JSONParser parser = new JSONParser();
		JSONArray jsonArr = null;
		JSONArray sortedJsonArray = new JSONArray();
		
		try {
			jsonArr = (JSONArray) parser.parse(new FileReader(".//songlist.json"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		List<JSONObject> jsonValues = new ArrayList<JSONObject>();
	    for (int i = 0; i < jsonArr.size(); i++) {
	        jsonValues.add((JSONObject) jsonArr.get(i));
	    }
	    Collections.sort( jsonValues, new Comparator<JSONObject>() {
	        private static final String KEY_NAME = "name";
	        private static final String KEY_ARTIST = "artist";
	        @Override
	        public int compare(JSONObject a, JSONObject b) {
	            String valA = new String();
	            String valB = new String();

	                valA = (String) a.get(KEY_NAME)+a.get(KEY_ARTIST);
	                valB = (String) b.get(KEY_NAME)+b.get(KEY_ARTIST);
	            return valA.compareTo(valB);
	        }
	    });
	    
	    for (int i = 0; i < jsonArr.size(); i++) {
	        sortedJsonArray.add(jsonValues.get(i));
	    }
	    
	    try (FileWriter file = new FileWriter("songlist.json")) 
        {
	    	//upload json file
            file.write(sortedJsonArray.toString());
        } catch (IOException e) {
			e.printStackTrace();
		}
	}
}
