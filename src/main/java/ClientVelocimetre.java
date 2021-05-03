import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Scanner;

public class ClientVelocimetre {


    public ClientVelocimetre() throws IOException {
    }


    public void RunClient() throws IOException {

        MulticastSocket socket = new MulticastSocket(5557);
        InetAddress multicastIP =   InetAddress.getByName("224.0.22.114");
        InetSocketAddress groupMulticast = new InetSocketAddress(multicastIP,5557);;
        NetworkInterface netIf = NetworkInterface.getByName("wlp0s20f3");;
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
            ClientVelocimetre clientVelocimetre = new ClientVelocimetre();
            clientVelocimetre.RunClient();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

}
