import java.lang.*;
import java.io.*;

public class Student {
	private int roll;
	private String name;
	private int marks;
 
 	// default constructor
	public Student(){
		roll = 20;
		name = "Lorem Ipsum"
		marks = 88;
	}
	
	public static void main(String[] args){
		Student student = new Student();
		/*
			print details:
			roll no, marks
		*/
		System.out.println("roll no:"+student.roll+"\nMarks:"+student.marks);
		
	}
}
