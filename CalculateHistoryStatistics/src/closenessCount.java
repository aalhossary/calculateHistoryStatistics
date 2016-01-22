import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.Vector;

import hossary.Utilities;
import hossary.Utilities.Atom;


public class closenessCount {
	private static final int STEPS = 500;
	long count = 0;
	Vector<Vector<Long>> threadsCount;
	PrintStream out;
	double cutOff= Math.pow(5, 2);
	long[][] sums = new long[64][64];
	Vector<Long[]> historySums= new Vector<Long[]>();
	static Vector<Vector<Atom>> allFilesVector= new Vector<Vector<Atom>>();
	
	public closenessCount(String folder) {
		try {
			allFilesVector = getFiles(folder);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("finished reading");
//		countAccordingToNafisa();
//		countUsing2DArray();
		countUsingVectorOfArray();
		System.out.println("finished counting");
//		writeResultFromArray();
		writeResultFromVectorOfArrayOfLongs();
		System.out.println("finished writing");

	}




	public void countUsing2DArray() {
		for (int i = 0; i< allFilesVector.size()-1; i++){
			System.out.println("counting "+i);
			Vector<Atom> firstVAtom = allFilesVector.get(i);
			for (int j = i+1; j<allFilesVector.size(); j++){
				Vector<Atom> secondVAtom = allFilesVector.get(j);
				count=0;
				for (int k = 0; k < firstVAtom.size(); k++){
					Atom firstAtom = firstVAtom.get(k);
					for(int l=0; l< secondVAtom.size(); l++){
						Atom secondAtom = secondVAtom.get(l);
						if(areNear(firstAtom, secondAtom)){
							count++;
						}
					}
				}
				//save the count
				sums[i][j]=count;
				sums[j][i]=count;
				sums[i][i]=firstVAtom.size()*secondVAtom.size();
			}
		}
		sums[63][63]=allFilesVector.get(63).size()*allFilesVector.get(63).size();
	}
	public void countUsingVectorOfArray() {
		//find max length
		int maxLength=0;
		for (Vector<Atom> vector : allFilesVector) {
			if (maxLength < vector.size()) {
				maxLength=vector.size();
			}
		}
		
		//prepare vector for usage
		for (int i = 0; i < maxLength; i+=STEPS) {
			historySums.addElement(new Long[64]);
		}
		
		//now count and fill
		//I'll start with K, to have early results
		for (int k = 0; k < maxLength; k+=STEPS){
			Long[] counts= historySums.elementAt(k/STEPS);
			for (int i = 0; i< allFilesVector.size(); i++){
				System.out.println("counting "+i);
				count=0;
				Vector<Atom> firstVAtom = allFilesVector.get(i);
				for (int j = 0; j<allFilesVector.size(); j++){
					if (i == j) {
						//TODO set to maximum
//						sums[i][i]=firstVAtom.size()*secondVAtom.size();
						continue;
					}
					Vector<Atom> secondVAtom = allFilesVector.get(j);
					if (k >= firstVAtom.size())
						continue;
					for(int k1= k; k1 < k+STEPS;k1++){
						if (k1 >= firstVAtom.size())
							continue;
						Atom firstAtom = firstVAtom.get(k1);
						for(int l=0; l< k1; l++){
							if(l >= secondVAtom.size())
								continue;
							Atom secondAtom = secondVAtom.get(l);
							if(areNear(firstAtom, secondAtom)){
								count++;
							}
						}
					}
				}
				//save the count
				counts[i]=count;
			}
			//output (extra)
			System.out.println("output step"+ k);
			for (Long[] longs : historySums) {
				for (Long l : longs) {
					System.out.print(l);
					System.out.print('\t');
				}
				System.out.println();
			}
		}
	}

	public boolean areNear(Atom firstAtom, Atom secondAtom) {
		double x2 = firstAtom.x - secondAtom.x;
		double y2 = firstAtom.y - secondAtom.y;
		double z2 = firstAtom.z - secondAtom.z;
		double xyz2 = x2*x2+y2*y2+z2*z2;
		return xyz2 <= cutOff;
	}


	public static Vector<Vector<Atom>> getFiles(String folderName) throws FileNotFoundException{
		File folder ;
		folder = new File(folderName);
		String[] filesNames = folder.list();
		//System.out.println(filesNames.toString());
		for (int i = 0; i < filesNames.length; i++) {
			System.out.println("reading "+i);
			Vector<Atom> atomsVector= getAtoms(new File(folder, filesNames[i]));
			allFilesVector.add(atomsVector);
		}
		return allFilesVector;

	}
	public static Vector<Atom> getAtoms(File file){
		Vector<Atom> atoms = new Vector<Atom>();
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
//			Atom atom = new Atom();
//			//System.out.println("inside while printing line " + line );
//			atom.x = Double.parseDouble (line.substring (30, 38).trim());
//			//System.out.println(atom.x);
//			atom.y = Double.parseDouble (line.substring (38, 46).trim());
//			//System.out.println(atom.y);
//			atom.z = Double.parseDouble (line.substring (46, 54).trim());
//			//System.out.println(atom.z);
////			System.out.println("adding atom");
			Atom atom = Utilities.pdb_ATOM_Handler(line);
			atoms.addElement(atom);
			//System.out.println(atoms.toString());
		}
		return atoms;
	}


//	public void writeResultFromArray(){
//		try {
//			out= new PrintStream("ThreadsClosenessCount.txt");
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//		
//		//write header
//		for(int i = 0; i<64; i++){
//			out.print(" Thread" + (i+1));
//			if (i<63){
//				out.print('\t');
//			}			
//		}
//		
//		//write results
//		for(int i=0; i<64; i++){
//			for(int j=0; j< 64; j++){
//				System.out.print(sums[i][j]);
//				if (j< 63) {
//					System.out.print('\t');	
//				}else {
//					System.out.print('\n');
//				}
//			}
//		}
//		
//	}
	public void writeResultFromVectorOfArrayOfLongs(){
		try {
			out= new PrintStream("ThreadsClosenessCount.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		//write header
		for(int i = 0; i<64; i++){
			out.print("Thread " + (i+1));
			out.print((i < 63) ? '\t' : '\n');
		}

		//write results
		for (Long[] longs : historySums) {
			for (int i = 0; i < longs.length; i++) {
				Long l = longs[i];
				out.print(l);
				out.print((i < 63) ? '\t' : '\n');
			}
		}
	}

	public static void main(String[] args) {
//	String folder = "D:\\Amr\\Qvina02_project\\statistical_analysis for paper\\EL1000_test";
		String folder = Utilities.getParameter(args, "-folder", ".", null);
//		closenessCount cc = 
		new closenessCount(folder);
	}

}
