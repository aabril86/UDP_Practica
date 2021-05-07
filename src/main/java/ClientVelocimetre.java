import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Scanner;

public class ClientVelocimetre {

    private MulticastSocket socket;
    private InetAddress multicastIP;
    private InetSocketAddress groupMulticast;
    private NetworkInterface netIf;

    public ClientVelocimetre(int port, String ip) throws IOException {
        this.socket = new MulticastSocket(port);
        this.multicastIP = InetAddress.getByName(ip);
        this.groupMulticast = new InetSocketAddress(multicastIP,port);
        this.netIf = NetworkInterface.getByName("wlp0s20f3");
    }


    public void RunClient() throws IOException {

        socket.joinGroup(groupMulticast, netIf);
        DatagramPacket packet;

        byte [] receivedData = new byte[1024];

        int media = 0;
        float resultat;
        int count = 0;

        boolean continueRunning = true;

        while(continueRunning){

            packet = new DatagramPacket(receivedData, 1024);
            socket.setSoTimeout(5000);

            try{
                socket.receive(packet);
                media = media + ByteBuffer.wrap(packet.getData()).getInt();
                System.out.println(ByteBuffer.wrap(packet.getData()).getInt());
                count++;
                if(count == 5){
                    count = 0;
                    resultat = (float) media/5;
                    System.out.println("MITJANA: " + resultat);
                    media = 0;
                }
            }catch (SocketTimeoutException e){
                System.out.println(e.getMessage());
                continueRunning = false;

            }
        }
    }

    public static void main(String[] args) {
        try{
            ClientVelocimetre clientVelocimetre = new ClientVelocimetre(5557, "224.0.22.116");
            clientVelocimetre.RunClient();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

}
