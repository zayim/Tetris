import java.io.*;
/**
 * Klasa koja nam sluzi za lakse unosenje brojeva u konzolnom okruzenju. Ne samo brojeva, nego i stringova generalno.
 * Ova klasa predvidja i hvatanje izuzetaka bacenih usljed pogresno ucitanih vrijednosti.
 * @author Nadin Zajimovic
 *
 */
public class UlazniTok {
	/**
	 * Metoda koja trazi od korisnika da unese neki string.
	 * @return Vraca objekat tipa String, koji predstavlja unesenu rijec/string.
	 */
	public String unesiString() {
		String unos = new String();
		try {
			BufferedReader ulazniTok = new BufferedReader(
					new InputStreamReader(System.in));
			unos = ulazniTok.readLine();
			if (unos.length() == 0)
				return null;
		} catch (IOException e) {
			System.out.println("Izuzetak prilikom unosa stringa!");
		}
		return unos;
	}
	
	/**
	 * Metoda koja sluzi za unos cijelog broja. Prvo unesemo string, pomocu vec napisane metode, pa onda taj string pretvorimo u cijeli broj
	 * metodom parseInt iz klase Integer, naravno ukoliko je to moguce, tj. ukoliko smo zaista unijeli cijeli broj. Ukoliko smo pogrijesili
	 * pri unosu, ponovo unosimo broj, sve dok ga ne unesemo ispravno.
	 * @return Vraca uneseni cijeli broj.
	 */
	public int unesiInt() {
		String tmp = unesiString();
		int broj=0;
		if (tmp==null) return 0;
		else try{
			 broj = Integer.parseInt(tmp);
		}
		catch(Exception e){
			broj=unesiInt();
		}
		return broj;
	}
}
