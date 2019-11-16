Oliver Altergott, 16/11/2019

Swrve Coding Assessment written in Java as part of the interview process for an internship role.


Test Instructions:
Write a command line tool in Java, ruby or python that takes one parameter which is an 
URL to a remote server. 

 
● The program must download this gzipped CSV file
  
● The file will contain data about each user with various bits of information 
○ user_id 
○ date_joined 
○ spend (in dollars) 
○ milliseconds played 
○ device_height - pixels 
○ device_width - pixels 

● Your program should then output to standard out the following with new line separators 
○ Total count of all users 
○ number of users with a device resolution of 640x960 (width x height) 
○ total spend of all users in dollars 
○ user_id of the first user who joined 
● Your program should have sufficient unit tests, with a good coverage. Feel free to use 
whatever unit testing framework you like or no framework. 
● Your program should treat all data it receives as untrustworthy and unsanitized. 
● Your program should run in a timely manner. 
● Your program should handle common failure cases (returning a non-zero exit status). 
● Your program should handle very large amounts of data (potentially on the order of 
billions of records) in an efficient manner. 



