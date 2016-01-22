package hossary;

public abstract class Utilities {
	
	public static class Atom{
		String atomType;
		int id;
		public double x, y, z;
	}

	
	public static String getParameter(String[] args, String parameter, String defaultValueIfParameterFound, String valueIfNotFound) {
		boolean flagFound=false;
		String input= defaultValueIfParameterFound;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals(parameter)) {
				flagFound=true;
				if (i+1 >= args.length) {
					break;
				}
				String nextArg = args[i+1];
				if (nextArg.startsWith("-")) {
					break;
				} else {
					input = nextArg;
				}
				i+=1;
			}
		}
		if (!flagFound) {
			return valueIfNotFound;
		}
		return input;
	}
	
	
	/**
	 Handler for
	 ATOM Record Format
	 *
	 <pre>
        ATOM      1  N   ASP A  15     110.964  24.941  59.191  1.00 83.44           N
	 *
	 COLUMNS        DATA TYPE       FIELD         DEFINITION
	 ---------------------------------------------------------------------------------
	 1 -  6        Record name     "ATOM  "
	 7 - 11        Integer         serial        Atom serial number.
	 13 - 16        Atom            name          Atom name.
	 17             Character       altLoc        Alternate location indicator.
	 18 - 20        Residue name    resName       Residue name.
	 22             Character       chainID       Chain identifier.
	 23 - 26        Integer         resSeq        Residue sequence number.
	 27             AChar           iCode         Code for insertion of residues.
	 31 - 38        Real(8.3)       x             Orthogonal coordinates for X in Angstroms.
	 39 - 46        Real(8.3)       y             Orthogonal coordinates for Y in Angstroms.
	 47 - 54        Real(8.3)       z             Orthogonal coordinates for Z in Angstroms.
	 55 - 60        Real(6.2)       occupancy     Occupancy.
	 61 - 66        Real(6.2)       tempFactor    Temperature factor.
	 73 - 76        LString(4)      segID         Segment identifier, left-justified.
	 77 - 78        LString(2)      element       Element symbol, right-justified.
	 79 - 80        LString(2)      charge        Charge on the atom.
	 </pre>
	 */
	public static Atom pdb_ATOM_Handler(String line) {
		Character iCode = line.substring(26,27).charAt(0);
		if ( iCode == ' ')
			iCode = null;
//		ResidueNumber residueNumber = new ResidueNumber(chain_id, Integer.valueOf(resNum), iCode);

		//recordName      groupCode3
		//|                |    resNum
		//|                |    |   iCode
		//|     |          | |  |   ||
		//ATOM      1  N   ASP A  15     110.964  24.941  59.191  1.00 83.44           N
		//ATOM   1964  N   ARG H 221A      5.963 -16.715  27.669  1.00 28.59           N

//		Character aminoCode1 = null;
//
//		if ( recordName.equals("ATOM") ){
//			aminoCode1 = StructureTools.get1LetterCode(groupCode3);
//		} else {
//			// HETATOM RECORDS are treated slightly differently
//			// some modified amino acids that we want to treat as amino acids
//			// can be found as HETATOM records
//			aminoCode1 = StructureTools.get1LetterCode(groupCode3);
//			if ( aminoCode1 != null)
//				if ( aminoCode1.equals(StructureTools.UNKNOWN_GROUP_LABEL))
//					aminoCode1 = null;
//		}

		//          1         2         3         4         5         6
		//012345678901234567890123456789012345678901234567890123456789
		//ATOM      1  N   MET     1      20.154  29.699   5.276   1.0
		//ATOM    112  CA  ASP   112      41.017  33.527  28.371  1.00  0.00
		//ATOM     53  CA  MET     7      23.772  33.989 -21.600  1.00  0.00           C
		//ATOM    112  CA  ASP   112      37.613  26.621  33.571     0     0


//		String fullname = line.substring (12, 16);

		// create new atom
		Atom atom = new Atom() ;

		atom.x = Double.parseDouble (line.substring (30, 38).trim());
		atom.y = Double.parseDouble (line.substring (38, 46).trim());
		atom.z = Double.parseDouble (line.substring (46, 54).trim());

		if ( line.length() > 77 ) {
			// parse element from element field
			try {
				String element = line.substring (76, 78).trim();
				atom.atomType=element;
			}  catch (IllegalArgumentException e){}
		} 
		return atom;
		//System.out.println("current group: " + current_group);
	}
	

}
