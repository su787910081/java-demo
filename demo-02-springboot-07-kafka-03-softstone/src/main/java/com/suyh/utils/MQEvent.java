package com.suyh.utils;

/**
 * 
 * 消息队列的统一消息对象类.
 * 
 * @author whsonga
 * @date  2020-03-31
 *
 */
public class MQEvent<T> {

	public MQEvent() {
	}

	public MQEvent(String eventId, String eventType) {
		this.eventId = eventId;
		this.eventType = eventType;
	}

	public MQEvent(String eventId, String eventType, T data) {
		this.eventId = eventId;
		this.eventType = eventType;
		this.data = data;
	}
	
	/**
	 * 消息id，每条消息唯一标识, 可以采用uuid等
	 */
	private String eventId;  
	
	/**
	 * 消息类型，用于共享性topic区分不同业务
	 */
	private String eventType; 	
	
	/**
	 * 消息业务对象
	 */
	private T data;
	
	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	@Override
	public String toString() {
		return "MQEvent{" +
				"eventId='" + eventId + '\'' +
				", eventType='" + eventType + '\'' +
				", data=" + data +
				'}';
	}
}
