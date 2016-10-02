package client;

import static java.lang.System.exit;

/**
 * The client class to send the message to the server
 * Simulate N users
 * Created by cdemet08 on 9/23/16.
 */
public class Client {

	/**
	 * N users to send request to the server
	 */
	private static int N = 5;

	/**
	 * the ip address to connect to the server
 	 */
	private static String ipAddress;

	/**
	 * the port to connect to the server
	 */
   private static int port=0;


	/**
	 * the main function to run the client
	 * @param args
	 */
   public static void main(String[] args) {

      //	check input parameter
      checkInputParameter(args);

      //	port number initiliaze
      ipAddress  = args[0];

      //
      port = Integer.parseInt(args[1]);

      System.out.println("The server ip address is:" + ipAddress + " and port:" + port);

		int i = 1;
		while (i <= N) {

			try {

				Thread t = new Thread(new UserClientThread(ipAddress,port,i));
				t.start();

			} catch (Exception e) {
				System.err.println("Error in connection attempt.");
			}

			i++;
		}



   }


	/**
	 * check parameter from the user
	 * @param args
	 */
	private static void checkInputParameter(String[] args) {
      if(args.length < 2) {
         System.err.println("Give the port number and the ip address.");
         exit(1);
      }
   }


}
