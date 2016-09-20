/**
 * Klasa koja predstavlja osnovu za igru. Sastoji se iz matrice 20x10, no te
 * dimenzije je moguce lagano promijeniti. Ukoliko je element matrice 0, na
 * njemu se ne nalazi nista, dok ukoliko je neki broj razlicit od nule, na tom
 * mjestu se nalazi kvadratic figure ciji je tip upravo taj broj. Za igru je
 * potrebno znati s kojom figurom trenutno radimo, kao i koja ce figura biti
 * sljedeca ubacena na plocu.
 * 
 * @author Nadin Zajimovic
 * 
 */
public class PlocaZaIgru {
	int[][] matrica;
	Figura trenutna, naredna;
	int rezultat;
	public final int BROJ_MOGUCIH_FIGURA = 6;
	public final int VISINA_POLJA;
	public final int SIRINA_POLJA;
	public final int LEVEL;

	/**
	 * Konstruktor koji generise novu plocu za igru i slucajnim izborom postavi
	 * jednu figuru u vrh polja za igru, i to na sredinu, te slucajno generise
	 * narednu figuru. Takodjer postavlja cijelu matricu na nulu.
	 */
	public PlocaZaIgru(int level, int visina, int sirina) {
		LEVEL = level;
		VISINA_POLJA = visina;
		SIRINA_POLJA = sirina;

		rezultat = 0;
		matrica = new int[VISINA_POLJA + 5][SIRINA_POLJA + 5];
		for (int i = 0; i < VISINA_POLJA; i++)
			for (int j = 0; j < SIRINA_POLJA; j++)
				matrica[i][j] = 0;

		trenutna = new Figura(slucajnaFigura());
		trenutna.postaviNaPlocu(
				SIRINA_POLJA / 2 - trenutna.vratiStranicu() / 2, 0);
		naredna = new Figura(slucajnaFigura());
	}

	/**
	 * Metoda za generisanje slucajne figure.
	 * 
	 * @return Vraca kao "slucajni" cijeli broj u dozvoljenom rasponu figura.
	 */
	private int slucajnaFigura() {
		return 1 + (int) (Math.random() * BROJ_MOGUCIH_FIGURA);
	}

	/**
	 * Metoda koja pomjera trenutnu figuru na ploci jedno mjesto prema dnu.
	 * Naravno to radi ukoliko je moguce pomjeriti trenutnu figuru. Nakon
	 * pomjeranja vrsimo updateovanje matrice, racunanje rezultata i slicnih
	 * stvari.
	 */
	public void pomjeriDole() {
		if (daLiJeMogucePomjeritiDole())
			trenutna.pomjeriDole();
		update();
	}

	/**
	 * Metoda zaduzena da automatski, bez naseg uticaja, pomjera trenutnu figuru
	 * prema dole. Razlika izmezu slicne metode (koja sluzi da bi obradila
	 * igracev zahtjev za pomjeranje prema dole) je ta, ukoliko nije moguce
	 * pomjeriti figuru, ova metoda ce ubaciti novu figuru na plocu.
	 */
	public void automatskiPomjeriDole() {
		if (daLiJeMogucePomjeritiDole())
			trenutna.pomjeriDole();
		else
			ubaciFiguru();
		update();
	}

	/**
	 * Metoda koja vrsi pomjeranje trenutne figure udesno, ukoliko je to moguce,
	 * i refreshuje sve informacije nako pomjeranja.
	 */
	public void pomjeriDesno() {
		if (daLiJeMogucePomjeritiDesno())
			trenutna.pomjeriDesno();
		update();
	}

	/**
	 * Metoda koja vrsi pomjeranje trenutne figure ulijevo, ukoliko je to
	 * moguce, i refreshuje sve informacije nako pomjeranja.
	 */
	public void pomjeriLijevo() {
		if (daLiJeMogucePomjeritiLijevo())
			trenutna.pomjeriLijevo();
		update();
	}

	/**
	 * Metoda koja rotira trenutnu figuru, ukoliko je to moguce i nakon toga
	 * vrsi updateovanje svih neophodnih informacija.
	 */
	public void rotiraj() {
		if (daLiJeMoguceRotirati())
			trenutna.rotiraj();
		update();
	}

	/**
	 * Metoda koja se poziva nakon neko izvrsenog poteza. Zadatak joj je
	 * provjeriti da li je neki red popunjen, i ako jeste da ga izbaci, tj. da
	 * pomjeri sve redove jedno mjesto nadole. Takodjer, updatuje se i rezultat,
	 * ukoliko smo ocistili jedan red.
	 */
	public void update() {
		for (int i = 0; i < VISINA_POLJA; i++) {
			boolean popunjeno = true;
			for (int j = 0; j < SIRINA_POLJA; j++)
				if (matrica[i][j] == 0)
					popunjeno = false;
			if (popunjeno) {
				rezultat += 10 + (LEVEL-1)*5;
				for (int j = i; j > 0; j--) {
					for (int k = 0; k < SIRINA_POLJA; k++) {
						int tmp = matrica[j][k];
						matrica[j][k] = matrica[j - 1][k];
						matrica[j - 1][k] = tmp;
					}
				}
				for (int j = 0; j < SIRINA_POLJA; j++)
					matrica[0][j] = 0;
			}
		}
	}

	/**
	 * Metoda koja ubacuje narednu figuru u polje i slucajno generise novu
	 * narednu. Ovo se desava kada vise nije moguce pomjerati trenutnu figuru pa
	 * nam treba naredna.
	 */
	public void ubaciFiguru() {
		for (int i = 0; i < trenutna.vratiStranicu(); i++)
			for (int j = 0; j < trenutna.vratiStranicu(); j++)
				if (trenutna.elementMatrice(i, j) != 0){
					matrica[trenutna.vratiY() + i][trenutna.vratiX() + j] = trenutna
							.vratiTip();
					rezultat++;
				}

		trenutna = new Figura(naredna);
		naredna = new Figura(slucajnaFigura());
		trenutna.postaviNaPlocu(
				SIRINA_POLJA / 2 - trenutna.vratiStranicu() / 2, 0);
	}

	/**
	 * Analizira svaku kolonu matrice figure, nadje donju granicu u svakoj
	 * koloni, te provjeri da li je moguce pomjeriti tu granicu jedno mjesto
	 * nanize. Ukoliko je moguce pomjeriti svaku kolonu, moguce je pomjeriti i
	 * figuru.
	 * 
	 * @return Vraca logicnu vrijednost true ukoliko je moguce obaviti
	 *         pomjeranje figure nadole, false u suprotnonm.
	 */
	public boolean daLiJeMogucePomjeritiDole() {

		for (int i = 0; i < trenutna.vratiStranicu(); i++) {
			int donjaGranica = trenutna.vratiStranicu() - 1;
			while (trenutna.elementMatrice(donjaGranica, i) == 0
					&& donjaGranica > 0)
				donjaGranica--;
			if (donjaGranica != 0
					|| (donjaGranica == 0 && trenutna.elementMatrice(0, i) != 0)) {
				if (trenutna.vratiY() + donjaGranica + 1 >= VISINA_POLJA
						|| trenutna.vratiY() + donjaGranica + 1 < 0)
					return false;
				else if (matrica[trenutna.vratiY() + donjaGranica + 1][trenutna
						.vratiX() + i] != 0)
					return false;
			}
		}
		return true;
	}

	/**
	 * Analizira svaki red matrice figure, nadje lijevu granicu u svakoj koloni,
	 * te provjeri da li je moguce pomjeriti tu granicu jedno mjesto ulijevo.
	 * Ukoliko je moguce pomjeriti svaki red, moguce je pomjeriti i figuru.
	 * 
	 * @return Vraca logicnu vrijednost true ukoliko je moguce obaviti
	 *         pomjeranje figure ulijevo, false u suprotnonm.
	 */
	public boolean daLiJeMogucePomjeritiLijevo() {
		for (int i = 0; i < trenutna.vratiStranicu(); i++) {
			int lijevaGranica = 0;
			while (trenutna.elementMatrice(i, lijevaGranica) == 0
					&& lijevaGranica < trenutna.vratiStranicu() - 1)
				lijevaGranica++;
			if (trenutna.vratiX() + lijevaGranica <= 0)
				return false;
			else if (matrica[trenutna.vratiY() + i][trenutna.vratiX()
					+ lijevaGranica - 1] != 0)
				return false;
		}
		return true;
	}

	/**
	 * Analizira svaki red matrice figure, nadje desnu granicu u svakoj koloni,
	 * te provjeri da li je moguce pomjeriti tu granicu jedno mjesto udesno.
	 * Ukoliko je moguce pomjeriti svaki red, moguce je pomjeriti i figuru.
	 * 
	 * @return Vraca logicnu vrijednost true ukoliko je moguce obaviti
	 *         pomjeranje figure udesno, false u suprotnonm.
	 */
	public boolean daLiJeMogucePomjeritiDesno() {
		for (int i = 0; i < trenutna.vratiStranicu(); i++) {
			int desnaGranica = trenutna.vratiStranicu() - 1;
			while (trenutna.elementMatrice(i, desnaGranica) == 0
					&& desnaGranica > 0)
				desnaGranica--;
			if (trenutna.vratiX() + desnaGranica >= SIRINA_POLJA - 1)
				return false;
			else if (matrica[trenutna.vratiY() + i][trenutna.vratiX()
					+ desnaGranica + 1] != 0)
				return false;
		}
		return true;
	}

	/**
	 * Napravimo kopiju trenutne figure, rotiramo nju i vidimo da li nesto ne
	 * valja. Ukoliko nije dobro, vratimo false, ukoliko je sve uredu, vratimo
	 * true;
	 * 
	 * @return Vraca logicku vrijednost true ukoliko je rotacija za 90 stepeni u
	 *         smjeru kazaljke moguca, false u suprotnom.
	 */
	public boolean daLiJeMoguceRotirati() {
		Figura privremena = new Figura(trenutna);
		privremena.rotiraj();

		for (int i = 0; i < privremena.vratiStranicu(); i++) {
			for (int j = 0; j < privremena.vratiStranicu(); j++) {
				if (privremena.elementMatrice(i, j) != 0) {
					if (privremena.vratiY() + i >= VISINA_POLJA
							|| privremena.vratiY() + i < 0)
						return false;
					if (privremena.vratiX() + j < 0
							|| privremena.vratiX() + j >= SIRINA_POLJA)
						return false;
					if (matrica[privremena.vratiY() + i][privremena.vratiX()
							+ j] != 0)
						return false;
				}
			}
		}
		return true;
	}

	/**
	 * Metoda koja provjeraje da li je igra gotova. Prolazimo kroz matricu
	 * naredne figure i gledamo da li svaka kockica od figure moze stati na
	 * plocu za igru. Ako bar jedna kockica figure ne moze stati na plocu za
	 * igru, znaci da je igra gotova.
	 * 
	 * @return Vraca vrijednost true ukoliko vise nije moguce stavljati nove
	 *         figure na plocu, false u suprotnom.
	 */
	public boolean daLiJeIgraGotova() {
		if (daLiJeMogucePomjeritiDole())
			return false;
		int pocetakX = SIRINA_POLJA / 2 - naredna.vratiStranicu() / 2;
		boolean trenutnaX, trenutnaY;
		for (int i = 0; i < naredna.vratiStranicu(); i++) {
			for (int j = 0; j < naredna.vratiStranicu(); j++) {
				if (naredna.elementMatrice(i, j) != 0) {
					if (matrica[i][pocetakX + j] != 0)
						return true;
					trenutnaX = pocetakX + j >= trenutna.vratiX()
							&& pocetakX + j < trenutna.vratiX()
									+ trenutna.vratiStranicu();
					trenutnaY = i >= trenutna.vratiY()
							&& i < trenutna.vratiY() + trenutna.vratiStranicu();
					if ((trenutnaX && trenutnaY)
							&& trenutna.elementMatrice(i - trenutna.vratiY(),
									pocetakX + j - trenutna.vratiX()) != 0)
						return true;
				}
			}
		}
		return false;
	}

	/**
	 * Metoda koja matricu pretvara u string, oblik pogodan za ispis u na
	 * konzolni ekran.
	 * 
	 * @return Vraca objekat tipa String koji predstavlja plocu pretvorenu u
	 *         string.
	 */
	public String toString() {
		String granicnik = "*";
		String ispis = new String();
		ispis += "Naredna figura:\n\n";
		for (int i = 0; i < naredna.vratiStranicu(); i++) {
			ispis += "   ";
			for (int j = 0; j < naredna.vratiStranicu(); j++)
				ispis += naredna.elementMatrice(i, j);
			ispis += "\n";
		}
		ispis += "\n\n";
		for (int i = 0; i < SIRINA_POLJA * 2 + 2; i++)
			ispis += granicnik;
		ispis += "\n";
		for (int i = 0; i < trenutna.vratiY() && i < VISINA_POLJA; i++) {
			ispis += granicnik;
			for (int j = 0; j < SIRINA_POLJA; j++)
				ispis += matrica[i][j] + " ";
			ispis += granicnik + "\n";
		}

		for (int i = trenutna.vratiY(); i < trenutna.vratiY()
				+ trenutna.vratiStranicu()
				&& i < VISINA_POLJA; i++) {
			ispis += granicnik;
			for (int j = 0; j < trenutna.vratiX() && j < SIRINA_POLJA; j++)
				ispis += matrica[i][j] + " ";
			for (int j = trenutna.vratiX(); j < trenutna.vratiX()
					+ trenutna.vratiStranicu()
					&& j < SIRINA_POLJA; j++) {
				if (trenutna.elementMatrice(i - trenutna.vratiY(),
						j - trenutna.vratiX()) != 0)
					ispis += trenutna.elementMatrice(i - trenutna.vratiY(), j
							- trenutna.vratiX())
							+ " ";
				else if (j >= 0)
					ispis += matrica[i][j] + " ";
			}
			for (int j = trenutna.vratiX() + trenutna.vratiStranicu(); j < SIRINA_POLJA; j++)
				ispis += matrica[i][j] + " ";
			ispis += granicnik + "\n";
		}

		for (int i = trenutna.vratiY() + trenutna.vratiStranicu(); i < VISINA_POLJA; i++) {
			ispis += granicnik;
			for (int j = 0; j < SIRINA_POLJA; j++)
				ispis += matrica[i][j] + " ";
			ispis += granicnik + "\n";
		}
		for (int i = 0; i < SIRINA_POLJA * 2 + 2; i++)
			ispis += granicnik;
		ispis += "\n";
		return ispis;
	}

	/**
	 * Getter metoda pomocu koje saznajemo koja je figura trenutno aktivna na
	 * ploci.
	 * 
	 * @return Vraca objekat tipa Figura, koji predstavlja trenutno aktivnu
	 *         figuru.
	 */
	public Figura vratiTrenutnu() {
		return trenutna;
	}

	/**
	 * Getter metoda pomocu koje saznajemo koja je figura naredna.
	 * 
	 * @return Vraca objekat tipa Figura, koji predstavlja figuru koja ce
	 *         sljedeca biti ubacena.
	 */
	public Figura vratiNarednu() {
		return naredna;
	}

	/**
	 * Getter metoda pomocu koje saznajemo rezultat.
	 * 
	 * @return Vraca cijeli broj koji predstavlja rezultat do sada odigranog
	 *         dijela igre.
	 */
	public int vratiRezultat() {
		return rezultat;
	}

	/**
	 * Metoda kojom saznajemo level igre.
	 * 
	 * @return Vraca cijeli broj, 1 - najlaksi level, 2 - srednji level, 3 -
	 *         najtezi level.
	 */
	public int vratiLevel() {
		return LEVEL;
	}

	/**
	 * Metoda kojom saznajemo sirinu ploce za igru.
	 * 
	 * @return Vraca cijeli broj koji predstavlja najveci broj kvadratica u
	 *         jednom redu.
	 */
	public int vratiSirinu() {
		return SIRINA_POLJA;
	}

	/**
	 * Metoda kojom saznajemo visinu ploce za igru.
	 * 
	 * @return Vraca cijeli broj koji predstavlja najveci broj kvadratica u
	 *         jednoj koloni.
	 */
	public int vratiVisinu() {
		return VISINA_POLJA;
	}

	/**
	 * Metoda kojom saznajemo elemente matrice ploce za igru.
	 * 
	 * @param i
	 *            Vertikalna koordinata elementa.
	 * @param j
	 *            Horizontalna koordinata elementa.
	 * @return Vraca vrijednost matrice ploce na poziciji (i,j).
	 */
	public int elementMatrice(int i, int j) {
		return matrica[i][j];
	}

	/**
	 * Metoda kojom saznajemo koliko nam je trenutno figura na raspolaganju.
	 * 
	 * @return Vraca cijeli broj, broj jedinstvenih figura.
	 */
	public int brojFigura() {
		return BROJ_MOGUCIH_FIGURA;
	}

	/**
	 * Metoda zaduzena za kraj igre i ispis rezultata.
	 */
	public void zavrsi() {
		System.out.println("Bravo! Zavrsili ste igru rezultatom: " + rezultat);
	}
}
