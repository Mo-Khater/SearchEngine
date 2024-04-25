import java.util.Scanner;
public class Main {
    public static void main() {
            Scanner myObj = new Scanner(System.in); // Create a Scanner object
            System.out.println("search for:");
            String query = myObj.nextLine();

            queryProcessor qp =new queryProcessor(query) ;
            qp.searchly();


    }
}