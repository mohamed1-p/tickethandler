package com.tickethandler.dto;


import java.util.LinkedList;
import java.util.List;

import lombok.Data;

@Data
public class CompanyDto {

	private int company_id;
	private String company_name;
	private List<String> products = new LinkedList<>();
}
