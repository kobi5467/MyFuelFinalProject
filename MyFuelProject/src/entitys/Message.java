package entitys;

import enums.MessageType;

public class Message {

	private MessageType messageType;
	private Object messageData;
	
	public Message(MessageType messageType, Object messageData) {
		this.messageType = messageType;
		this.messageData = messageData;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public Object getMessageData() {
		return messageData;
	}

	public void setMessageData(Object messageData) {
		this.messageData = messageData;
	}

	@Override
	public String toString() {
		return "Message [messageType=" + messageType + ", messageData="
				+ messageData + "]";
	}
	
}
