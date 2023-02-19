import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientTest {
    protected static final String HOST = "localhost";
    protected static final int PORT = 8989;

    public static void main(String[] args) {
        try (
                Socket clientSocket = new Socket(HOST, PORT);
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                ){
            Scanner scanner = new Scanner(System.in);
            String answer = scanner.nextLine();
            out.println(answer);
            String report = in.readLine();
            System.out.println(report);
            scanner.close();
        } catch (IOException e) {
            System.out.println("Сервер не стартовал");
            e.printStackTrace();
        }

    }
}
