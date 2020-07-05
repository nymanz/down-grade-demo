package com.sinbad.demo.pojo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class DemoPojo implements Serializable {


	private Long id;
	private String name;
	private Date birthday;

	private int test;


}
