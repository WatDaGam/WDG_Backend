package com.wdg.wdgbackend.model.entity;

public class Block {
	private long writerId;
	private String writerName;

	public Block(long writerId, String writerName) {
		this.writerId = writerId;
		this.writerName = writerName;
	}

	public long getWriterId() {
		return writerId;
	}

	public void setWriterId(long writerId) {
		this.writerId = writerId;
	}

	public String getWriterName() {
		return writerName;
	}

	public void setWriterName(String writerName) {
		this.writerName = writerName;
	}
}
