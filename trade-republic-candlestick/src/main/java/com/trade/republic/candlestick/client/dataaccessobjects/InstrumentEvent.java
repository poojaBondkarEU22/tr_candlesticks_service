package com.trade.republic.candlestick.client.dataaccessobjects;

import org.springframework.data.mongodb.core.mapping.Document;

import com.trade.republic.candlestick.client.datatransferobject.Type;

@Document("tr_m_instruments_event")
public class InstrumentEvent {
	
	private Type type;
	private Instrument data;
	
	public InstrumentEvent() {
		
	}

	public InstrumentEvent(Type type, Instrument data) {
		super();
		this.type = type;
		this.data = data;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Instrument getData() {
		return data;
	}

	public void setData(Instrument data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "InstrumentEvent [type=" + type + ", data=" + data + "]";
	}
	
	
}
