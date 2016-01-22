import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.Vector;


public class closenessCount {
	int count = 0;
	Vector<Integer> ThreadCount;
	Vector<Vector<Integer>>  threadsCount;
	PrintStream out;
	double cutOff= Math.pow(5, 2);
	static Vector<Vector<Atom>> allFilesVector= new Vector<Vector<Atom>>();
	
	public static class Atom{
		String atomType;
		int id;
		double x, y, z;
	}


	public closenessCount(String folder) {
		try {
			allFilesVector = getFiles(folder);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		for (int i = 0; i< allFilesVector.size()-1; i++){
			Vector<Atom> firstVAtom = allFilesVector.get(i);
			for (int j = i+1; j<allFilesVector.size(); j++){
				Vector<Atom> secondVAtom = allFilesVector.get(j);
				count=0;
				for (int k = 0; k<firstVAtom.size(); k++){
					Atom firstAtom = firstVAtom.get(k);
					for(int l=0; l<secondVAtom.size(); l++){
						Atom secondAtom = firstVAtom.get(l);
						if(areNear(firstAtom, secondAtom)){
							count++;
						}
					}
				}
				//save the count
				ThreadCount.add(count);
				threadsCount.add(ThreadCount);
			}
		}
		writeResult();

	}




	public boolean areNear(Atom firstAtom, Atom secondAtom) {
		double x2 = firstAtom.x - secondAtom.x;
		double y2 = firstAtom.y - secondAtom.y;
		double z2 = firstAtom.z - secondAtom.z;
		double xyz2 = x2*x2+y2*y2+z2*z2;
		boolean proximate = xyz2 <= cutOff;
		return proximate;
	}




	public static Vector<Vector<Atom>> getFiles(String folderName) throws FileNotFoundException{
		File folder ;
		folder = new File(folderName);
		String[] filesNames = folder.list();
		//System.out.println(filesNames.toString());
		for (int i = 0; i < filesNames.length; i++) {
			Vector<Atom> atomsVector= getAtoms(new File(folder, filesNames[i]));
			allFilesVector.add(atomsVector);
		}
		return allFilesVector;

	}
	public static Vector<Atom> getAtoms(File file){
		Vector<Atom> atoms = new Vector<Atom>();
		Atom atom = new Atom();
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			//System.out.println("inside while printing line " + line );
			atom.x = Double.parseDouble (line.substring (30, 38).trim());
			//System.out.println(atom.x);
			atom.y = Double.parseDouble (line.substring (38, 46).trim());
			//System.out.println(atom.y);
			atom.z = Double.parseDouble (line.substring (46, 54).trim());
			//System.out.println(atom.z);
			System.out.println("adding atom");
			atoms.addElement(atom);
			//System.out.println(atoms.toString());
		}
		return atoms;
	}



	public Vector<Vector<Integer>> compareDistance(Vector<Atom> vAtom, int vectorIndex, Vector<Vector<Atom>> filesVector){

	/*	for (int i = 1; i<filesVector.size(); i++){
			for(int j=0; j<vAtom.size(); j++){
				double x2=filesVector.get(i).get(j).x - vAtom.get(j).x;
				double y2 = filesVector.get(i).get(j).y - vAtom.get(j).y;
				double z2 = filesVector.get(i).get(j).z - vAtom.get(j).z;
				double xyz = Math.pow(x2, 2)+Math.pow(y2, 2)+Math.pow(z2, 2);
				if(xyz<= cutOff){
					count++;
				}


			}
		}*/
		for(int i=0; i<vAtom.size(); i++){
			for (int j = vectorIndex+1; j<filesVector.size(); j++){
				for (int k = 0; k<filesVector.get(j).size(); k++){
					double x2=filesVector.get(j).get(k).x - vAtom.get(i).x;
					double y2 = filesVector.get(j).get(i).y - vAtom.get(i).y;
					double z2 = filesVector.get(j).get(i).z - vAtom.get(i).z;
					double xyz = Math.pow(x2, 2)+Math.pow(y2, 2)+Math.pow(z2, 2);
					if(xyz<= cutOff){
						count++;
					}
				}
				ThreadCount.add(count);
			}
			threadsCount.add(ThreadCount);
		}
		return threadsCount;
	}

	public void writeResult(){

		try {
			out= new PrintStream("ThreadsClosenessCount.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		//write header
		for(int i = 0; i<64; i++){
			out.print(" Thread" + (i+1));
			if (i<63){
				out.print('\t');
			}			
		}

		//write results
		for(int i=0; i<threadsCount.size(); i++){
			for(int j=0; j<ThreadCount.size(); j++){
				System.out.print(ThreadCount.elementAt(j));
				if (j<ThreadCount.size()-1){
					System.out.print('\t');	
				}else {
					System.out.println('\n');
				}
			}}

	}





	public static void main(String[] args) {
		String folder = "D:\\Amr\\Qvina02_project\\statistical_analysis for paper\\EL1000_test";
		closenessCount cc = new closenessCount(folder);

	}

}
