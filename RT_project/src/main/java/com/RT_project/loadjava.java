package com.RT_project;

import static com.RT_project.AssignS.javafile;
 class loadjava extends Thread{
	 
	 public void run() {
		 try
	        {  
	         // We are running "dir" and "ping" commands and  "mkdir" command to create nwe flie name 'repos'  command on cmd 
		     Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"dir && ping localhost\"\"&&mkdir repos \""); 
			 sleep(500);
	             for(int i=0;i<javafile.length;i++) {
	        	 Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"dir && ping localhost\"\"&&cd repos&&git clone "+javafile[i]+".git \"");
	       		  sleep(1000);
	        	}
	        } 
	        catch (Exception e) 
	        { 
	            System.out.println("HEY Buddy ! U r Doing Something Wrong "); 
	            e.printStackTrace(); 
	        }
		 System.out.println("\n\n\t\t**Complated Download All Repository**");
	 };
	
}
