import java.io.Serializable;

/**
 * Created by oforos on 9/27/16.
 */
public class MessageObject implements Serializable {

	private static final long serialVersionUID = 5950169519310163575L;

	private int idClient = 0;

	private String clientMsg = new String();

	private String serverMsg = new String();

	private String clientIP_Port = new String();

	private byte[] payloadServer = null;


	public void setPayloadServer(byte[] payloadServer){
		this.payloadServer = payloadServer;
	}

	public byte[] getPayloadServer(){
		return this.payloadServer;
	}

	public void setClientIP_Port(String clientIP_Port){
		this.clientIP_Port = clientIP_Port;
	}

	public String getClientIP_Port(){
		return this.clientIP_Port;
	}

	public String getServerMsg(){
		return this.serverMsg;
	}

	public void setServerMsg(String serverMsg){
		this.serverMsg = serverMsg;
	}


	public void setClientMsg(String clientMsg){
		this.clientMsg = clientMsg;
	}

	public String getClientMsg(){
		return this.clientMsg;
	}

	public void setIdClient(int idClient){
		this.idClient = idClient;
	}

	public int getIdClient(){
		return this.idClient;
	}


}
