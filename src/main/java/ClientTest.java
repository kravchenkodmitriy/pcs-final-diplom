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
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            System.out.println("entry word");


            try (Scanner scanner = new Scanner(System.in)) {
                while (scanner.hasNext()) {
                    var textInput = scanner.nextLine();

                    System.out.println(textInput);

                    out.println(textInput);

                    var answer = in.readLine();

                    System.out.println(answer);

                }
            }

        } catch (IOException e) {
            System.out.println("Нет соединения с сервером");
            e.printStackTrace();
        }

    }
}
