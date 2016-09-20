import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import java.io.File;

/**
 * Klasa kojom modeliramo panel gdje ce se nalaziti figure, tj. sama igra.
 * @author Nadin Zajimovic
 *
 */
public class PanelZaIgru extends JComponent {
	PlocaZaIgru ploca;
	BufferedImage slikeFigura[], pozadina;
	int dimenzijaSlike;
	final static long serialVersionUID = 100;
	
	/**
	 * Konstruktor koji stvara novi panel prema parametrima.
	 * @param level Nivo igre, tj. brzina kojom ce se figure spustati.
	 * @param visina Visina polja za igru.
	 * @param sirina Sirina polja za igru.
	 * @param rezolucija Rezolucija nam govori kolika ce biti dimenzija kvadratica za figure.
	 */
	public PanelZaIgru(int level, int visina, int sirina, int rezolucija) {
		ploca = new PlocaZaIgru(level, visina, sirina);
		slikeFigura = new BufferedImage[ploca.brojFigura() + 1];
		setBackground(Color.DARK_GRAY);
		for (int i = 1; i <= ploca.brojFigura(); i++) {
			Figura tmp = new Figura(i);
			slikeFigura[i] = tmp.vratiSlikuFigure();
		}
		
		int dimenzijaX=35,dimenzijaY=35;
		
		if (rezolucija == 1){
			dimenzijaX=(900/2)/sirina;
			dimenzijaY=500/visina;
		}
		else if (rezolucija == 2){
			dimenzijaX=(1200/2)/sirina;
			dimenzijaY=650/visina;
		}
		else{
			dimenzijaX=(1500/2)/sirina;
			dimenzijaY=800/visina;
		}
		if (dimenzijaX<dimenzijaY) dimenzijaSlike=dimenzijaX;
		else dimenzijaSlike=dimenzijaY;
		
		try{
			pozadina = ImageIO.read(new File("slike/pozadina.png"));
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(null, "Pozadina nije pronaðena!");
		}
	}
	
	/**
	 * Metoda koja je zaduzena za iscrtavanje figura na ekran.
	 */
	public void paint(Graphics g) {
		g.drawImage(pozadina,0,0,dimenzijaSlike*ploca.vratiSirinu(),dimenzijaSlike*ploca.vratiVisinu(),null);
		for (int i = 0; i < ploca.vratiVisinu(); i++) {
			for (int j = 0; j < ploca.vratiSirinu(); j++) {
				if (ploca.elementMatrice(i, j) != 0) {
					g.drawImage(slikeFigura[ploca.elementMatrice(i, j)], j
							* dimenzijaSlike, i * dimenzijaSlike,
							dimenzijaSlike, dimenzijaSlike, null);
				} /*else
					g.fillRect(j * dimenzijaSlike, i * dimenzijaSlike,
							dimenzijaSlike,

							dimenzijaSlike);*/
			}
		}

		for (int i = 0; i < ploca.vratiTrenutnu().vratiStranicu(); i++) {
			for (int j = 0; j < ploca.vratiTrenutnu().vratiStranicu(); j++) {
				if (ploca.vratiTrenutnu().elementMatrice(i, j) != 0) {
					int x, y;
					x = dimenzijaSlike * (ploca

					.vratiTrenutnu().vratiX() + j);

					y = dimenzijaSlike * (ploca.vratiTrenutnu().vratiY() + i);
					g.drawImage(slikeFigura[ploca.vratiTrenutnu().vratiTip()],
							x, y, dimenzijaSlike, dimenzijaSlike, null);
				}
			}
		}
	}
	
	/**
	 * Ovom metodom dobijamo plocu za igru.
	 * @return Vraca objekat tipa PlocaZaIgru, tj. trenutnu plocu koju koristimo.
	 */
	public PlocaZaIgru vratiPlocu() {
		return ploca;
	}
	
	/**
	 * Metoda kojom saznajemo dimenziju slike koja se koristi za kvadratice figura.
	 * @return Vraca vijeli broj koji predstavlja dimenziju slike u pixelima.
	 */
	public int vratiDimenzijuSlike() {
		return dimenzijaSlike;
	}
}