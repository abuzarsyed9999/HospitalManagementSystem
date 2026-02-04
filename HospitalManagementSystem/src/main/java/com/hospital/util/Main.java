package com.hospital.util;

import java.sql.Connection;

public class Main {
	public static void main(String args[])
	{
		Connection conn=DBConnection.getConnection();
		if(conn!=null)
		{
			System.out.println("connnection succesffuly created ");
		}
		else 
		{
			System.out.println("connection failed ");
		}
	}
}
