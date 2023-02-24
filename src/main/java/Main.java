import com.google.gson.Gson;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static final Gson gson = new Gson();

    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));

        try (ServerSocket serverSocket = new ServerSocket(8989)) {
            while (true) { // в цикле(!) принимаем подключения
                try (
                        Socket socket = serverSocket.accept(); // На этом вызове поток зависает, в ожидании клиента
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream());
                ) {
                    // Считываю строку текста см. клиента.
                    var input = in.readLine();
                    
                    System.out.println(input);

                    // Получаю результат поиска.
                    var searchResult = engine.search(input);

                    // Сериализую в жсон
                    var serialized = gson.toJson(searchResult);

                    // Отправляю строку текста обратно
                    out.println(serialized);                }
            }
        } catch (IOException e) {
            System.out.println("Can't start the server");
            e.printStackTrace();
        }
    }
}