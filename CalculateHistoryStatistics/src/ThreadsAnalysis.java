

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.Vector;



public class ThreadsAnalysis {

	Vector<Vector<checkStatus>> allThreads= new Vector<Vector<checkStatus>>();
	PrintStream out;

	public ThreadsAnalysis(String folder){	
		readFiles(folder);
		writeAnalysis();
	}

	// make class for the status of every check
	public static class checkStatus{
		boolean firstCheck;
		boolean secondCheck;
		public checkStatus(boolean firstCheck, boolean secondCheck){
			this.firstCheck = firstCheck;
			this.secondCheck=secondCheck;	
		}		
	}

	// read files from folder
	public void readFiles(String folder){
		for (int i=0; i<64; i++){ 
			System.out.println("reading file #"+(i+1));
			Vector<checkStatus> thread = new Vector<checkStatus>();
			allThreads.addElement(thread);
			Scanner scanner = null;
			try {
				scanner = new Scanner(new File(folder,"history_" + i+ "_small.txt"));
			} catch (FileNotFoundException e) {
				System.out.println(e.getMessage());
				//e.printStackTrace();
			}
			
			
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();

				// check letter and update check status accordingly
				char letter = line.charAt(21);
				switch (letter) {
				case 'G':
					thread.addElement(new checkStatus(true,false));
					break;

				case 'g':
					thread.addElement(new checkStatus(false,false));
					break;

				case 'N':
					thread.lastElement().secondCheck=true;
					break;
					
				default:
					break;
				}
			}
			scanner.close();
		}
	}

	//write interpretation
	public void writeAnalysis(){
		try {
			out= new PrintStream("ThreadAnalysis.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		//write header
		for(int i = 0; i<64; i++){
			for (int j = 0; j < 1; j++) {
				out.print((i+1)+" G Success");
				out.print('\t');
			}
			for (int j = 0; j < 1; j++) {
				out.print((i+1)+" N Success");	
				if (i<63){
					out.print('\t');
				}
			}
		}
		out.println();
		
		// write results
		int maxLength=0;
		for(Vector<checkStatus> vector : allThreads){
			if (maxLength< vector.size()){
				maxLength=vector.size();
			}	
		}

		for (int i = 0; i < maxLength; i++) {
			for (int j = 0; j < 64; j++) {
				Vector<checkStatus> th = allThreads.elementAt(j);
				if (th.size() > i) {
					//out.print(j);
					boolean fCheck = th.elementAt(i).firstCheck;
					out.print(fCheck ? "1" : "0");
				}
				out.print('\t');
			if (th.size() >i){
					boolean sCheck = th.elementAt(i).secondCheck;
					out.print(sCheck ? "1" : "0");
				}
				if (j<63){
					out.print('\t');
				}
				
			}
			out.println();
		}
	/*	
		for (int i = 0; i < maxLength; i++) {
			for (int j = 0; j < 64; j++) {
				Vector<checkStatus> th = allThreads.elementAt(j);
				if (th.size() > i) {
					//					out.print(j);
					boolean fCheck = th.elementAt(i).firstCheck;
					out.print(fCheck ? "1" : "0");
					out.print('\t');
					boolean sCheck = th.elementAt(i).secondCheck;
					out.print(sCheck ? "1" : "0");
				}else{
					out.print("\t");
				}
				if (j<63){
					out.print('\t');
				}
				out.println();
			}
		}*/

	}
	// main to run
	public static void main (String[] args){
		String folder = "D:\\Amr\\Qvina02_project\\Statistical_analysis_for_paper";
	//	System.out.println("testing testing testing");
	//	System.out.println(folder);
		new ThreadsAnalysis(folder);
	}
}
