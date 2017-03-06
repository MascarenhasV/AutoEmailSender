# AutoEmailSender

This tool sends shuffle assignments to the students of 11-601 class at CMU. All the shuffle assignments are stored as files in a folder and the format of the files should be andrew_id.html. 

The commands to run the code are as follows:

- Linux:
```
javac -cp activation.jar:javax.mail.jar: EmailSender.java
java -cp activation.jar:javax.mail.jar: EmailSender email_id password folder_location
```

- Windows:
```
javac -cp "activation.jar;javax.mail.jar" EmailSender.java
java -cp ".;activation.jar;javax.mail.jar;" EmailSender email_id password folder_location
```
