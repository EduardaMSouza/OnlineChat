import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServerSideSocket {
    public static void main(String[] args) throws IOException, InterruptedException {
        OnlineChat();
    }

    //Responsável por deixar o servidor rodando
    //e por criar e adicionar o array dos sockets
    public static void OnlineChat() throws IOException, InterruptedException {
        ServerSocket serverSocket = new ServerSocket(54321);
        //Local onde os sockets ficam guardados para receberem e mandarem mensagens
        Collection<Socket> arrayListSockets = Collections.synchronizedCollection(new ArrayList<>());
        //fica rodando até desligar o servidor
        while (true) {
            Socket socket = serverSocket.accept();
            arrayListSockets.add(socket);
            UserConnection userConnection = new UserConnection(socket, arrayListSockets);
            System.out.println("Users Online: " + arrayListSockets.size());
        }
    }
}
