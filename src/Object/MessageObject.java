package Object;

import java.io.Serializable;

/**
 * The Object to send from client to server and server to client with payload
 * Created by cdemet08 on 9/27/16.
 */
public class MessageObject implements Serializable {

	/**
	 * serialVersion for Serializable the object with Client and Server
	 */
	private static final long serialVersionUID = 15L;

	/**
	 * The id Client
	 */
	private String idClient = new String();

	/**
	 * The client Message to send
	 */
	private String clientMsg = new String();

	/**
	 * The server message to send
	 */
	private String serverMsg = new String();

	/**
	 * The client ip and port
	 */
	private String clientIP_Port = new String();

	/**
	 * the payload in bytes to send from server to client
	 */
	private byte[] payloadServer = new byte[0];


	/**
	 * set the payload
	 * @param payloadServer
	 */
	public void setPayloadServer(byte[] payloadServer){
		this.payloadServer = payloadServer;
	}

	/**
	 * get the payload
	 * @return
	 */
	public byte[] getPayloadServer(){
		return this.payloadServer;
	}

	/**
	 * set the ip and port for the client in string
	 * @param clientIP_Port
	 */
	public void setClientIP_Port(String clientIP_Port){
		this.clientIP_Port = clientIP_Port;
	}

	/**
	 * get the ip and port for the client in string
	 * @return
	 */
	public String getClientIP_Port(){
		return this.clientIP_Port;
	}

	/**
	 * get the server message
	 * @return
	 */
	public String getServerMsg(){
		return this.serverMsg;
	}

	/**
	 * set the server message
	 * @param serverMsg
	 */
	public void setServerMsg(String serverMsg){
		this.serverMsg = serverMsg;
	}


	/**
	 * set the client message
	 * @param clientMsg
	 */
	public void setClientMsg(String clientMsg){
		this.clientMsg = clientMsg;
	}

	/**
	 * get the client message
	 * @return
	 */
	public String getClientMsg(){
		return this.clientMsg;
	}

	/**
	 * set the id client thread
	 * @param idClient
	 */
	public void setIdClient(String idClient){
		this.idClient = idClient;
	}

	/**
	 * get the id client thread
	 * @return
	 */
	public String  getIdClient(){
		return this.idClient;
	}


}
