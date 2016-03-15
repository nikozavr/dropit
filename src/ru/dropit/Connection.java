package ru.dropit;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;


class GetClients implements Callable<Map<InetAddress, String>>{
    GetClients(){

    }

    @Override
    public Map<InetAddress, String> call() {
        Map<InetAddress, String> client = new HashMap<InetAddress, String>();

        try{

            DatagramSocket socket = new DatagramSocket(4000,InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);
            socket.setSoTimeout(5000);
            System.out.println("==Listen is started==");
            System.out.println("==On " + socket.getInetAddress().getLocalHost().toString() + "==");
            byte[] buf = new byte[256];
            while(!Thread.interrupted()) {
                try {
                    Arrays.fill(buf, (byte) 0);
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    InetAddress send_addr = packet.getAddress();
                    System.out.println("--" + send_addr + "--");
                    System.out.println("--" + packet.getData().toString() + "--");
                    client.put(packet.getAddress(), packet.getData().toString());
                } catch (SocketTimeoutException e){
                    System.out.println("Error: Socket timeout");
                    break;
                }
            }
            socket.close();
            System.out.println("==Listen is over==");
        } catch (Exception e){
            e.printStackTrace();
        }
        return client;
    }
}

class CallClients extends Thread {
    @Override
    public void run(){
        try{
            System.out.println("====Send is started====");
            DatagramSocket socket = new DatagramSocket(4001);
            socket.setBroadcast(true);
            byte[] buf = InetAddress.getLocalHost().getHostName().toString().getBytes();
            InetAddress address = InetAddress.getByName("192.168.255.255");
            DatagramPacket data = new DatagramPacket(buf, buf.length, address, 4000);
            socket.send(data);
            socket.close();
            System.out.println("====Send is over====");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

public class Connection {
    static CallClients callAll;

    public static Map<InetAddress, String> listClients(){
        ExecutorService executor = Executors.newFixedThreadPool(1);
        GetClients getClients = new GetClients();
        Future<Map<InetAddress,String>> clients = executor.submit(getClients);
        Map<InetAddress, String> list = new HashMap<InetAddress, String>();
        try {
            list = clients.get();
        } catch (Exception e){
            e.printStackTrace();
        }
        clients.cancel(true);
        //this method will stop the running underlying task
        try {
            for (InetAddress key : list.keySet()) {
                System.out.println(key);
            }
        } catch (Exception e){
            System.out.println("Error");
        }
        return list;
    }

    static void connect(){
        ExecutorService executor = Executors.newFixedThreadPool(1);
        GetClients getClients = new GetClients();
        Future<Map<InetAddress,String>> clients = executor.submit(getClients);

        callAll = new CallClients();
        callAll.start();
    }

}
