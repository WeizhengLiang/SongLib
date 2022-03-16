//CS213 2022S
// Group Member: Jiaqi He, Weizheng Liang

package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
public class SongList_util {
public boolean SameSong (String name, String artist) throws IOException {
	 boolean SameName =false;
	 boolean SameArtist = false;
	 JSONArray song_list = ReadSongList();
		for (Object o: song_list) {
			JSONObject song = (JSONObject) o;
			if (name.equals(song.get("name"))) {
				SameName = true;
			}
			if(artist.equals(song.get("artist"))) {
				SameArtist = true;
			}
			if (SameName&&SameArtist) {
				return true;
			}else {
				SameName = false;
				SameArtist = false;
			}
		}
	return false;
}
public boolean IsValid (JSONArray Songlist, JSONObject Song) throws IOException {
	String song_name = (String) Song.get("name");
	String artist = (String) Song.get("artist");
	String Year = (String) Song.get("Year");
	String Album = (String) Song.get("Album");
	boolean sameSong =false;
	boolean IsNumber = true;
for (int i =0; i<Year.length();i++) {
	if(Character.isDigit(Year.charAt(i))==false) {
		IsNumber=false;
	}
}
	if(!Songlist.isEmpty()) {
		 sameSong = SameSong((String)Song.get("name"), (String)Song.get("artist"));
			if (sameSong) {
				Alert alert_duplicate = new Alert (AlertType.INFORMATION);
				alert_duplicate.setTitle("Duplicated Song");
				alert_duplicate.setHeaderText(null);
				alert_duplicate.setContentText("The song is already existed in the list");
				alert_duplicate.show();
				return false;
			}
		}
		if (song_name.isBlank()||artist.isBlank()||song_name.contains("|")||artist.contains("|")||Album.contains("|")||IsNumber==false) {
			Alert alert_invalid = new Alert (AlertType.INFORMATION);
			alert_invalid.setTitle("Invalid Input");
			alert_invalid.setHeaderText(null);
			alert_invalid.setContentText("The chracter '|' is not allowed , the year has to be postive integer, and the artist name and song name cannot be empty");
			alert_invalid.show();
			return false;
		}
	return true;
}

public JSONArray InsertSong (JSONArray Songlist, JSONObject Song) throws IOException {
	boolean sameSong = false;

	if(IsValid(Songlist,Song)){
		Songlist.add(Song);
	}
    try {
        FileWriter file = new FileWriter(".//songlist.json");
        Songlist.writeJSONString(file);
        file.close();
     } catch (IOException e) {
        e.printStackTrace();
     }
	return Songlist;
}
public JSONArray ReadSongList() throws IOException {
	JSONParser parser = new JSONParser();
	JSONArray song_list = new JSONArray();
	BufferedReader br = new BufferedReader(new FileReader(".//songlist.json")); 
	if (br.readLine()==null) {
		// the json file is empty, then return an empty songlist
		return song_list;
	}
	try {
		 song_list = (JSONArray) parser.parse(new FileReader(".//songlist.json"));
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} catch (ParseException e) {
		e.printStackTrace();
	}
	return song_list;
}
public JSONArray EditSong(JSONArray Songlist, JSONObject Song, int idx) throws IOException {
	boolean sameSong = false;
	if(IsValid(Songlist,Song)) {
		Songlist.remove(idx);
		Songlist.add(Song);
	}
    try {
        FileWriter file = new FileWriter(".//songlist.json");
        Songlist.writeJSONString(file);
        file.close();
     } catch (IOException e) {
        e.printStackTrace();
     }
	return Songlist;
}

public boolean FindFile(String target){
	File dir = new File("./");
	File[] dir_contents = dir.listFiles();
	for (int i =0; i<dir_contents.length;i++) {
		if (dir_contents[i].getName().equals(target)) {
			return true;				
		}
	}
	return false;
}
public String PrintSong (JSONObject song, int number) {
	String song_detail;
	if (number==1) {
		 song_detail = (String)song.get("name")+"|"+(String)song.get("artist");	
	}else {
		 song_detail = (String)song.get("name")+"|"+(String)song.get("artist")+"|"+(String)song.get("Year")+"|"+(String)song.get("Album");
	}
	return song_detail;
}
}
