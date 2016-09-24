package client;

import com.sun.jna.platform.win32.Netapi32Util;
import server.ConnectionThread;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import static java.lang.System.exit;

/**
 * Created by cdemet08 on 9/23/16.
 */
public class Client {


	private static int N = 1;

	// config variable
   private static String ipAddress;
   static int port=0;

   public static void main(String[] args) {

      //	check input parameter
      checkInputParameter(args);

      //	port number initiliaze
      ipAddress  = args[0];

      //
      port = Integer.parseInt(args[1]);

      System.out.println("The server ip address is:" + ipAddress + " and port:" + port);

		int i = 0;
		while (i < N) {

			try {

				Thread t = new Thread(new UserClientThread(ipAddress,port));
				t.start();

			} catch (Exception e) {
				System.err.println("Error in connection attempt.");
			}

			i++;
		}



   }


   private static void checkInputParameter(String[] args) {
      if(args.length < 2) {
         System.err.println("Give the port number and the ip address.");
         exit(1);
      }
   }


}
