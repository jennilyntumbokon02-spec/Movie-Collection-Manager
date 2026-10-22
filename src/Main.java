import database.DatabaseSetup;
import ui.LandingPageFrame;

public class Main {
    public static void main(String[] args) {

        DatabaseSetup.createTables();

        new LandingPageFrame();
    }
}