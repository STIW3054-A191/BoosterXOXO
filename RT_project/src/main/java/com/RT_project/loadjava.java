package com.RT_project;

import static com.RT_project.AssignS.javafile;
 class loadjava extends Thread{
	 
	 public void run() {
		 try
	        {  
	         // We are running "dir" and "ping" commands and  "mkdir" command to create nwe flie name 'repos'  command on cmd 
		 Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"dir && ping localhost\"\"&&mkdir repos \""); 
	        	for(int i=0;i<javafile.length;i++) {
	         Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"dir && ping localhost\"\" &&git clone "+javafile[i]+".git \"");
	         sleep(5000);
	        	}
	        } 
	        catch (Exception e) 
	        { 
	            System.out.println("HEY Buddy ! U r Doing Something Wrong "); 
	            e.printStackTrace(); 
	        }
	 };
	
} 
