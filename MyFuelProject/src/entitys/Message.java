package entitys;

import java.io.Serializable;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import entitys.enums.MessageType;

public class Message implements Serializable{

	/**
	 * This is the message that will send between client and server.
	 */
	private static final long serialVersionUID = 1L;
	private MessageType messageType;
	private String messageData;
	
	public Message(MessageType messageType, String messageData) {
		this.messageType = messageType;
		this.messageData = messageData;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public String getMessageData() {
		return messageData;
	}

	public void setMessageData(String messageData) {
		this.messageData = messageData;
	}
	
	public JsonObject getMessageAsJsonObject() {
		if(messageData != null && !messageData.equals("")) {
			return new Gson().fromJson(messageData, JsonObject.class);			
		}
		return null;
	}

	@Override
	public String toString() {
		return "Message [messageType=" + messageType + ", messageData="
				+ messageData.toString() + "]";
	}
	
}
