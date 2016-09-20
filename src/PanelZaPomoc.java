import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.awt.image.BufferedImage;
/**
 * Klasa kojom modeliramo panel za pomoc, tj. panel gdje ce se nalaziti rezultat i naredna figura.
 * @author Nadin Zajimovic
 *
 */
public class PanelZaPomoc extends JPanel{
	final static long serialVersionUID = 300;
	PlocaZaIgru ploca;
	PanelZaIgru panelZaIgru;
	BufferedImage slikaRezultat, slikaNarednaFigura, slikaKraj, pozadina;
	BufferedImage[] cifre;
	int visina, sirina;
	final int MAX_VISINA_FIGURE = 4;
	boolean gotova;
	
	/**
	 * Konstruktor kojim inicijaliziramo panel prema proslijedjenom panelu za igru, iz kojeg cemo saznavati rezultat i narednu figuru.
	 * @param panel Parametar je objekat klase PanelZaIgru, tj. panel za igru koji koristimo.
	 */
	public PanelZaPomoc(PanelZaIgru panel) {
		gotova = false;
		panelZaIgru=panel;
		ploca=panelZaIgru.vratiPlocu();
		visina = ploca.vratiVisinu()*panel.vratiDimenzijuSlike() + 28;
		sirina = ploca.vratiSirinu()*panel.vratiDimenzijuSlike();
		cifre = new BufferedImage[10];
		try{
			slikaRezultat = ImageIO.read(new File("slike/rezultat.png"));
			slikaNarednaFigura = ImageIO.read(new File("slike/narednaFigura.png"));
			slikaKraj = ImageIO.read(new File("slike/krajIgre.png"));
			String putanja="slike/";
			for (int i=0; i<10; i++) cifre[i] = ImageIO.read(new File(putanja + i + ".png"));
			pozadina = ImageIO.read(new File("slike/pozadina2.png"));
		}
		catch(Exception e){
			System.out.println("Izuzetak pri citanju slike!");
		}
	}
	
	/**
	 * Metoda zaduzena za iscrtavanje panela na ekran.
	 */
	public void paint (Graphics g){
		g.drawImage(pozadina,0,0,panelZaIgru.vratiDimenzijuSlike()*ploca.vratiSirinu(),panelZaIgru.vratiDimenzijuSlike()*ploca.vratiVisinu(),null);
		int razmakPixeli=10;
		int sirinaRezultat=slikaRezultat.getWidth(), visinaRezultat = slikaRezultat.getHeight();
		int sirinaNarednaFigura=slikaNarednaFigura.getWidth(), visinaNarednaFigura = slikaNarednaFigura.getHeight();
		int dimenzijaFigure=panelZaIgru.vratiDimenzijuSlike()*MAX_VISINA_FIGURE;
		int sirinaCifre=cifre[0].getWidth(), visinaCifre=cifre[0].getHeight();
		int visinaSvega=visinaCifre+visinaRezultat+dimenzijaFigure+visinaNarednaFigura + 5*razmakPixeli;
		int x=sirina/2 - sirinaRezultat/2, y= visina/2 - visinaSvega/2;
		g.drawImage(slikaRezultat,x,y,sirinaRezultat,visinaRezultat,null);
		y+= visinaRezultat+razmakPixeli;
		
		int brojCifara = 0, pom=ploca.vratiRezultat();
		int[] cifreRezultata = new int[10];
		while (pom>0){
			cifreRezultata[brojCifara++]=pom%10;
			pom/=10;
		}
		if (brojCifara==0) brojCifara=1;
		
		x=sirina/2 - (sirinaCifre*brojCifara)/2; 
		for (int i=brojCifara-1; i>=0; i--){
			g.drawImage(cifre[cifreRezultata[i]],x,y,sirinaCifre,visinaCifre,null);
			x+=sirinaCifre;
		}
		
		y+=visinaCifre + 3*razmakPixeli; 
		if (!gotova){
			x=sirina/2 - sirinaNarednaFigura/2;
			g.drawImage(slikaNarednaFigura, x, y, sirinaNarednaFigura, visinaNarednaFigura, null);
		
			int pocetakX=sirina/2 - ploca.vratiNarednu().vratiStranicu()*panelZaIgru.vratiDimenzijuSlike()/2;
			int pocetakY=y+visinaNarednaFigura+razmakPixeli;
			for (int i=0; i<ploca.vratiNarednu().vratiStranicu(); i++){
				for (int j=0; j<ploca.vratiNarednu().vratiStranicu(); j++){
					if (ploca.vratiNarednu().elementMatrice(i, j)!=0){
						x=pocetakX + j*panelZaIgru.vratiDimenzijuSlike();
						y=pocetakY + i*panelZaIgru.vratiDimenzijuSlike();
						g.drawImage(ploca.vratiNarednu().vratiSlikuFigure(),x,y,panelZaIgru.vratiDimenzijuSlike(),panelZaIgru.vratiDimenzijuSlike(),null);
					
					}
				}
			}
		}
		else{
			x=sirina/2 - slikaKraj.getWidth()/2;
			g.drawImage(slikaKraj,x,y,slikaKraj.getWidth(),slikaKraj.getHeight(),null);
		}
		
	}
	
	/**
	 * Metoda kojom zavrsajemo igru, tj. dajemo signal ovom panelu da je vrijeme da iscrta tekst "Kraj igre" na ekran.
	 * To se radi tako sto promjenjivu "gotova" postavimo na true.
	 */
	public void zavrsi(){
		gotova=true;
	}
}