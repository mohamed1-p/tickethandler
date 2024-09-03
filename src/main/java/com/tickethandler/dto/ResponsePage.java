package com.tickethandler.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePage<T> {

	private List<T> content;
	private int page;
	private int size;
	private long totalElements;
	private int totalpages;
	private boolean Last;
	
	

}
