import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;
/**
 * Klasa GrafickoOkruzenje nam pruza sve graficke elemente za igranje Tetrisa.
 * @author Nadin Zajimovic
 *
 */
public class GrafickoOkruzenje extends TimerTask{
	JFrame glavniProzor;
	PanelZaIgru panelZaIgru;
	PanelZaPomoc panelZaPomoc;
	JButton pocetak, pomoc, postavke;
	int level, visina, sirina, rezolucija;
	Timer timer;
	boolean pauzirana, gotova;
	TipkaListener tipkaListener;
	static final long serialVersionUID = 200;
	
	/**
	 * Metoda pokreni sluzi za pokretanje glavnog prozora za igru, iz kojeg mozemo vidjeti informacije o igri, postavke i zapoceti igru.
	 */
	public void pokreni() {
		timer = new Timer();
		pauzirana = false;
		gotova=false;
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 0.1;
		glavniProzor = new JFrame("Tetris 2012");
		glavniProzor.setResizable(false);
		glavniProzor.getContentPane().setLayout(new GridBagLayout());
		glavniProzor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pocetak = new JButton("Tetris 2012");
		pocetak.addActionListener(new PocetakListener());
		pomoc = new JButton("Pomoc");
		pomoc.addActionListener(new PomocListener());
		postavke = new JButton("Postavke");
		postavke.addActionListener(new PostavkeListener());

		glavniProzor.getContentPane().add(pomoc, c);
		glavniProzor.getContentPane().add(pocetak, c);
		glavniProzor.getContentPane().add(postavke, c);
		glavniProzor.setSize(600, 400);
		glavniProzor.setVisible(true);
	}
	
	/**
	 * Ukoliko odaberemo pocetak igre, pokrece se metoda pocniIgru, koja inicijalizira novu igru i zapocinje sa radom.
	 */
	public void pocniIgru() {
		glavniProzor.getContentPane().setLayout(new GridLayout());
		glavniProzor.getContentPane().removeAll();
		if (level == 0)
			level = 1;
		if (visina == 0)
			visina = 20;
		if (sirina == 0)
			sirina = 10;
		if (rezolucija == 0)
			rezolucija = 3;
		
		panelZaIgru = new PanelZaIgru(level, visina, sirina, rezolucija);
		int visinaPixeli = panelZaIgru.vratiPlocu().vratiVisinu()
				* panelZaIgru.vratiDimenzijuSlike() + 28;
		int sirinaPixeli = (int)(2.02 * panelZaIgru.vratiPlocu().vratiSirinu()
				* panelZaIgru.vratiDimenzijuSlike());
		
		int razmak=700 - 250*(panelZaIgru.vratiPlocu().vratiLevel()-1);
		timer.schedule(this,razmak,razmak);
		
		panelZaPomoc = new PanelZaPomoc(panelZaIgru);
		
		glavniProzor.getContentPane().add(panelZaIgru,BorderLayout.WEST);
		glavniProzor.getContentPane().add(panelZaPomoc,BorderLayout.EAST);
		glavniProzor.setSize(sirinaPixeli, visinaPixeli);
		glavniProzor.repaint();
		tipkaListener = new TipkaListener();
		glavniProzor.addKeyListener(tipkaListener);
		glavniProzor.requestFocus();
	}
	
	/**
	 * Metoda koja zavrsava igru nakon sto je nemoguce vise spustati figure.
	 */
	public void zavrsiIgru(){
		panelZaPomoc.zavrsi();
		glavniProzor.repaint();
	}
	
	/**
	 * Metoda naslijedjena iz klase TimerTask, kojom definisemo sta trebamo raditi u pravilnim vremenskim intervalima, tj. automatsko spustanje figure.
	 */
	public void run(){
		if (!gotova){
			if (!pauzirana){
			panelZaIgru.vratiPlocu().automatskiPomjeriDole();
			glavniProzor.repaint();
			gotova=panelZaIgru.vratiPlocu().daLiJeIgraGotova();
			}
		}
		else{
			timer.cancel();
			glavniProzor.removeKeyListener(tipkaListener);
			zavrsiIgru();
			System.out.println("Gotova igra!");
		}
	}
	
	/**
	 * Listener klasa za tipku sa tastature.
	 * @author Nadin Zajimovic
	 *
	 */
	class TipkaListener implements KeyListener{
		/**
		 * Naslijedjena metoda iz klase KeyListener.
		 */
		public void keyPressed(KeyEvent e1){
			if (!gotova){
				int kodTipke = e1.getKeyCode();
				if (kodTipke==37) panelZaIgru.vratiPlocu().pomjeriLijevo();
				else if (kodTipke==38) panelZaIgru.vratiPlocu().rotiraj();
				else if (kodTipke==39) panelZaIgru.vratiPlocu().pomjeriDesno();
				else if (kodTipke==40) panelZaIgru.vratiPlocu().pomjeriDole();
				/////////   Ovaj dio je dodan zbog eksperimentisanja, pritisak na 'a' je pauza, pritisak na 'b' je nastavak...
				else if (kodTipke==65) pauzirana=true;
				else if (kodTipke==66) pauzirana=false;
				//////////
				glavniProzor.repaint();
				gotova=panelZaIgru.vratiPlocu().daLiJeIgraGotova();
			}
		}
		/**
		 * Naslijedjena metoda iz klase KeyListener.
		 */
		public void keyTyped(KeyEvent e1){
		}
		/**
		 * Naslijedjena metoda iz klase KeyListener.
		 */
		public void keyReleased(KeyEvent e1){
		}
	}
	
	/**
	 * Listener klasa za dugme za pocetak igre.
	 * @author Nadin Zajimovic
	 *
	 */
	class PocetakListener implements ActionListener {
		/**
		 * Naslijedjena metoda iz klase ActionListener.
		 */
		public void actionPerformed(ActionEvent e) {
			pocniIgru();
		}
	}
	
	/**
	 * Listener klasa za dugme za postavke igre.
	 * @author Nadin Zajimovic
	 *
	 */
	class PostavkeListener implements ActionListener {
		JFrame framePostavke;
		JButton ok;
		JRadioButton level1, level2, level3, rez1, rez2, rez3;
		JTextField visinaUnos, sirinaUnos;
		
		/**
		 * Naslijedjena metoda iz klase ActionListener.
		 */
		public void actionPerformed(ActionEvent e) {
			JPanel panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
			visinaUnos = new JTextField(2);
			sirinaUnos = new JTextField(2);

			framePostavke = new JFrame("Tetris 2012 - Postavke");
			framePostavke.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			ok = new JButton("OK");
			ok.addActionListener(new OkListener());
			JLabel level = new JLabel("Level: "), rezolucija = new JLabel(
					"Rezolucija: ");
			JLabel visina = new JLabel("Visina polja: "), sirina = new JLabel(
					"Sirina polja: ");

			level1 = new JRadioButton("Sporo");
			level2 = new JRadioButton("Srednje");
			level3 = new JRadioButton("Brzo");
			ButtonGroup levels = new ButtonGroup();
			levels.add(level1);
			levels.add(level2);
			levels.add(level3);
			rez1 = new JRadioButton("Niska");
			rez2 = new JRadioButton("Srednja");
			rez3 = new JRadioButton("Visoka");
			ButtonGroup rezolucije = new ButtonGroup();
			rezolucije.add(rez1);
			rezolucije.add(rez2);
			rezolucije.add(rez3);

			LevelListener lL = new LevelListener();
			level1.addActionListener(lL);
			level2.addActionListener(lL);
			level3.addActionListener(lL);

			RezolucijaListener rL = new RezolucijaListener();
			rez1.addActionListener(rL);
			rez2.addActionListener(rL);
			rez3.addActionListener(rL);

			framePostavke.setLayout(new GridBagLayout());
			panel.add(level);
			panel.add(level1);
			panel.add(level2);
			panel.add(level3);
			panel.add(rezolucija);
			panel.add(rez1);
			panel.add(rez2);
			panel.add(rez3);
			panel.add(visina);
			panel.add(visinaUnos);
			panel.add(sirina);
			panel.add(sirinaUnos);
			panel.add(ok);
			framePostavke.getContentPane().add(panel);
			framePostavke.setSize(400, 400);
			framePostavke.setVisible(true);
		}
		
		/**
		 * Metoda kojom dobijamo OK dugme.
		 * @return Vraca objekat klase JButton, koji predstavlja OK dugme koje se koristi.
		 */
		public JButton okDugme() {
			return ok;
		}
		
		/**
		 * Listener klasa za OK dugme.
		 * @author Nadin Zajimovic
		 *
		 */
		private class OkListener implements ActionListener {
			/**
			 * Naslijedjena metoda iz klase ActionListener.
			 */
			public void actionPerformed(ActionEvent e) {
				framePostavke.dispose();

				String tmp = sirinaUnos.getText();
				try {
					if (tmp != null)
						sirina = Integer.parseInt(tmp);
					tmp = visinaUnos.getText();
					if (tmp != null)
						visina = Integer.parseInt(tmp);
				} catch (Exception e1) {
					//JOptionPane.showMessageDialog(null,"Pogresno ste unijeli sirinu i/ili visinu!");
				}
			}

		}
		
		/**
		 * Klasa Listener za dugmad za izbor levela.
		 * @author Nadin Zajimovic
		 *
		 */
		public class LevelListener implements ActionListener {
			/**
			 * Naslijedjena metoda iz klase ActionListener.
			 */
			public void actionPerformed(ActionEvent e2) {
				if (e2.getSource() == level1)
					level = 1;
				else if (e2.getSource() == level2)
					level = 2;
				else
					level = 3;
			}
		}
		
		/**
		 * Klasa listener za dugmad za izbor rezolucije.
		 * @author Nadin Zajimovic
		 *
		 */
		public class RezolucijaListener implements ActionListener {
			/**
			 *  Naslijedjena metoda iz klase ActionListener.
			 */
			public void actionPerformed(ActionEvent e2) {
				if (e2.getSource() == rez1)
					rezolucija = 1;
				else if (e2.getSource() == rez2)
					rezolucija = 2;
				else
					rezolucija = 3;
			}
		}
	}
}

/**
 * Klasa Listener za dugme za pomoc.
 * @author Nadin Zajimovic
 *
 */
class PomocListener implements ActionListener {
	JFrame pomocFrame;
	JButton ok;
	
	/**
	 * Naslijedjena metoda iz klase ActionListener.
	 */
	public void actionPerformed(ActionEvent e) {
		pomocFrame = new JFrame("Pomoc");
		pomocFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pomocFrame.setLayout(new GridBagLayout());
		pomocFrame.setSize(300, 200);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

		JTextArea autor = new JTextArea(
				"Autor: Nadin Zajimovic\nGodina: 2012\nUstanova: PMF\n\n");
		JTextArea pomoc = new JTextArea(
				"Dobrodosli u Tetris 2012. Igrate sa strelicama,\ndok sami sebi mozete podesiti postavke brzine,\nrezolucije i nivoa. Sretno!");
		autor.setEditable(false);
		pomoc.setEditable(false);
		panel.add(autor);
		panel.add(pomoc);
		pomocFrame.getContentPane().add(panel);
		pomocFrame.setVisible(true);
	}
}

