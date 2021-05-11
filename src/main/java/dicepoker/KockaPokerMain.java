package dicepoker;

public class KockaPokerMain {

	public static void main(String[] args) {
		
		JatekMenet jMenet = new JatekMenet();

        jMenet.emberJatekosSzam = jMenet.emberJatekosSzamMeghat();

        jMenet.jatekosokLetrehozasa();

        jMenet.fordulokLejatszasa();

        jMenet.rangsorMeghat();

	}

}
