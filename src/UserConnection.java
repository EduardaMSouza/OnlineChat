import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Collection;

public class UserConnection {

    private Socket socket;

    //Array de sockets de todos os usuários online
    private Collection<Socket> onlineSockets;

    //Username do usuário
    private String username;

    //Construtor
    public UserConnection(Socket socket, Collection<Socket> onlineSockets) {
        this.socket = socket;
        this.onlineSockets = onlineSockets;

        Thread thread = new Thread(() -> {

            try {

                sendData(System.getenv("SERVER_ENTRY_MESSAGE"), socket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {

                this.username = recevieData();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println(username);
            recevieDataFromSocket();
            onlineSockets.remove(socket);
            try {

                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }) ;

        thread.start();

    }

    //Responsável por receber mensagem que um determinado usuário mandou
    // e em seguida mandar ele para todos os outros usuários online
    // no servidor com auxílio do método sendDataEveryone
    protected void recevieDataFromSocket() {
        String newMessage = "";

        try {

            while (socket.isConnected()) {
                newMessage = recevieData();

                if (newMessage.equals("/quit")) {
                    break;
                }
                sendDataEveryone(username + " : " + newMessage);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Responsável por mandar as mensagens recebidas pelo servidor
    // à todos os usuários online
    protected void sendDataEveryone(String message) {

        onlineSockets.forEach((eachSocket) -> {
            try {
                sendData(message, eachSocket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    //Recebe uma mensagem de uma conexão socket
    protected String recevieData() throws IOException {

        DataInputStream entrada = new DataInputStream(this.socket.getInputStream());

        return entrada.readUTF();
    }

    //Envia uma mensagem para um usuário usando sua conexão socket
    protected static void  sendData(String message, Socket socket) throws IOException {
        new DataOutputStream(socket.getOutputStream()).writeUTF(message);
    }
}
