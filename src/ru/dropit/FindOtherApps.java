package ru.dropit;

import ru.dropit.FindApps.GetClients;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;


public class FindOtherApps {


  /*  public static Map<InetAddress, String> listClients(){
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Future<Map<InetAddress,String>> clients = executor.submit(new GetClients());
        Map<InetAddress, String> list = new HashMap<InetAddress, String>();
        Logger logger = Logger.getLogger("DropItLog");
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
            logger.info("Error");
        }
        return list;
    }
*/
}
