import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

//Classe provisoria
public class ClientSide {
    public static void main(String[] args) throws IOException {
        socket();
    }

    public static void socket() throws IOException {
        Scanner scanner = new Scanner(System.in);
        Socket socket = new Socket("localhost", 54321);


        //Envia mensagens ao servidor
        Thread threadSend = new Thread(() -> {
            while (socket.isConnected()) {
                String message = scanner.nextLine();
                DataOutputStream saida = null;
                try {
                    saida = new DataOutputStream(socket.getOutputStream());
                    saida.writeUTF(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        //Recebe todas as mensagens enviadas pelo servidor
        Thread threadReceive = new Thread(() -> {
            while (socket.isConnected()) {
                try {
                    DataInputStream entrada = new DataInputStream(socket.getInputStream());
                    String newMessage = entrada.readUTF();
                    if (newMessage.equals("/quit")) break;
                    System.out.println(newMessage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        threadReceive.start();
        threadSend.start();
        while (socket.isConnected()) {

        }
        socket.close();
    }
}
