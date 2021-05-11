package dicepoker;

import java.util.Scanner;
import java.util.Random;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;

class JatekMenet {

	byte  emberJatekosSzam;
	private final byte 	  OSSZES_JATEKOS_SZAM	= 4;
	private Scanner 	  input 			  	= new Scanner(System.in);
	private List<Jatekos> jatekosList 		  	= new ArrayList<Jatekos>(OSSZES_JATEKOS_SZAM);

	private Comparator<Jatekos> nyertJatekokSzamaOrder = new Comparator<Jatekos>() {
		public int compare(Jatekos j1, Jatekos j2) {
			return ((Integer)j2.nyertJatekokSzama).compareTo(j1.nyertJatekokSzama);
		}
	};

	private Comparator<Jatekos> dobasErtekOrder = new Comparator<Jatekos>() {
		public int compare(Jatekos j1, Jatekos j2) {
			return ((Integer)j1.dobasErtek).compareTo(j2.dobasErtek);
		}
	};

	byte emberJatekosSzamMeghat() {
		byte jatekosokSzama;

		System.out.print("Jatekosok szama(1-4): ");

		while (true) {
			jatekosokSzama = input.nextByte();

			if (jatekosokSzama > 0 && jatekosokSzama <= 4) {
				break;
			} else {
				System.out.println("Minimum 1, maximum 4 jatekos");
			}

			System.out.println();
		}

		return jatekosokSzama;
	}

	void jatekosokLetrehozasa() {
		String 	nevString;
		String 	nemString;
		int 	botSzam = 1;

		System.out.println();

		for (int i = 0; i < OSSZES_JATEKOS_SZAM; i++) {
			if (emberJatekosSzam > i) {
				System.out.printf("%d. jatekos neve: ",  i + 1);
				nevString = input.next();

				while(true) {
					System.out.printf("%d. jatekos neme: ", i + 1);
					nemString = input.next();

					if (nemString.equalsIgnoreCase("no")) {
						jatekosList.add(new No(nevString));
						break;
					} else if (nemString.equalsIgnoreCase("ferfi")) {
						jatekosList.add(new Ferfi(nevString));
						break;
					} else {
						System.out.println("No vagy Ferfi!");
					}
				}

			} else {
				jatekosList.add(new Bot("Bot" + botSzam++));
			}
		}

		System.out.println();
		input.nextLine();

		//Saját extra feladat
		for (Jatekos i : jatekosList) {
			System.out.printf("%s:\t",i.nev);

			if (i.nem.equals("Ferfi")) {
				((Ferfi)i).mecsetNezek();
			} else if (i.nev.equals("Bot1") || i.nev.equals("Bot2") || i.nev.equals("Bot3")) {
				((Bot)i).botVagyok();
			} else {
				((No)i).vasarolniMentem();
			}
		}

		System.out.println();
	}

	void fordulokLejatszasa() {
		Random 	rand 	= 	new Random();
		int 	dobas;

		System.out.println("A jatek elkezdodott!\n");

		for (int k = 1; k <= 10; k++) {
			System.out.printf("%d. fordulo:\n", k);

			for (Jatekos i : jatekosList){
				if (i.jatekbanVan == true ) {
					System.out.printf("%s:\t", i.nev);

					Collections.fill(i.dobasok, 0);

					for (int j = 0; j < 5; j++) {
						dobas = rand.nextInt(6)+1;
						System.out.printf("%d\t", dobas);
						i.dobasok.set(dobas-1, i.dobasok.get(dobas-1) + 1);
					}

					System.out.println();
				}
			}

			System.out.printf("\nA fordulot %s nyerte.\n", jatekosList.get(forduloNyertesIndex()).nev);
			++jatekosList.get(forduloNyertesIndex()).nyertJatekokSzama;

			//Extra feladat
			if (k == 5 || k == 8) {
				int minNyertJatekokSzama = 10;
				Jatekos utolso = null;
				boolean egyVan = false;

				for (Jatekos i : jatekosList) {
					if (i.jatekbanVan && i.nyertJatekokSzama < minNyertJatekokSzama) {
						utolso = i;
						minNyertJatekokSzama = i.nyertJatekokSzama;
						egyVan = true;
					} else if (i.jatekbanVan && i.nyertJatekokSzama == minNyertJatekokSzama) {
						egyVan = false;
					}
				}

				if (egyVan && utolso != null) {
					System.out.printf("\n%s nem folytathatja tovabb a jatekot!\n", utolso.nev);
					utolso.jatekbanVan = false;
				}
			}

			System.out.println();

			if (k <= 9) {
				System.out.println("Nyomja meg az ENTER-t a folytatashoz!");
				input.nextLine();
			}
		}
		input.close();
	}

	int forduloNyertesIndex(){
		int egySzambolLegtobb;

		for (Jatekos i : jatekosList) {
			i.dobasErtek = 0;

			if (i.jatekbanVan == true ) {
				egySzambolLegtobb = Collections.max(i.dobasok);

				switch (egySzambolLegtobb){
					case 5:
						i.dobasErtek = 8000 + i.dobasok.indexOf(egySzambolLegtobb) * 100 + sum(i.dobasok); //nagypóker
						break;

					case 4:
						i.dobasErtek = 7000 + i.dobasok.indexOf(egySzambolLegtobb) * 100 + sum(i.dobasok); //kispóker
						break;

					case 3:
						if (i.dobasok.contains(2)) {
							i.dobasErtek = 6000 + i.dobasok.indexOf(egySzambolLegtobb) * 100 + sum(i.dobasok); //full
						} else {
							i.dobasErtek = 2000 + i.dobasok.lastIndexOf(egySzambolLegtobb) * 100 + sum(i.dobasok); //terc
						}
						break;

					case 2:
						if (i.dobasok.indexOf(2) != i.dobasok.lastIndexOf(2)) {
							i.dobasErtek = 3000 + i.dobasok.indexOf(egySzambolLegtobb) * 100 + sum(i.dobasok); //két pár
						} else {
							i.dobasErtek = 1000 + i.dobasok.indexOf(egySzambolLegtobb) * 100 + sum(i.dobasok); //pár
						}
						break;

					default:
						if (i.dobasok.get(1) != 0 && i.dobasok.get(2) != 0 &&
								i.dobasok.get(3) != 0 && i.dobasok.get(4) != 0) {

							if (i.dobasok.get(0) == 1) {
								i.dobasErtek = 4000; //kissor
							} else {
								i.dobasErtek = 5000; //nagysor
							}

						} else {
							i.dobasErtek = sum(i.dobasok);
						}
				}
			}
		}

		return jatekosList.indexOf(Collections.max(jatekosList, dobasErtekOrder));
	}

	int sum(List<Integer> dobasok) {
		int result = 0;

		for (int i : dobasok) {
			result += i * dobasok.indexOf(i)+1;
		}

		return result;
	}

	void rangsorMeghat() {

		jatekosList.sort(nyertJatekokSzamaOrder);

		int j = 1;

		for (int i = 0; i < OSSZES_JATEKOS_SZAM; i++) {
			System.out.printf("%d. helyezett:\t%s\t%d\n", j, jatekosList.get(i).nev, jatekosList.get(i).nyertJatekokSzama);
			if (i < 3) {
				if (jatekosList.get(i).nyertJatekokSzama != jatekosList.get(i + 1).nyertJatekokSzama) {
					++j;
				}
			}
		}
	}

}
