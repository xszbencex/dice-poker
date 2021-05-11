package dicepoker;

import java.util.List;
import java.util.ArrayList;

class Jatekos {   
	protected 	String 	nev;
    protected	String	nem;
    int 				nyertJatekokSzama;
    boolean				jatekbanVan = true;
    int					dobasErtek;
    List<Integer> 		dobasok = new ArrayList<Integer>(6);	//nem a konkret szamot tartalmazza, amit dobott, hanem, hogy melyik szambol (index + 1-nek megfeleloen) mennyit
    
    Jatekos() {
    	for (int i = 0; i < 6; i++)
    		dobasok.add(0);
    }
}


class No extends Jatekos { 
	
    No(String snev) {
        this.nev = snev;
        this.nem = "No";
    }

    void vasarolniMentem() {
        System.out.println("Vasarolni mentem");
    }
}


class Ferfi extends Jatekos {
	
    Ferfi(String snev) {
        this.nev = snev;
        this.nem = "Ferfi";
    }

    void mecsetNezek() {
        System.out.println("Hajra magyarok!");
    }
}


class Bot extends Jatekos {
	
    Bot(String snev) {
        this.nev = snev;
        this.nem = "No";
    }

    void botVagyok() {
        System.out.println("Robot vagyok!");
    }
}
