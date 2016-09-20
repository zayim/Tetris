import java.util.Timer;
import java.util.TimerTask;

/**
 * Klasa koja modelira konzolno okruzenje u kojem ce se igrati igrica. Ideja je da figura spada prema dole svake sekunde, ali da
 * i korisnik svojim komandama moze mijenjati poziciju figure, kao i rotirati je. Igrac na pocetku vidi listu komandi a kasnije ih unosi
 * kao cijele brojeve.
 * @author Nadin Zajimovic
 *
 */
public class KonzolnoOkruzenje extends TimerTask {
	PlocaZaIgru ploca;
	UlazniTok ulaz;
	Timer timer;
	String plocaString;
	boolean gotova;
	boolean pauzirana;
	
	/**
	 * Konstruktor koji stvara novu plocu za igru, kreira timer koji ce nam koristiti za pravilno padanje figure prema dole te ispisuje
	 * pozdrav na konzolu i spisak komandi.
	 */
	public KonzolnoOkruzenje() {
		int level=1, visina=20, sirina=10;
		String komanda = new String();
		ulaz = new UlazniTok();
		timer = new Timer();
		System.out.println("Dobrodosli u TETRIS 2012!");
		System.out.println("Igricu isprogramirao: Nadin Zajimovic");
		System.out
				.println("Komande:\n5 -> rotiraj\n1 -> pomjeri lijevo\n3 -> pomjeri dole\n2 -> pomjeri desno\n7 -> pauziraj\n9 -> kraj");
		System.out.println("Trenutne postavke:\nLEVEL:        1\nVISINA POLJA: 20\nSIRINA POLJA: 10");
		System.out.println("Pritisnite ENTER da zapocnete igricu s tim postavkama!");
		System.out.println("Ukoliko zelite promijeniti postavke, unesite 'settings'...");
		komanda = ulaz.unesiString();
		if (komanda != null && komanda.equalsIgnoreCase("settings")){
			System.out.print("LEVEL:        "); level=ulaz.unesiInt(); if (level<1 || level>3) level=1;
			System.out.print("VISINA POLJA: "); visina=ulaz.unesiInt(); if (visina<1) visina=1;
			System.out.print("SIRINA POLJA: "); sirina=ulaz.unesiInt(); if (visina<1) visina=1;
			System.out.println("Pritisnite ENTER da zapocnete igricu!");
			ulaz.unesiString();
		}
		ploca = new PlocaZaIgru(level,visina,sirina);
		plocaString = ploca.toString();
		gotova = false;
		pauzirana = false;
	}
	
	/**
	 * Metoda kojom pokrecemo igricu. Postavljamo timer i trazimo od korisnika da unosi komande sve dok se igrica ne zavrsi.
	 * U medjuvremenu updatujemo stanje igrice, rezultat i sve potrebne elemente.
	 */
	public void pokreni() {
		int razmak=1000 - 250*(ploca.vratiLevel()-1);
		timer.schedule(this, razmak, razmak);
		int komanda;
		while (!ploca.daLiJeIgraGotova() && !gotova) {
			plocaString = ploca.toString();
			plocaString = plocaString.replace('0', ' ');
			for (int i = 0; i < 30; i++)
				System.out.println("");
			System.out.println("Rezultat: " + ploca.vratiRezultat() + "\n\n" + plocaString);
			System.out.print("Komanda: ");
			komanda = ulaz.unesiInt();
			switch (komanda) {
			case 5:
				ploca.rotiraj();
				break;
			case 1:
				ploca.pomjeriLijevo();
				break;
			case 3:
				ploca.pomjeriDesno();
				break;
			case 2:
				while (ploca.daLiJeMogucePomjeritiDole())
					ploca.automatskiPomjeriDole();
				ploca.automatskiPomjeriDole();
				break;
			case 7:
				pauzirana = true;
				System.out.print("Pritisni enter za nastavak: ");
				ulaz.unesiString();
				pauzirana = false;
				break;
			case 9:
				if (!gotova) {
					gotova = true;
					ploca.zavrsi();
				}
				break;
			}
		}
		if (!gotova) {
			gotova = true;
			ploca.zavrsi();
		}
	}
	
	/**
	 * Metoda koju pokrece timer u pravilnim razmacima. Poziva se automatsko padanje figure prema dole i updateovanje stanja igrice nakon
	 * padanja. Ukoliko je igra gotova, timer se zaustavlja.
	 */
	public void run() {
		if (!ploca.daLiJeIgraGotova() && !gotova && !pauzirana) {
			ploca.automatskiPomjeriDole();
			plocaString = ploca.toString();
			plocaString = plocaString.replace('0', ' ');
			for (int i = 0; i < 30; i++)
				System.out.println("");
			System.out.println("Rezultat: " + ploca.vratiRezultat() + "\n\n" + plocaString);
			System.out.print("Komanda: ");
		} else if (!pauzirana){
			timer.cancel();
			if (!gotova) {
				gotova = true;
				ploca.zavrsi();
			}
		}
	}

}
