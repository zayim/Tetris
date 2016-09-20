import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.FileInputStream;
/**
 * Klasa kojom modeliramo opsti oblik jedne figure. Figura se sastoji iz matrice, koja ce nam sluziti za laksu rotaciju i pozicije na
 * ploci za igru. Matrica nam govori o obliku figure, tj. tamo gdje se nalazi 0, nema nicega, na mjestima gdje se nalazi broj, nalazi se kockica.
 * Takodjer, potrebno je znati i tip figure, tj. jedinstven cijeli broj, te putanju gdje se nalazi slika koja ce biti iscrtavana na mjestima kvadratica i
 * putanju gdje se nalazi fajl u kojem je opisan izgled figure. cinjenica da je izgled figure opisan u fajlu, dozvoljava nam da mijenjamo oblik figure
 * bez potrebe za promjenama u izvornom kodu.
 * @author Nadin Zajimovic
 *
 */
public class Figura {
	boolean daLiJeNaPloci;
	
	int stranica, tipFigure, vertikalnoNaPloci, horizontalnoNaPloci;
	int[][] matrica;
	final int figuraI = 1, figura4 = 2, figuraO = 3, figuraL = 4, figuraZ = 5,
			figuraE = 6;
	BufferedImage slikaFigure;
	String putanjaSlike, putanjaFajla;

	/**
	 * Konstruktor koji ima zadatak da inicijalizira figuru prema parametru koji se prosljedjuje. Ovaj konstruktor
	 * procita informacije o figuri iz odgovarajuceg fajla i tako generise matricu figure. Takodjer se inicijalizira i
	 * slika figure koja ce se prikazivati na ekranu.
	 * @param tip Parametar tip nam govori koja vrsta figure ce se kreirati. Postoji 6 vrsta figura, koje se po potrebi mogu mijenjati u fajlovima.
	 */
	public Figura(int tip) {
		tipFigure = tip;
		daLiJeNaPloci = false;
		putanjaSlike = new String("figureSlike/figura");
		putanjaFajla = new String("figureOblici/figura");
		String tmp = new String();

		if (tip == figuraI) tmp = "I";
		else if (tip == figura4) tmp = "4";
		else if (tip == figuraO) tmp = "O";
		else if (tip == figuraL) tmp = "L";
		else if (tip == figuraZ) tmp = "Z";
		else if (tip == figuraE) tmp = "E";

		putanjaSlike += tmp + ".png";
		putanjaFajla += tmp + ".txt";
		try {
			slikaFigure = ImageIO.read(new File(putanjaSlike));
		} catch (IOException e) {
			System.out.println("Izutetak pri citanju slike!");
		}

		try {
			FileInputStream in = new FileInputStream(putanjaFajla);
			stranica = in.read() - 48;
			int pom = in.read(); pom = in.read();
			matrica = new int[stranica][stranica];
			for (int i = 0; i < stranica; i++) {
				for (int j = 0; j < stranica; j++) {
					pom = in.read();
					if (((char) pom) == ' ')
						matrica[i][j] = 0;
					else if (((char) pom) == '*')
						matrica[i][j] = tipFigure;
				}
				pom = in.read();
				pom = in.read();
			}
			in.close();

		} catch (IOException e) {
			System.out.println("Izuzetak pri citanju fajla za figure!");
		}
	}

	/**
	 * Konstruktor kopije koji ima zadatak da stori novi primjerak figure po
	 * uzoru na onaj koji mu je proslijedjen kao parametar.
	 * 
	 * @param fig Parametar je objekat tipa Figura, i kopiraju se svi atributi iz parametra u objekat nad kojim se poziva konstruktor.
	 */
	public Figura(Figura fig) {
		this.daLiJeNaPloci = fig.daLiJeNaPloci;
		this.stranica = fig.stranica;
		this.tipFigure = fig.tipFigure;
		this.vertikalnoNaPloci = fig.vertikalnoNaPloci;
		this.horizontalnoNaPloci = fig.horizontalnoNaPloci;
		this.putanjaSlike = fig.putanjaSlike;

		this.matrica = new int[this.stranica][this.stranica];

		for (int i = 0; i < this.stranica; i++)
			for (int j = 0; j < this.stranica; j++)
				this.matrica[i][j] = fig.matrica[i][j];

		try {
			this.slikaFigure = ImageIO.read(new File(this.putanjaSlike));
		} catch (IOException e) {
			System.out.println("Izutetak pri citanju slike!");
		}
	}

	/**
	 * Metoda koja postavlja figuru na plocu, zadajuci joj parametre X i Y
	 * koordinate gdje cemo je postaviti na plocu.
	 * 
	 * @param x Kooridnata x, tj. horizontalna pozicija gornjeg lijevog ugla figure na ploci.
	 * @param y Kooridnata y, tj. vertikalna pozicija gornjeg lijevog ugla figure na ploci.
	 */
	public void postaviNaPlocu(int x, int y) {
		daLiJeNaPloci = true;
		vertikalnoNaPloci = y;
		horizontalnoNaPloci = x;
	}
	
	/**
	 * Metoda zaduzena za rotaciju figure. Posto je figura zapravo kvadratna matrica, za rotaciju u smjeru kazaljke je dovoljno
	 * transponovati matricu i onda zamijeniti 1. kolonu sa zadnjom, 2. sa predzadnjom itd.
	 */
	public void rotiraj() {
		for (int i = 0; i < stranica; i++) {
			for (int j = 0; j <= i; j++) {
				int pomocna = matrica[i][j];
				matrica[i][j] = matrica[j][i];
				matrica[j][i] = pomocna;
			}
		}

		for (int i = 0; i < stranica; i++) {
			for (int j = 0; j < stranica / 2; j++) {
				int pomocna = matrica[i][j];
				matrica[i][j] = matrica[i][stranica - 1 - j];
				matrica[i][stranica - 1 - j] = pomocna;
			}
		}
	}

	/**
	 * Metoda koja pretvara figuru u string, oblik pogodan za ispis na konzolni ekran.
	 * @return Vraca kao rezultat objekat tipa String, tj. figuru pretvorenu u string.
	 */
	public String toString() {
		String ispis = "";
		for (int i = 0; i < stranica; i++) {
			for (int j = 0; j < stranica; j++)
				ispis += matrica[i][j] + " ";
			ispis += "\n";
		}
		return ispis;
	}
	
	/**
	 * Getter metoda za sliku figure.
	 * @return Vraca kao rezultat objekat tipa BufferedImage koji predstavlja sliku koristenu za iscrtavanje jednog kvadratica figure.
	 */
	public BufferedImage vratiSlikuFigure() {
		return slikaFigure;
	}
	
	/**
	 * Getter koji vraca duzinu stranice.
	 * @return Rezultat je tipa int i predstavlja dimenziju kvadratne matrice koristene za figuru.
	 */
	public int vratiStranicu() {
		return stranica;
	}
	
	/**
	 * Getter metoda pomocu koje saznajemo element matrice koristene za ovu figuru.
	 * @param i Vertikalna koordinata u matrici.
	 * @param j Horizontalna kooridnata u matrici.
	 * @return Rezultat je cijeli broj koji se nalazi na mjestu (i,j) u matrici.
	 */
	public int elementMatrice(int i, int j) {
		return matrica[i][j];
	}
	
	/**
	 * Getter pomocu kojeg saznajemo o kojoj je figuri rijec.
	 * @return Vraca kao rezutltat cijeli broj u opsegu 1 do 6, jer imamo 6 mogucih figura.
	 */
	public int vratiTip() {
		return tipFigure;
	}
	
	/**
	 * Metoda pomocu koje saznajemo horizontalnu poziciju figure na ploci za igru.
	 * @return Vraca cijeli broj koji predstavlja horizontalnu kooridnatu gornjeg lijevog ugla matrice na ploci.
	 */
	public int vratiX() {
		return horizontalnoNaPloci;
	}
	
	/**
	 * Metoda pomocu koje saznajemo vertikalnu poziciju figure na ploci za igru.
	 * @return Vraca cijeli broj koji predstavlja vertikalnu kooridnatu gornjeg lijevog ugla matrice na ploci.
	 */
	public int vratiY() {
		return vertikalnoNaPloci;
	}
	
	/**
	 * Setter metoda koja figuri dodjeljuje y kooridnatu na ploci.
	 * @param y Parametar je y kooridnata, tj. vertikalna pozicija figure na ploci.
	 */
	public void postaviY(int y) {
		vertikalnoNaPloci = y;
	}
	
	/**
	 * Setter metoda koja figuri dodjeljuje x kooridnatu na ploci.
	 * @param x Parametar je x kooridnata, tj. horizontalna pozicija figure na ploci.
	 */
	public void postaviX(int x) {
		horizontalnoNaPloci = x;
	}
	
	/**
	 * Metoda koja pomjera figuru za jednu poziciju nanize, tj. povecava joj y koordinatu.
	 */
	public void pomjeriDole() {
		vertikalnoNaPloci++;
	}
	
	/**
	 * Metoda koja pomjera figuru za jednu poziciju udesno, tj. povecava joj x koordinatu.
	 */
	public void pomjeriDesno() {
		horizontalnoNaPloci++;
	}
	
	/**
	 * Metoda koja pomjera figuru za jednu poziciju ulijevo, tj. smanjuje joj x koordinatu.
	 */
	public void pomjeriLijevo() {
		horizontalnoNaPloci--;
	}
}