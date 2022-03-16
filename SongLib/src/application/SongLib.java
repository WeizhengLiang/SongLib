//CS213 2022S
// Group Member: Jiaqi He, Weizheng Liang

package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.text.Text;


public class SongLib extends Application {
	@Override
	public void start(Stage Stage) {
		try {

			Parent root= FXMLLoader.load(getClass().getResource("/Main.fxml"));
			Stage stage = new Stage();
			Scene scene= new Scene(root);
			stage.setScene(scene);
			FunctionController Controller = new FunctionController();
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
