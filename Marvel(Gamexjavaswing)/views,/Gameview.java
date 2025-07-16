package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.attribute.AclEntryFlag;
import java.time.chrono.MinguoDate;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import engine.Game;
import engine.Player;
import exceptions.AbilityUseException;
import exceptions.ChampionDisarmedException;
import exceptions.InvalidTargetException;
import exceptions.LeaderAbilityAlreadyUsedException;
import exceptions.LeaderNotCurrentException;
import exceptions.NotEnoughResourcesException;
import exceptions.UnallowedMovementException;
import model.abilities.Ability;
import model.world.AntiHero;
import model.world.Champion;
import model.world.Cover;
import model.world.Direction;
import model.world.Hero;

public class Gameview extends JFrame implements ActionListener, KeyListener{
	public Game game ; 
	JPanel player1panel;
	JPanel player2panel;
	JPanel currentpanel;
	JPanel gridpanel;
	JPanel turnorderpanel;
	JButton endturnn ; 
	JButton Leaderability ;
	boolean endgame ;
	 public Gameview(Game g) {
		 super("Battle");
		 ImageIcon img = new ImageIcon("Marvel2.png");
		    this.setIconImage(img.getImage());
		JButton b = new JButton();
		b.addKeyListener(this);b.addActionListener(this);
		 b.setBounds(0,0 , 5, 5);
		 this.add(b);
		 game = g ;
		 this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 this.setSize(1540,900);
		 this.setLayout(null);
		 //-------------grid panel     ---------------
		 gridpanel = new JPanel();
		 gridpanel.setLayout(new GridLayout(5,5));
		 updategrid();
	//	 JPanel left = new JPanel();
	//	 left.setLayout(new BorderLayout());
		 gridpanel.setBounds(900,0 , 640, 500);
	//	 left.add(gridpanel,BorderLayout.NORTH);
		 this.add(gridpanel); 
		// --------------Current panel ------------------
		 currentpanel = new JPanel();
		 currentpanel.setBackground(Color.cyan);
		 currentpanel.setLayout(new BorderLayout());
		 updatecurrentpannel();
		 currentpanel.setBounds(0,500 , 900, 400);
		 this.add(currentpanel);
	//----------------turn order panel---------------	 
		 turnorderpanel = new JPanel();
		 turnorderpanel.setBackground(new Color(102,51,0));
		 turnorderpanel.setLayout(new BorderLayout());
		 updateturnorderpannel();
		 turnorderpanel.setBounds(900,500 , 640, 400);
		 this.add(turnorderpanel);
	//--------------player1panel-----------------
		 player1panel = new JPanel();
		 player1panel.setBackground(Color.cyan);
		 player1panel.setLayout(new BorderLayout());
		 updateplayer1panel();
		 player1panel.setBounds(0,0 , 450, 500);
		 this.add(player1panel);
		 
	//-------------player2panel-----------------
		 player2panel = new JPanel();
		 player2panel.setBackground(Color.cyan);
		 player2panel.setLayout(new BorderLayout());
		 updateplayer2panel();
		 player2panel.setBounds(450,0 , 450, 500);
		 this.add(player2panel);
		 
		 
		 this.setLocationRelativeTo(null);
		  this.setVisible(true);
		  endgame = false ;
	}

	public void updateplayer2panel() {
		player2panel.removeAll();
		player2panel.revalidate();
		player2panel.repaint();
		// TODO Auto-generated method stub
		String s = "";
		s += "Player 2 : "+ game.getSecondPlayer().getName();
		s+="\n";
		if(game.isSecondLeaderAbilityUsed()) {
			s+= "Leader Ability is used" ;
			s+="\n";
		}
		else {
			s+= "Leader Ability isn't used yet " ;
			s+="\n";
		}
		s+="\n";
		for(int i = 0 ; i<game.getSecondPlayer().getTeam().size();i++) {
			Champion temp = game.getSecondPlayer().getTeam().get(i);
			s+="Champion "+(i+1)+" : "+temp.getName();
			if( temp instanceof Hero) {
				s+=" ( Hero )" ;
			}else {
				if(temp instanceof AntiHero) {
					s+=" ( AntiHero )" ;
				}
			else {
				s+=" ( Villian )" ;
			}
		}
			if(temp == game.getSecondPlayer().getLeader()) {
				s+=" ( Leader )";
			}
			else {
				s+=" ( Not Leader )";
			}
			s+="\n";
			s+="CurrentHP : "+ temp.getCurrentHP();
			s+= " , ";
			s+="Mana : "+ temp.getMana();
			s+= "\n";
			s+="Speed : "+ temp.getSpeed();
			s+= " , ";
			s+="Maxactions : "+ temp.getMaxActionPointsPerTurn();
			s+="\n";
			s+="AttackDamage : "+ temp.getAttackDamage();
			s+= " , ";
			s+="AttackRange : "+ temp.getAttackRange();
			s+="\n";
			s+="Applied Effects :"; 
			for(int j = 0 ; j<temp.getAppliedEffects().size(); j++) {
				s+= temp.getAppliedEffects().get(j).getName()+ " : "+temp.getAppliedEffects().get(j).getDuration();
				if(j%2 == 0 && (j+1)!=temp.getAppliedEffects().size())
					s+= "\n";
			}
			s+= "\n";
			s+="\n";
		}
		JTextArea j = new JTextArea(s);
		j.setEditable(false);
		j.setBackground(new Color(51,10,0));
		j.setForeground(Color.white);
		j.setFont(new Font("mm", 1, 13));
		player2panel.add(j);
	  //  player1panel.setBorder(BorderFactory.createLineBorder(Color.red));
	}

	public void updateplayer1panel() {
		player1panel.removeAll();
		player1panel.revalidate();
		player1panel.repaint();
		String s = "";
		s += "Player 1 : "+ game.getFirstPlayer().getName();
		s+="\n";
		if(game.isFirstLeaderAbilityUsed()) {
			s+= "Leader Ability is used" ;
			s+="\n";
		}
		else {
			s+= "Leader Ability isn't used yet " ;
			s+="\n";
		}
		s+="\n";
		for(int i = 0 ; i<game.getFirstPlayer().getTeam().size();i++) {
			Champion temp = game.getFirstPlayer().getTeam().get(i);
			s+="Champion "+(i+1)+" : "+temp.getName();
			if( temp instanceof Hero) {
				s+=" ( Hero )" ;
			}else {
				if(temp instanceof AntiHero) {
					s+=" ( AntiHero )" ;
				}
			else {
				s+=" ( Villian )" ;
			}
		}
			if(temp == game.getFirstPlayer().getLeader()) {
				s+=" ( Leader )";
			}
			else {
				s+=" ( Not Leader )";
			}
			s+="\n";
			s+="CurrentHP : "+ temp.getCurrentHP();
			s+= " , ";
			s+="Mana : "+ temp.getMana();
			s+= "\n";
			s+="Speed : "+ temp.getSpeed();
			s+= " , ";
			s+="Maxactions : "+ temp.getMaxActionPointsPerTurn();
			s+="\n";
			s+="AttackDamage : "+ temp.getAttackDamage();
			s+= " , ";
			s+="AttackRange : "+ temp.getAttackRange();
			s+="\n";
			s+="Applied Effects :"; 
			for(int j = 0 ; j<temp.getAppliedEffects().size(); j++) {
				s+= temp.getAppliedEffects().get(j).getName()+ " : "+temp.getAppliedEffects().get(j).getDuration();
				if(j%2 == 0 && (j+1)!=temp.getAppliedEffects().size())
					s+= "\n";
			}
			s+= "\n";
			s+="\n";
		}
		JTextArea j = new JTextArea(s);
		j.setEditable(false);
		j.setBackground(Color.black);
		j.setForeground(Color.white);
		j.setFont(new Font("mm", 1, 13));
		player1panel.add(j);
	  //  player1panel.setBorder(BorderFactory.createLineBorder(Color.red));
	}

	public void updateturnorderpannel2() {
		
		Color temp = null;
		if(game.getFirstPlayer().getTeam().contains(game.getCurrentChampion())) {
			temp = new Color(51,10,0);
		}
		else {
			temp = Color.black;
		}
		temp = new Color(0 , 0 ,60);
		turnorderpanel.removeAll();
		turnorderpanel.revalidate();
		turnorderpanel.repaint();
		String s = "" ;
		Champion curr = game.getCurrentChampion();
		Champion nex = game.getNextChampion();
		if(game.getFirstPlayer().getTeam().contains(curr)) {
			s+=game.getFirstPlayer().getName()+"'s  turn  ";
			s+="\n";
			s+="Current Champion : "+ curr.getName() ;
			s+="\n";
		}else {
			s+=game.getSecondPlayer().getName()+"'s  turn  ";
			s+="\n";
			s+="Current Champion : "+ curr.getName() ;
			s+="\n";
		}
		if(game.getFirstPlayer().getTeam().contains(nex)) {
			s+="Next turn : "+ game.getFirstPlayer().getName();
			s+="\n";
			s+="Next Champion : "+ nex.getName() ;
		}else {
			s+="Next turn : "+ game.getSecondPlayer().getName();
			s+="\n";
			s+="Next Champion : "+ nex.getName() ;
		}
		JTextArea a= new JTextArea(s);
		a.setFont(new Font("Omar", 3, 25));
		a.setEditable(false);
		a.setBackground(temp);
		a.setForeground(Color.cyan);
		a.setBounds(100,70 , 440, 150);
		turnorderpanel.setLayout(null);
		turnorderpanel.add(a);
		turnorderpanel.addKeyListener(this);
		
		turnorderpanel.setBackground(temp);
		
	}
	

	public void updatecurrentpannel() {
		currentpanel.removeAll();
		currentpanel.revalidate();
		currentpanel.repaint();
		Champion c = game.getCurrentChampion();
		JTextArea j = new JTextArea(currentChampiondata(c));
	//	j.setFont(new Font ("AKK",2,15));
		j.setEditable(false);
		JPanel ap = new JPanel();
		ap.setLayout(new FlowLayout());
		JLabel lab = new JLabel("Abilities : ");
		lab.setForeground(Color.cyan);
		ap.add(lab);
		for(int i = 0 ; i< c.getAbilities().size(); i++) {
			JPanel temp = new JPanel();temp.setLayout(new BorderLayout());
			JTextArea tex = new JTextArea(c.getAbilities().get(i).toString2());
			tex.setBackground(Color.GRAY);
			tex.setForeground(Color.cyan);
			MyButton2 but = new MyButton2(c.getAbilities().get(i),"Cast");
			but.setBackground(Color.blue);
			but.setForeground(Color.black);
			but.addKeyListener(this);
			but.addActionListener(this);
			temp.add(tex , BorderLayout.NORTH);
			temp.add(but , BorderLayout.SOUTH);
			ap.add(temp);
		}
		JLabel south = new JLabel();
		south.setLayout(new FlowLayout());
		endturnn = new JButton("END TURN");
		endturnn.setBackground(Color.blue);
		endturnn.setForeground(Color.black);
		endturnn.addActionListener(this);
		endturnn.addKeyListener(this);
		Leaderability = new JButton("Leader Ability");
		Leaderability.setBackground(Color.blue);
		Leaderability.setForeground(Color.black);
		Leaderability.addActionListener(this);
		Leaderability.addKeyListener(this);
		ap.add(endturnn );
		ap.add(Leaderability);
		if(game.getFirstPlayer().getTeam().contains(c)) {
		ap.setBackground(Color.black);
		j.setBackground(Color.black);
		}else {
			ap.setBackground(new Color(51,10,0));
			j.setBackground(new Color(51,10,0));
		}
		ap.setBackground(new Color(0 , 0 ,60));
		j.setBackground(new Color(0 , 0 ,60));
		ap.setForeground(Color.cyan);
		j.setForeground(Color.cyan);
		j.setFont(new Font("mm", 1, 13));
		currentpanel.add(j , BorderLayout.NORTH);
		currentpanel.add(ap , BorderLayout.CENTER);
		
	//	currentpanel.add(south , BorderLayout.SOUTH);
	}
	public String currentChampiondata(Champion c ) {
		String lea ="";
		if(game.getFirstPlayer().getLeader()==c ||game.getSecondPlayer().getLeader()==c )
			 lea = " leader ";
		else 
			lea = "Not Leader";
		String s = "";
		s+="Current Champion : "+ c.getName() + " ( "  +gettype(c)+" )"+" ("+lea+")";
		s+="\n";
		s+="CurrentHP : "+c.getCurrentHP();
		s+=" , Mana : "+ c.getMana();
		s+= "\n";
		s+="Actionpoints : "+ c.getCurrentActionPoints();
		s+= "\n";
		s+="AttackDamage : "+ c.getAttackDamage();
		s+=" ,AttackRange : "+ c.getAttackRange();
		s+= "\n";s+="Applied Effects :";
		for(int i = 0 ; i< c.getAppliedEffects().size();i++) {
			s+= c.getAppliedEffects().get(i).getName()+ " : "+c.getAppliedEffects().get(i).getDuration();
			if(i%2 == 0 && (i+1)!=c.getAppliedEffects().size())
				s+= "\n";
		}
		s+= "\n";
	//	s+="Abilities : "; 
		return s ;
	}
	public String gettype(Champion c) {
		if(c instanceof Hero)
			return "Hero";
		if(c instanceof AntiHero)
			return "AntiHero";
		return "Villian";
				
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(endgame)return ;
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_UP){
			
	          try {
				game.move(Direction.UP);
				updategrid();
				updatecurrentpannel();
				
			}catch (ChampionDisarmedException e1 ) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Champion is DisArmed");
			}catch(NotEnoughResourcesException e1 ){
				JOptionPane.showMessageDialog(null, "NotEnoughResources");
			}catch(UnallowedMovementException e1) {
				JOptionPane.showMessageDialog(null, "UnallowedMovement");
			}catch(CloneNotSupportedException e1) {
			//	JOptionPane.showMessageDialog(null, "Champion is DisArmed");
			}
	       }
		if(e.getKeyCode() == KeyEvent.VK_DOWN){
	          try {
				game.move(Direction.DOWN);	
				updategrid();
				updatecurrentpannel();
				
			} catch (ChampionDisarmedException e1 ) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Champion is DisArmed");
			}catch(NotEnoughResourcesException e1 ){
				JOptionPane.showMessageDialog(null, "NotEnoughResources");
			}catch(UnallowedMovementException e1) {
				JOptionPane.showMessageDialog(null, "UnallowedMovement");
			}catch(CloneNotSupportedException e1) {
			//	JOptionPane.showMessageDialog(null, "Champion is DisArmed");
			}
	       }
		if(e.getKeyCode() == KeyEvent.VK_LEFT){
	          try {
				game.move(Direction.LEFT);	
				updategrid();
				updatecurrentpannel();
				
			} catch (ChampionDisarmedException e1 ) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Champion is DisArmed");
			}catch(NotEnoughResourcesException e1 ){
				JOptionPane.showMessageDialog(null, "NotEnoughResources");
			}catch(UnallowedMovementException e1) {
				JOptionPane.showMessageDialog(null, "UnallowedMovement");
			}catch(CloneNotSupportedException e1) {
			//	JOptionPane.showMessageDialog(null, "Champion is DisArmed");
			}
	       }
		if(e.getKeyCode() == KeyEvent.VK_RIGHT){
	          try {
				game.move(Direction.RIGHT);
				updategrid();
				updatecurrentpannel();
				
			} catch (ChampionDisarmedException e1 ) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Champion is DisArmed");
			}catch(NotEnoughResourcesException e1 ){
				JOptionPane.showMessageDialog(null, "NotEnoughResources");
			}catch(UnallowedMovementException e1) {
				JOptionPane.showMessageDialog(null, "UnallowedMovement");
			}catch(CloneNotSupportedException e1) {
			//	JOptionPane.showMessageDialog(null, "Champion is DisArmed");
			}
	       }
		if(e.getKeyCode() == KeyEvent.VK_A) {
			try {
				game.attack(Direction.LEFT);updategrid();
				updatecurrentpannel();
				updateplayer1panel();
				updateplayer2panel();
				CheckEndGame();
				updateturnorderpannel();
			} catch (ChampionDisarmedException  e1) {
				JOptionPane.showMessageDialog(null, "Champion is DisArmed");
				
			}catch(NotEnoughResourcesException e1) {
				JOptionPane.showMessageDialog(null, "NotEnoughResources");
			}
             catch(CloneNotSupportedException e1) {
				
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_D) {
			try {
				game.attack(Direction.RIGHT);updategrid();
				updatecurrentpannel();
				updateplayer1panel();
				updateplayer2panel();
				CheckEndGame();
				updateturnorderpannel();
			} catch (ChampionDisarmedException  e1) {
				JOptionPane.showMessageDialog(null, "Champion is DisArmed");
				
			}catch(NotEnoughResourcesException e1) {
				JOptionPane.showMessageDialog(null, "NotEnoughResources");
			}
             catch(CloneNotSupportedException e1) {
				
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_W) {
			try {
				game.attack(Direction.UP);updategrid();
				updatecurrentpannel();
				updateplayer1panel();
				updateplayer2panel();
				CheckEndGame();
				updateturnorderpannel();
			} catch (ChampionDisarmedException  e1) {
				JOptionPane.showMessageDialog(null, "Champion is DisArmed");
				
			}catch(NotEnoughResourcesException e1) {
				JOptionPane.showMessageDialog(null, "NotEnoughResources");
			}
             catch(CloneNotSupportedException e1) {
				
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_S) {
			try {
				game.attack(Direction.DOWN);updategrid();
				updatecurrentpannel();
				updateplayer1panel();
				updateplayer2panel();
				CheckEndGame();
				updateturnorderpannel();
			} catch (ChampionDisarmedException  e1) {
				JOptionPane.showMessageDialog(null, "Champion is DisArmed");
				
			}catch(NotEnoughResourcesException e1) {
				JOptionPane.showMessageDialog(null, "NotEnoughResources");
			}
             catch(CloneNotSupportedException e1) {
				
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(endgame)return ;
		if(e.getSource() == endturnn) {
			try {
				game.endTurn();
				updatecurrentpannel();
				updateplayer1panel();
				updateplayer2panel();
				updateturnorderpannel();
			} catch (CloneNotSupportedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				
		}
		if(e.getSource() == Leaderability) {
			try {
				game.useLeaderAbility();
				updategrid();
				updateplayer1panel();
				updateplayer2panel();
				CheckEndGame();
				updateturnorderpannel();
			} catch (LeaderAbilityAlreadyUsedException e1) {
				JOptionPane.showMessageDialog(null, "LeaderAbilityAlreadyUsed");
			} catch (LeaderNotCurrentException e1) {
				JOptionPane.showMessageDialog(null, "LeaderNotCurrent");
			} catch (AbilityUseException e1) {
				JOptionPane.showMessageDialog(null, "AbilityUseException");
			} catch (CloneNotSupportedException e1) {
				JOptionPane.showMessageDialog(null, "AbilityUseException");
			}
		}
		if(e.getSource() instanceof MyButton2) {
			Ability a= ((MyButton2)e.getSource()).getA();
			switch(a.getCastArea()) {
			case DIRECTIONAL :{
				Direction[] d = {Direction.UP, Direction.DOWN,Direction.LEFT, Direction.RIGHT};
				JComboBox c1 = new JComboBox(d);
				c1.setEditable(true);
				JOptionPane.showMessageDialog( null, c1, "Pick a direction", JOptionPane.QUESTION_MESSAGE);
				
				try {
					game.castAbility(a,(Direction)c1.getSelectedItem());
					updatecurrentpannel();updategrid();
					updateplayer1panel();
					updateplayer2panel();
					CheckEndGame();
					updateturnorderpannel();
				} catch (AbilityUseException e1) {
					JOptionPane.showMessageDialog(null, "AbilityUseException");
				} catch (NotEnoughResourcesException e1) {
					JOptionPane.showMessageDialog(null, "NotEnoughResources");
				} catch (InvalidTargetException e1) {
					JOptionPane.showMessageDialog(null, "InvalidTarget");
				} catch (CloneNotSupportedException e1) {
					JOptionPane.showMessageDialog(null, "AbilityUseException");
				}
				
		break;	}
			case SINGLETARGET :{
				JPanel pan = new JPanel();
				pan.setLayout(new BorderLayout());
				Integer[] n = {0,1,2,3,4};
				JComboBox c2 = new JComboBox(n);
				c2.setEditable(true);
				JComboBox c3 = new JComboBox(n);
				c3.setEditable(true);
				pan.add(c2, BorderLayout.WEST);
				pan.add(c3, BorderLayout.EAST);
				JOptionPane.showMessageDialog( null, pan, "Pick a position", JOptionPane.QUESTION_MESSAGE);
				
				try {
					game.castAbility(a,((Integer)c2.getSelectedItem()).intValue(),((Integer)c3.getSelectedItem()).intValue());
					updatecurrentpannel();updategrid();
					updateplayer1panel();
					updateplayer2panel();
					CheckEndGame();
					updateturnorderpannel();
				} catch (AbilityUseException e1) {
					JOptionPane.showMessageDialog(null, "Cannot use this Ability ");
				} catch (NotEnoughResourcesException e1) {
					JOptionPane.showMessageDialog(null, "NotEnoughResources");
				} catch (InvalidTargetException e1) {
					JOptionPane.showMessageDialog(null, "InvalidTarget");
				} catch (CloneNotSupportedException e1) {
					JOptionPane.showMessageDialog(null, "Cannot use this Ability");
				}
			break ;}
			default :{
				try {
					game.castAbility(a);
					updatecurrentpannel();updategrid();
					updateplayer1panel();
					updateplayer2panel();
					CheckEndGame();
					updateturnorderpannel();
				} catch (AbilityUseException e1) {
					JOptionPane.showMessageDialog(null, "Cannot use this Ability");
				} catch (NotEnoughResourcesException e1) {
					JOptionPane.showMessageDialog(null, "NotEnoughResources");
				} catch (InvalidTargetException e1) {
					JOptionPane.showMessageDialog(null, "InvalidTarget");
				} catch (CloneNotSupportedException e1) {
					JOptionPane.showMessageDialog(null, "Cannot use this Ability");
				}
			}
			}
		}
			
		
	}
	public void updategrid2() {
		gridpanel.removeAll();
		gridpanel.revalidate();
		gridpanel.repaint();
		
		for(int i = 4 ; i>-1;i--) {
			for(int j = 0 ; j<5;j++) {
				Object o = game.getBoard()[i][j];
				if(o==null) {
					JTextField l = new JTextField("Empty");
					l.setHorizontalAlignment(JTextField.CENTER);
					l.setEditable(false);
					l.setBackground(Color.blue);
					l.setToolTipText("( "+i+" , "+j+" )");
					gridpanel.add(l);
				}
				else {
					if(o instanceof Cover){
						
						JTextField l = new JTextField("Cover : "+((Cover)o).getCurrentHP() );
						l.setHorizontalAlignment(JTextField.CENTER);
						l.setEditable(false);
						l.setBackground(Color.red);
						l.setToolTipText("( "+i+" , "+j+" )");
						gridpanel.add(l);
					}
					else {Image image ;
						Champion c = (Champion) o;String s="" ;
						if(game.inteam1(c)) {
							s += game.getFirstPlayer().getName();
						}else {
							s += game.getSecondPlayer().getName();
						}
						JPanel pann = new JPanel();
						pann.setLayout(new BorderLayout());
						JLabel l = new JLabel(s);pann.add(l, BorderLayout.NORTH);
						JLabel l2 ; 
						
						if(c instanceof Hero) {
							 l2 = new JLabel("Hero :"+c.getName());
							 s+="\n";
							 s+=c.getName()+" : "+ c.getCurrentHP();
							 s+="\n";
							 s+="Hero";
							 try {
								 image = ImageIO.read(new File("Letter-H-icon.png")).getScaledInstance(20, 15, Image.SCALE_DEFAULT);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						//	 s+="\n";
					//		 s+="( "+((int)c.getLocation().getX())+" , "+((int)c.getLocation().getY())+" )"; 
						}
						else {
							if(c instanceof AntiHero) {
								 l2 = new JLabel("Anti-Hero :"+c.getName());
								 s+="\n";
								 s+=c.getName()+" : "+ c.getCurrentHP();
								 s+="\n";
								 s+="Anti-Hero";
							//	 s+="\n";
							//	 s+="( "+((int)c.getLocation().getX())+" , "+((int)c.getLocation().getY())+" )"; 
							}
							else {
							 l2 = new JLabel("Villian :"+c.getName());
							 s+="\n";
							 s+=c.getName()+" : "+ c.getCurrentHP();
							 s+="\n";
							 s+="Villian";
						//	 s+="\n";
					//		 s+="( "+((int)c.getLocation().getX())+" , "+((int)c.getLocation().getY())+" )"; 
							}
						}
						pann.add(l2, BorderLayout.CENTER);
						JTextArea f = new JTextArea(s);
						f.setForeground(Color.white);
					//	f.setHorizontalAlignment(JTextField.CENTER);
						f.setEditable(false);
						f.setBackground(Color.BLACK);
						f.setToolTipText("( "+i+" , "+j+" )");
				//		f.addMouseListener(this);
						gridpanel.add(f);
					//	gridpanel.add(pann);
					}
				}
				
			}
		}
		this.add(gridpanel);
	}

	
	public void CheckEndGame() {
		if(game.checkGameOver()== null)
			return ;
		endgame = true ;
		new Goodbye(game.checkGameOver());
	}
	public void updategrid() {
		gridpanel.removeAll();
		gridpanel.revalidate();
		gridpanel.repaint();
		
		for(int i = 4 ; i>-1;i--) {
			for(int j = 0 ; j<5;j++) {
				Object o = game.getBoard()[i][j];
				if(o==null) {
					JTextField l = new JTextField("Empty");
					l.setHorizontalAlignment(JTextField.CENTER);
					l.setEditable(false);
					l.setBackground(Color.blue);
					l.setToolTipText("( "+i+" , "+j+" )");
					gridpanel.add(l);
				}
				else {
					if(o instanceof Cover){
						
						JTextField l = new JTextField("Cover : "+((Cover)o).getCurrentHP() );
						l.setHorizontalAlignment(JTextField.CENTER);
						l.setEditable(false);
						l.setBackground(Color.red);
						l.setToolTipText("( "+i+" , "+j+" )");
						Image image  = null ;
						try {
							 image  = ImageIO.read(new File("C-Icon.jpg")).getScaledInstance(20, 20, Image.SCALE_DEFAULT);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						JButton butto;
						if(image!= null)
						 butto = new JButton("Cover",new ImageIcon(image));
						else
						 butto = new JButton("Cover");
						butto.setBackground(Color.red);
						butto.setForeground(Color.black);
						String ss = "<html>"+ "(HP :"+((Cover)o).getCurrentHP()+" )"+"<br>"+"  ( "+i+" , "+j+" )";
						butto.setToolTipText(ss);
						butto.addKeyListener(this);
						gridpanel.add(butto);
				//		gridpanel.add(l);
					}
					else {Image image =  null ;
						Champion c = (Champion) o;String s="<html>";
						if(game.inteam1(c)) {
							s += game.getFirstPlayer().getName();
						}else {
							s += game.getSecondPlayer().getName();
						}
						JPanel pann = new JPanel();
						pann.setLayout(new BorderLayout());
						JLabel l = new JLabel(s);pann.add(l, BorderLayout.NORTH);
						JLabel l2 ; 
						
						if(c instanceof Hero) {
							 l2 = new JLabel("Hero :"+c.getName());
							 s+="<br>";
							 s+="  " +c.getName()+" (HP : "+ c.getCurrentHP()+")";
							 s+="<br>";
							 s+="  Hero";
							 try {
								 image = ImageIO.read(new File("H-Icon.jpg")).getScaledInstance(20, 20, Image.SCALE_DEFAULT);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						//	 s+="\n";
					//		 s+="( "+((int)c.getLocation().getX())+" , "+((int)c.getLocation().getY())+" )"; 
						}
						else {
							if(c instanceof AntiHero) {
								 l2 = new JLabel("Anti-Hero :"+c.getName());
								 s+="<br>";
								 s+="  " +c.getName()+" (HP : "+ c.getCurrentHP()+")";
								 s+="<br>";
								 s+="  Anti-Hero";
								 try {
									 image = ImageIO.read(new File("Letter-A-icon.png")).getScaledInstance(20, 20, Image.SCALE_DEFAULT);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							//	 s+="\n";
							//	 s+="( "+((int)c.getLocation().getX())+" , "+((int)c.getLocation().getY())+" )"; 
							}
							else {
							 l2 = new JLabel("Villian :"+c.getName());
							 s+="<br>";
							 s+="  " +c.getName()+" (HP : "+ c.getCurrentHP()+")";
							 s+="<br>";
							 s+="  Villian";
							 try {
								 image = ImageIO.read(new File("Letter-V-icon.png")).getScaledInstance(20, 20, Image.SCALE_DEFAULT);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						//	 s+="\n";
					//		 s+="( "+((int)c.getLocation().getX())+" , "+((int)c.getLocation().getY())+" )"; 
							}
						}
						pann.add(l2, BorderLayout.CENTER);
						JTextArea f = new JTextArea(s);
						f.setForeground(Color.white);
					//	f.setHorizontalAlignment(JTextField.CENTER);
						f.setEditable(false);
						f.setBackground(Color.BLACK);
						f.setToolTipText("( "+i+" , "+j+" )");
				//		f.addMouseListener(this);
						JButton butto ;
						if(image != null)
						 butto = new JButton(c.getName(),new ImageIcon(image));
						else 
						butto = new JButton(c.getName());
						if(game.getFirstPlayer().getTeam().contains(c)) {
						butto.setBackground(Color.black);
						butto.setForeground(Color.WHITE);
						}
						else {
							butto.setBackground(new Color(51,10,0));
							butto.setForeground(Color.WHITE);
						}
						
						butto.setToolTipText(s+"<br>"+"  ( "+i+" , "+j+" )");
						butto.addKeyListener(this);
						gridpanel.add(butto);
					//	gridpanel.add(pann);
					}
				}
				
			}
		}
		this.add(gridpanel);
	}
public void updateturnorderpannel() {
		
		Color temp = null;
		if(game.getFirstPlayer().getTeam().contains(game.getCurrentChampion())) {
			temp = new Color(51,10,0);
		}
		else {
			temp = Color.black;
		}
		temp = new Color(0 , 0 ,60);
		turnorderpanel.removeAll();
		turnorderpanel.revalidate();
		turnorderpanel.repaint();
		String s = "" ;
		Champion curr = game.getCurrentChampion();
		Champion nex = game.getNextChampion();
		if(game.getFirstPlayer().getTeam().contains(curr)) {
			s+=game.getFirstPlayer().getName()+"'s  turn  ";
			s+="\n";
			s+="Current Champion : "+ curr.getName() ;
			s+="\n";
		}else {
			s+=game.getSecondPlayer().getName()+"'s  turn  ";
			s+="\n";
			s+="Current Champion : "+ curr.getName() ;
			s+="\n";
		}
		s+="\n";
		
		if(game.getFirstPlayer().getTeam().contains(nex)) {
			s+="Next turn : "+ game.getFirstPlayer().getName();
			s+="\n";
			s+="Next Champion : "+ nex.getName() ;
		}else {
			s+="Next turn : "+ game.getSecondPlayer().getName();
			s+="\n";
			s+="Next Champion : "+ nex.getName() ;
			s+="\n"; 
		}
		String t ="If next champion is stunned , his turn wil be passed "; 
		JTextArea a= new JTextArea(s);
		a.setFont(new Font("Omar", 3, 15));
		a.setEditable(false);
		a.setBackground(temp);
		a.setForeground(Color.cyan);
		a.setBounds(50,50 , 300, 150);
		
		
		JTextArea b= new JTextArea(game.getTurnOrder().toString());
		b.setFont(new Font("Omar", 3, 15));
		b.setEditable(false);
		b.setBackground(temp);
		b.setForeground(Color.cyan);
		b.setBounds(450,50 , 160, 150);
		
		
		JTextArea c = new JTextArea(t);
		c.setFont(new Font("Omar", 3, 12));
		c.setEditable(false);
		c.setBackground(temp);
		c.setForeground(Color.yellow);
		c.setBounds(50,200 , 450, 25);
		turnorderpanel.setLayout(null);
		turnorderpanel.add(a);
		turnorderpanel.add(b);
		turnorderpanel.add(c);
		
		turnorderpanel.addKeyListener(this);
		
		turnorderpanel.setBackground(temp);
		
	}
}
