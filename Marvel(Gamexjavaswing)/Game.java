package engine;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.hamcrest.core.IsInstanceOf;

import exceptions.AbilityUseException;
import exceptions.ChampionDisarmedException;
import exceptions.InvalidTargetException;
import exceptions.LeaderAbilityAlreadyUsedException;
import exceptions.LeaderNotCurrentException;
import exceptions.NotEnoughResourcesException;
import exceptions.UnallowedMovementException;
import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.CrowdControlAbility;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.effects.Disarm;
import model.effects.Dodge;
import model.effects.Effect;
import model.effects.EffectType;
import model.effects.Embrace;
import model.effects.PowerUp;
import model.effects.Root;
import model.effects.Shield;
import model.effects.Shock;
import model.effects.Silence;
import model.effects.SpeedUp;
import model.effects.Stun;
import model.world.AntiHero;
import model.world.Champion;
import model.world.Condition;
import model.world.Cover;
import model.world.Damageable;
import model.world.Direction;
import model.world.Hero;
import model.world.Villain;

public class Game {
	
	private static ArrayList<Champion> availableChampions;
	private static ArrayList<Ability> availableAbilities;
	private Player firstPlayer;
	private Player secondPlayer;
	private Object[][] board;
	private PriorityQueue turnOrder;
	private boolean firstLeaderAbilityUsed;
	private boolean secondLeaderAbilityUsed;
	private final static int BOARDWIDTH = 5;
	private final static int BOARDHEIGHT = 5;

	public Game(Player first, Player second) {
		firstPlayer = first;

		secondPlayer = second;
		availableChampions = new ArrayList<Champion>();
		availableAbilities = new ArrayList<Ability>();
		board = new Object[BOARDWIDTH][BOARDHEIGHT];
		turnOrder = new PriorityQueue(6);
		placeChampions();
		placeCovers();
		prepareChampionTurns();
	}

	public static void loadAbilities(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		while (line != null) {
			String[] content = line.split(",");
			Ability a = null;
			AreaOfEffect ar = null;
			switch (content[5]) {
			case "SINGLETARGET":
				ar = AreaOfEffect.SINGLETARGET;
				break;
			case "TEAMTARGET":
				ar = AreaOfEffect.TEAMTARGET;
				break;
			case "SURROUND":
				ar = AreaOfEffect.SURROUND;
				break;
			case "DIRECTIONAL":
				ar = AreaOfEffect.DIRECTIONAL;
				break;
			case "SELFTARGET":
				ar = AreaOfEffect.SELFTARGET;
				break;

			}
			Effect e = null;
			if (content[0].equals("CC")) {
				switch (content[7]) {
				case "Disarm":
					e = new Disarm(Integer.parseInt(content[8]));
					break;
				case "Dodge":
					e = new Dodge(Integer.parseInt(content[8]));
					break;
				case "Embrace":
					e = new Embrace(Integer.parseInt(content[8]));
					break;
				case "PowerUp":
					e = new PowerUp(Integer.parseInt(content[8]));
					break;
				case "Root":
					e = new Root(Integer.parseInt(content[8]));
					break;
				case "Shield":
					e = new Shield(Integer.parseInt(content[8]));
					break;
				case "Shock":
					e = new Shock(Integer.parseInt(content[8]));
					break;
				case "Silence":
					e = new Silence(Integer.parseInt(content[8]));
					break;
				case "SpeedUp":
					e = new SpeedUp(Integer.parseInt(content[8]));
					break;
				case "Stun":
					e = new Stun(Integer.parseInt(content[8]));
					break;
				}
			}
			switch (content[0]) {
			case "CC":
				a = new CrowdControlAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), e);
				break;
			case "DMG":
				a = new DamagingAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), Integer.parseInt(content[7]));
				break;
			case "HEL":
				a = new HealingAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), Integer.parseInt(content[7]));
				break;
			}
			availableAbilities.add(a);
			line = br.readLine();
		}
		br.close();
	}

	public static void loadChampions(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		while (line != null) {
			String[] content = line.split(",");
			Champion c = null;
			switch (content[0]) {
			case "A":
				c = new AntiHero(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				break;

			case "H":
				c = new Hero(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				break;
			case "V":
				c = new Villain(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				break;
			}

			c.getAbilities().add(findAbilityByName(content[8]));
			c.getAbilities().add(findAbilityByName(content[9]));
			c.getAbilities().add(findAbilityByName(content[10]));
			availableChampions.add(c);
			line = br.readLine();
		}
		br.close();
	}

	private static Ability findAbilityByName(String name) {
		for (Ability a : availableAbilities) {
			if (a.getName().equals(name))
				return a;
		}
		return null;
	}

	public void placeCovers() {
		int i = 0;
		while (i < 5) {
			int x = ((int) (Math.random() * (BOARDWIDTH - 2))) + 1;
			int y = (int) (Math.random() * BOARDHEIGHT);

			if (board[x][y] == null) {
				board[x][y] = new Cover(x, y);
				i++;
			}
		}

	}

	public void placeChampions() {
		int i = 1;
		for (Champion c : firstPlayer.getTeam()) {
			board[0][i] = c;
			c.setLocation(new Point(0, i));
			i++;
		}
		i = 1;
		for (Champion c : secondPlayer.getTeam()) {
			board[BOARDHEIGHT - 1][i] = c;
			c.setLocation(new Point(BOARDHEIGHT - 1, i));
			i++;
		}
	
	}

	public static ArrayList<Champion> getAvailableChampions() {
		return availableChampions;
	}

	public static ArrayList<Ability> getAvailableAbilities() {
		return availableAbilities;
	}

	public Player getFirstPlayer() {
		return firstPlayer;
	}

	public Player getSecondPlayer() {
		return secondPlayer;
	}

	public Object[][] getBoard() {
		return board;
	}

	public PriorityQueue getTurnOrder() {
		return turnOrder;
	}

	public boolean isFirstLeaderAbilityUsed() {
		return firstLeaderAbilityUsed;
	}

	public boolean isSecondLeaderAbilityUsed() {
		return secondLeaderAbilityUsed;
	}

	public static int getBoardwidth() {
		return BOARDWIDTH;
	}

	public static int getBoardheight() {
		return BOARDHEIGHT;
	}
	private void prepareChampionTurns() {
		
		for (int i = 0 ; i < firstPlayer.getTeam().size(); i++) {
			if(firstPlayer.getTeam().get(i).getCondition()!= Condition.KNOCKEDOUT)
				this.turnOrder.insert(firstPlayer.getTeam().get(i));
		}
		for (int i = 0 ; i < secondPlayer.getTeam().size() ; i++) {
			if(secondPlayer.getTeam().get(i).getCondition()!= Condition.KNOCKEDOUT)
				this.turnOrder.insert(secondPlayer.getTeam().get(i));
		}
		
	}
	public Champion getCurrentChampion() {
		return (Champion) this.turnOrder.peekMin() ; 
	}
	
	
	
	
	private boolean p1isdead () {
		for (int i = 0 ; i < firstPlayer.getTeam().size(); i++) {
			if(firstPlayer.getTeam().get(i).getCondition()!= Condition.KNOCKEDOUT||firstPlayer.getTeam().get(i).getCurrentHP()>0)
				return false ;
		}
		return true ; 
	}
	private boolean p2isdead () {
		for (int i = 0 ; i < secondPlayer.getTeam().size() ; i++) {
			if(secondPlayer.getTeam().get(i).getCondition()!= Condition.KNOCKEDOUT||secondPlayer.getTeam().get(i).getCurrentHP()>0)
				return false ;
		}
		return true ; 
	}
	public Player checkGameOver() {
		if((p1isdead())&& (!p2isdead()))
			return  this. secondPlayer;
		if((p2isdead())&& (!p1isdead()))
			return  this.firstPlayer;
		return null ; 
	}
	private void removedead(ArrayList<Champion> x ) {
		Point p ;
		for(int i =0 ; i< x.size(); i++) {
			if(x.get(i).getCondition() == Condition.KNOCKEDOUT||x.get(i).getCurrentHP()==0) {
				 p = x.get(i).getLocation(); 
				board [(int) p.getX()][(int) p.getY()] = null ;
				x.get(i).setLocation(null);
				if(inteam1(x.get(i))) {
					firstPlayer.getTeam().remove(x.get(i));
				}
				if(inteam2(x.get(i))) {
					secondPlayer.getTeam().remove(x.get(i));
				}

			}	
		}
		removedeadfromqueue();
		
	}
	private void removeAlldead(ArrayList<Damageable> x ) {
		Point p ;
		for(int i =0 ; i< x.size(); i++) {
			if(x.get(i) instanceof Champion)
				removedead((Champion)x.get(i));
			else  {
				removedead((Cover)x.get(i));
			}
		
			
		}
	}
	
	private void removedead(Champion x ) {
		Point p ;
		switch(x.getCondition()) {
		case KNOCKEDOUT :{
			 p = x.getLocation(); 
				board [(int) p.getX()][(int) p.getY()] = null ;
				x.setLocation(null);
				x.setCurrentHP(0);
				if(inteam1(x)) {
					firstPlayer.getTeam().remove(x);
				}
				if(inteam2(x)) {
					secondPlayer.getTeam().remove(x);
				}

				break ;
		}
		default:{
			if(x.getCurrentHP()==0) {
				 p = x.getLocation(); 
				 x.setCondition(Condition.KNOCKEDOUT);
				board [(int) p.getX()][(int) p.getY()] = null ;
				x.setLocation(null);
				if(inteam1(x)) {
					firstPlayer.getTeam().remove(x);
				}
				if(inteam2(x)) {
					secondPlayer.getTeam().remove(x);
				}
			}break;
		}
		}
			
		removedeadfromqueue();
}
	private void removedead(Cover x ) {
		Point p ;
		
			if(x.getCurrentHP() == 0) {
				 p = x.getLocation(); 
				board [(int) p.getX()][(int) p.getY()] = null ;
				
			}	
}
	public boolean  narefriends (Champion x , Champion y ) {
		if((this.firstPlayer.getTeam()).contains(x)&&(this.firstPlayer.getTeam()).contains(y))
			return true ; 
		if((this.secondPlayer.getTeam()).contains(x)&&(this.secondPlayer.getTeam()).contains(y))
			return true ; 
		return false ; 
	}
	public boolean arefriends (Champion x , Champion y ) {
		if(inteam1(x)&&inteam1(y))
			return true ;
		if(inteam2(x)&&inteam2(y))
			return true;
		return false ;
	}
	public boolean iinteam1 (Champion x ) {
		for(int i = 0 ; i< this.firstPlayer.getTeam().size(); i++) {
			if(x.getName().equals((this.firstPlayer.getTeam().get(i)).getName()))
				return true ; 
		}
		return false ; 
	}
	public boolean inteam1 (Champion x ) {
		if((this.firstPlayer.getTeam()).contains(x))return true ;
		return false ;
	}
	public boolean inteam2 (Champion x ) {
		if((this.secondPlayer.getTeam()).contains(x))return true ;
		return false ; 
	}
	
	public boolean iinteam2 (Champion x ) {
		for(int i = 0 ; i< this.secondPlayer.getTeam().size(); i++) {
			if(x.getName().equals((this.secondPlayer.getTeam().get(i)).getName()))
				return true ; 
		}
		return false ; 
	}
	
	public void useLeaderAbility() throws LeaderAbilityAlreadyUsedException, LeaderNotCurrentException, AbilityUseException, CloneNotSupportedException {
	/*	if(isSilent()) {
			throw new AbilityUseException();
		}*/
		
		if(this.firstPlayer.getTeam().contains(getCurrentChampion())) {
			if(this.firstLeaderAbilityUsed) 
				throw new LeaderAbilityAlreadyUsedException(); 
			if(!getCurrentChampion().getName().equals(this.firstPlayer.getLeader().getName()))	
				throw new LeaderNotCurrentException();
			if(getCurrentChampion()!= this.firstPlayer.getLeader())
				throw new LeaderNotCurrentException();
			useLeaderAbility2();
			this.firstLeaderAbilityUsed = true ; 
		}
		else {
			if(this.secondLeaderAbilityUsed)
				throw new LeaderAbilityAlreadyUsedException();
			if(!getCurrentChampion().getName().equals(this.secondPlayer.getLeader().getName()))	
				throw new LeaderNotCurrentException();
			if(getCurrentChampion()!= this.secondPlayer.getLeader())
				throw new LeaderNotCurrentException();
			useLeaderAbility2();
			this.secondLeaderAbilityUsed = true ; 
		}
		
	}
		
public void useLeaderAbility2() throws CloneNotSupportedException  {
	Champion x = getCurrentChampion();
	ArrayList<Champion> c = getallChampions();
	if(x instanceof Hero ) {
		
		for(int i = 0 ; i< c.size(); i++) {
			if((!arefriends(x, c.get(i)))) {
				c.remove(i);
				i--;
			}
		}
		x.useLeaderAbility(c);	
	}
	else {
		
		if(x instanceof AntiHero ) {
			
			for(int i = 0 ; i< c.size(); i++) {
				if(!notleader(c.get(i))) {
					c.remove(i);
					i--;
			}
				}
			x.useLeaderAbility(c);	
		}
		else {
			if(x instanceof Villain ) {
				
				for(int i = 0 ; i< c.size(); i++) {
					if((arefriends(x, c.get(i)))||(c.get(i).getCurrentHP()>=(int)(c.get(i).getMaxHP()*0.3))) {
						c.remove(i);
						i--;
					
				}
					}
				x.useLeaderAbility(c);	
				removedead(c);
			}
		}
	}

}

public  ArrayList<Champion> getallChampions (){
	 ArrayList<Champion> r = new  ArrayList<Champion>();
	for(int i = 0 ; i < this.firstPlayer.getTeam().size() ; i++) {
		if(isalive(this.firstPlayer.getTeam().get(i)))
		 r.add(this.firstPlayer.getTeam().get(i));
		 
	}
	for(int i = 0 ; i < this.secondPlayer.getTeam().size() ; i++) {
		if(isalive(this.secondPlayer.getTeam().get(i)))
		 r.add(this.secondPlayer.getTeam().get(i));	 
	}
	return r ; 
}

public boolean isalive (Champion x ) {
	if(x.getCondition()== Condition.KNOCKEDOUT||x.getCurrentHP()==0)
		return false  ;
	return true  ; 
}


public boolean nnotleader (Champion x) {
	if(x.getName().equals(this.firstPlayer.getLeader().getName()))
		return false  ;
	if(x.getName().equals(this.secondPlayer.getLeader().getName()))
		return false  ;
	return true  ;
}
public boolean notleader (Champion x) {
	if(x == firstPlayer.getLeader())
		return false  ;
	if(x == secondPlayer.getLeader())
		return false  ;
	return true  ;
}

		
	
	public boolean isDisarmed () {
		ArrayList<Effect> e = getCurrentChampion().getAppliedEffects();
		for(int i = 0 ; i< e.size(); i++ ) {
		if(	e.get(i).getName().equals("Disarm"))
			return true ; 		
		}
		return false ;
	}
	public boolean isSilent () {
		ArrayList<Effect> e = getCurrentChampion().getAppliedEffects();
		for(int i = 0 ; i< e.size(); i++ ) {
		if(	e.get(i).getName().equals("Silence"))
			return true ; 		
		}
		return false ;
	}
	public void attack(Direction d) throws ChampionDisarmedException, NotEnoughResourcesException, CloneNotSupportedException {
		Champion c = getCurrentChampion();
		if(isDisarmed())
			throw new ChampionDisarmedException();
		if(c.getCurrentActionPoints()<2)
			throw new NotEnoughResourcesException();
		c.setCurrentActionPoints(c.getCurrentActionPoints()-2);
		Damageable e = getdamagableindirection(d);
		if(e== null)return ;
		if(e instanceof Cover) {
			e.setCurrentHP(e.getCurrentHP()-c.getAttackDamage());
			removedead((Cover)e);
			return ;
		}
		int v = c.getAttackDamage();
		Champion t = (Champion)e;
		
		if(hasShield(t.getAppliedEffects())) {
			removeShileld(t);
			return ;
		}
		if(hasDodge(t.getAppliedEffects())) {
			int z = (int)(Math.random()*2);
			if(z==1)return ; 
		}
		
		if(areOfsameClass(c, t)) {
			t.setCurrentHP(t.getCurrentHP()-v);
			Knockoutifdead(t);
			removedead(t);
			return ;
		}
		t.setCurrentHP(t.getCurrentHP()-(int)(v*1.5));
		Knockoutifdead(t);
		removedead(t);
		
	
	}
	public boolean hasDodge (ArrayList<Effect> x ) {
		for(int i = 0 ; i < x.size(); i++) {
			   if((x.get(i).getName()).equals("Dodge"))
				   return true ;
			  
		   }
		   return false ; 
	}
	public boolean hasShield (ArrayList<Effect> x ) {
		for(int i = 0 ; i < x.size(); i++) {
			   if(x.get(i).getName().equals("Shield"))
				   return true ;
			  
		   }
		   return false ; 
	}
	//to be checked later 
	public void removeShileld (Champion c  ) throws CloneNotSupportedException {
		ArrayList<Effect> a = c.getAppliedEffects();
		for(int j = 0 ; j<a.size();j++) {
			if(a.get(j).getName().equals("Shield")) {
				Effect n = (Effect)a.get(j).clone();
				a.remove(j);
				n.remove(c);
				return ; 
			}
		}
	}
	public boolean areOfsameClass(Champion x , Champion e) {
		if(x instanceof Hero && e instanceof Hero )
			return true ;
		if(x instanceof AntiHero && e instanceof AntiHero )
			return true ;
		if(x instanceof Villain && e instanceof Villain )
			return true ;
		return false ; 
		
	}
	public Damageable getdamagableindirection(Direction d) {
		int r = getCurrentChampion().getAttackRange();
		Point p = getCurrentChampion().getLocation();
		int x =(int ) p.getX();
		int y =(int ) p.getY();
		switch(d) {
		
		case UP : {
			for(int i = 1 ; i <= r ; i++) {
				if((x+i)>4)break;
				if(board[x+i][y]!= null) {
					if(board[x+i][y] instanceof Cover) {
					return (Damageable)board[x+i][y];
					}
					else {
						if(!arefriends(getCurrentChampion(), (Champion) board[x+i][y]))
							return (Damageable)board[x+i][y];
					}
				}
			}		
		break ;}
		case DOWN : {
			for(int i = 1 ; i <= r ; i++) {
				if((x-i)<0)break;
				if(board[x-i][y]!= null) {
					if(board[x-i][y] instanceof Cover) {
						return (Damageable)board[x-i][y];
						}
						else {
							if(!arefriends(getCurrentChampion(), (Champion) board[x-i][y]))
								return (Damageable)board[x-i][y];
						}
				}
			}	
			break ;}
        case RIGHT : {
			for(int i = 1 ;i<=r ; i++) {
				if((y+i)>4)break; 
				if(board[x][y+i]!= null) {
					if(board[x][y+i] instanceof Cover) {
						return (Damageable)board[x][y+i];
						}
						else {
							if(!arefriends(getCurrentChampion(), (Champion) board[x][y+i]))
								return (Damageable)board[x][y+i];
						}			
			}
				}
			break ;}
        
        
        case LEFT : {
        	for(int i = 1 ;i<=r ; i++) {
			if((y-i)<0)break; 
			if(board[x][y-i]!= null) {
				if(board[x][y-i] instanceof Cover) {
					return (Damageable)board[x][y-i];
					}
					else {
						if(!arefriends(getCurrentChampion(), (Champion) board[x][y-i]))
							return (Damageable)board[x][y-i];
					}			
		}
			}
			
			break ;}
        default : return null ; 
		}
		return null ; 
	}
	public void Knockoutifdead(Champion x ) {
		if(x.getCurrentHP()== 0)
			x.setCondition(Condition.KNOCKEDOUT);
		if(x.getCondition()==Condition.KNOCKEDOUT)
			x.setCurrentHP(0);
	}
	private void removedeadfromqueue() {
		PriorityQueue q = new PriorityQueue(6) ;
		while(!turnOrder.isEmpty()) {
			Champion c = (Champion)turnOrder.peekMin();
			if(c.getCondition()!= Condition.KNOCKEDOUT&&c.getCurrentHP()>0)
				q.insert(turnOrder.remove());
			else {
				turnOrder.remove();
			}
		}
		while(!q.isEmpty()) {
			turnOrder.insert(q.remove());
		}
	}
	 public void castAbility(Ability a, Direction d) throws AbilityUseException, NotEnoughResourcesException, InvalidTargetException, CloneNotSupportedException {
		 if(d == null)
			 throw new InvalidTargetException();
		 Champion c = getCurrentChampion();
		 if(isSilent()) 
			 throw new AbilityUseException();
		 if(a.getCurrentCooldown()>0)
			 throw new AbilityUseException();
		 if(a.getManaCost()>c.getMana())
			 throw new NotEnoughResourcesException();
		 if(a.getRequiredActionPoints()>c.getCurrentActionPoints())
			 throw new NotEnoughResourcesException();
		 c.setCurrentActionPoints(c.getCurrentActionPoints()-a.getRequiredActionPoints());
		 c.setMana(c.getMana()-a.getManaCost());
		 a.setCurrentCooldown(a.getBaseCooldown());
		 if(a instanceof HealingAbility) {
			 a.execute(getallfreindchampionsindirection(a, d));
			 return ; 
		 }
		 if(a instanceof DamagingAbility){
			 ArrayList<Damageable> list1 = getallenemieschampionsindirection(a, d);
			 a.execute(list1);
			 ArrayList<Damageable> list2 = getallcoverschampionsindirection(a, d) ;
			 a.execute(list2);
			 for(int i = 0 ; i<list1.size(); i++) {
				 Champion temp = (Champion)list1.get(i);
				 Knockoutifdead(temp );
				 removedead(temp);
			 }
			 for(int i = 0 ; i<list2.size(); i++) {
				 Cover temp = (Cover)list2.get(i);
				 removedead(temp);
			 }
			 return ; 
		 }
		 if(a instanceof CrowdControlAbility) {
			 CrowdControlAbility temp = (CrowdControlAbility)a ;
			 if(temp.getEffect().getType()== EffectType.BUFF) {
				 a.execute(getallfreindchampionsindirection(a, d));
				 return ;
			 }
			 if(temp.getEffect().getType()== EffectType.DEBUFF) {
				 a.execute(getallenemieschampionsindirection(a, d));
				 return ; 
			 }
		 }	 
	
	 }
	 public  ArrayList<Damageable> getallfreindchampionsindirection(Ability a,Direction d ){
		 Champion c  = getCurrentChampion();
		 int r = a.getCastRange();  
		 Point p = getCurrentChampion().getLocation();
		 int x =(int ) p.getX();
		 int y =(int ) p.getY();
		 ArrayList<Damageable> l = new ArrayList<Damageable>();
			switch(d) {
			
			case UP : {
				for(int i = 1 ; i <= r ; i++) {
					if((x+i)>4)break;
					if(board[x+i][y]!= null) {
						if(board[x+i][y] instanceof Champion) {
							Champion t = (Champion)board[x+i][y];
							if(arefriends(c, t))
								l.add(t);
						}
						
					}
				}		
			break ;}
			case DOWN : {
				for(int i = 1 ; i <= r ; i++) {
					if((x-i)<0)break;
					if(board[x-i][y]!= null) {
						if(board[x-i][y] instanceof Champion) {
							Champion t = (Champion)board[x-i][y];
							if(arefriends(c, t))
								l.add(t);
						}
					}
				}	
				break ;}
	        case RIGHT : {
				for(int i = 1 ;i<=r ; i++) {
					if((y+i)>4)break; 
					if(board[x][y+i]!= null) {
						if(board[x][y+i] instanceof Champion) {
							Champion t = (Champion)board[x][y+i];
							if(arefriends(c, t))
								l.add(t);
						}
				}
					}
				break ;}
	        
	        
	        case LEFT : {
	        	for(int i = 1 ;i<=r ; i++) {
				if((y-i)<0)break; 
				if(board[x][y-i]!= null) {
					if(board[x][y-i] instanceof Champion) {
						Champion t = (Champion)board[x][y-i];
						if(arefriends(c, t))
							l.add(t);
					}
							
			}
				}
				
				break ;}
	        default : return l ; 
			}
			return l; 
	 }
	 public  ArrayList<Damageable> getallenemieschampionsindirection(Ability a,Direction d ){
		 Champion c  = getCurrentChampion();
		 int r = a.getCastRange();  
		 Point p = getCurrentChampion().getLocation();
		 int x =(int ) p.getX();
		 int y =(int ) p.getY();
		 ArrayList<Damageable> l = new ArrayList<Damageable>();
			switch(d) {
			
			case UP : {
				for(int i = 1 ; i <= r ; i++) {
					if((x+i)>4)break;
					if(board[x+i][y]!= null) {
						if(board[x+i][y] instanceof Champion) {
							Champion t = (Champion)board[x+i][y];
							if(!arefriends(c, t))
								l.add(t);
						}
						
					}
				}		
			break ;}
			case DOWN : {
				for(int i = 1 ; i <= r ; i++) {
					if((x-i)<0)break;
					if(board[x-i][y]!= null) {
						if(board[x-i][y] instanceof Champion) {
							Champion t = (Champion)board[x-i][y];
							if(!arefriends(c, t))
								l.add(t);
						}
					}
				}	
				break ;}
	        case RIGHT : {
				for(int i = 1 ;i<=r ; i++) {
					if((y+i)>4)break; 
					if(board[x][y+i]!= null) {
						if(board[x][y+i] instanceof Champion) {
							Champion t = (Champion)board[x][y+i];
							if(!arefriends(c, t))
								l.add(t);
						}
				}
					}
				break ;}
	        
	        
	        case LEFT : {
	        	for(int i = 1 ;i<=r ; i++) {
				if((y-i)<0)break; 
				if(board[x][y-i]!= null) {
					if(board[x][y-i] instanceof Champion) {
						Champion t = (Champion)board[x][y-i];
						if(!arefriends(c, t))
							l.add(t);
					}
							
			}
				}
				
				break ;}
	        default : return l ; 
			}
			return l; 
	 }
	 public  ArrayList<Damageable> getallcoverschampionsindirection(Ability a,Direction d ){
		 Champion c  = getCurrentChampion();
		 int r = a.getCastRange();  
		 Point p = getCurrentChampion().getLocation();
		 int x =(int ) p.getX();
		 int y =(int ) p.getY();
		 ArrayList<Damageable> l = new ArrayList<Damageable>();
			switch(d) {
			
			case UP : {
				for(int i = 1 ; i <= r ; i++) {
					if((x+i)>4)break;
					if(board[x+i][y]!= null) {
						if(board[x+i][y] instanceof Cover) {
							Cover t = (Cover)board[x+i][y];
								l.add(t);
						}
						
					}
				}		
			break ;}
			case DOWN : {
				for(int i = 1 ; i <= r ; i++) {
					if((x-i)<0)break;
					if(board[x-i][y]!= null) {
						if(board[x-i][y] instanceof Cover) {
							Cover t = (Cover)board[x-i][y];
								l.add(t);
						}
					}
				}	
				break ;}
	        case RIGHT : {
				for(int i = 1 ;i<=r ; i++) {
					if((y+i)>4)break; 
					if(board[x][y+i]!= null) {
						if(board[x][y+i] instanceof Cover) {
							Cover t = (Cover)board[x][y+i];
							
								l.add(t);
						}
				}
					}
				break ;}
	        
	        
	        case LEFT : {
	        	for(int i = 1 ;i<=r ; i++) {
				if((y-i)<0)break; 
				if(board[x][y-i]!= null) {
					if(board[x][y-i] instanceof Cover) {
						Cover t = (Cover)board[x][y-i];
							l.add(t);
					}
							
			}
				}
				
				break ;}
	        default : return l ; 
			}
			return l; 
	 }
	 public int Manhatanlength (Point p1 , Point p2) {
		 int x1 = (int) p1.getX();
		 int y1 = (int)p1.getY();
		 int x2 = (int)p2.getX();
		 int y2 = (int)p2.getY();
		 int r = Math.abs(x2-x1) + Math.abs(y2-y1);
		 return r ;
		 
	 }
	 public void castAbility(Ability a, int x, int y) throws InvalidTargetException, AbilityUseException, NotEnoughResourcesException, CloneNotSupportedException {
		 if ( x<0||y<0||x>4||y>4 )
			 throw new AbilityUseException();
		 Champion c = getCurrentChampion();
		 
		 if(isSilent()) 
			 throw new AbilityUseException();
		 if(a.getCurrentCooldown()>0)
			 throw new AbilityUseException();
		 if(a.getManaCost()>c.getMana())
			 throw new NotEnoughResourcesException();
		 if(a.getRequiredActionPoints()>c.getCurrentActionPoints())
			 throw new NotEnoughResourcesException();
		 if(board[x][y]==null)
			 throw new InvalidTargetException();
		 Point p = new Point(x,y);
		 
		 if(Manhatanlength(c.getLocation(), p)>a.getCastRange())
			 throw new AbilityUseException();
		 
		 Damageable d =(Damageable) board[x][y];
		 ArrayList<Damageable> targets = new ArrayList<Damageable>();
		 targets.add(d);
		 if(a instanceof HealingAbility) {
			 if(d instanceof Cover)
				 throw new InvalidTargetException();
			 if(d instanceof Champion) {
				 if(!arefriends(c, (Champion)d))
					 throw new InvalidTargetException();
				 c.setCurrentActionPoints(c.getCurrentActionPoints()-a.getRequiredActionPoints());
				 c.setMana(c.getMana()-a.getManaCost());
				 a.setCurrentCooldown(a.getBaseCooldown());
				 a.execute(targets);
				 return ; 
			 }
		 }
		 if(a instanceof DamagingAbility) {
			 if(d instanceof Champion) {
				 if(arefriends(c, (Champion)d))
					 throw new InvalidTargetException();
			 }
			 c.setCurrentActionPoints(c.getCurrentActionPoints()-a.getRequiredActionPoints());
			 c.setMana(c.getMana()-a.getManaCost());
			 a.setCurrentCooldown(a.getBaseCooldown());
			 a.execute(targets);
			 if(d instanceof Champion) {
					 Knockoutifdead((Champion)d);
					 removedead((Champion)d);
			 }
			 else {
				 if(d instanceof Cover)
					 removedead((Cover)d);
			 }
			return ;  		
		 
		 }
		 if(a instanceof CrowdControlAbility) {
			 if(d instanceof Cover)
				 throw new InvalidTargetException();
			 CrowdControlAbility temp = (CrowdControlAbility)a ;
			 c.setCurrentActionPoints(c.getCurrentActionPoints()-a.getRequiredActionPoints());
			 c.setMana(c.getMana()-a.getManaCost());
			 a.setCurrentCooldown(a.getBaseCooldown());
			 switch(temp.getEffect().getType()) {
			 case BUFF:{
				 if(!arefriends(c, (Champion)d))
					 throw new InvalidTargetException();
				 a.execute(targets); 
			break ; }
			 case DEBUFF : {
				 if(arefriends(c, (Champion)d))
					 throw new InvalidTargetException();
				 a.execute(targets); 
			break ; 
			 }
			 }
		 }
		 
	 }
	 public void castAbility(Ability a) throws AbilityUseException, NotEnoughResourcesException, InvalidTargetException, CloneNotSupportedException {
		 Champion c = getCurrentChampion();
		 if(isSilent()) 
			 throw new AbilityUseException();
		 if(a.getCurrentCooldown()>0)
			 throw new AbilityUseException();
		 if(a.getManaCost()>c.getMana())
			 throw new NotEnoughResourcesException();
		 if(a.getRequiredActionPoints()>c.getCurrentActionPoints())
			 throw new NotEnoughResourcesException();
		 Point p = c.getLocation();
		 c.setCurrentActionPoints(c.getCurrentActionPoints()-a.getRequiredActionPoints());
		 c.setMana(c.getMana()-a.getManaCost());
		 a.setCurrentCooldown(a.getBaseCooldown());
		 ArrayList<Damageable> targets = new ArrayList<Damageable>();
		 switch(a.getCastArea()) {
		 case SELFTARGET : {
			 if(a instanceof DamagingAbility) {
				 c.setCurrentActionPoints(c.getCurrentActionPoints()+a.getRequiredActionPoints());
				 c.setMana(c.getMana()+a.getManaCost());
				 a.setCurrentCooldown(0);
				 throw new InvalidTargetException();
			 }
			 if(a instanceof CrowdControlAbility) {
				 CrowdControlAbility temp = (CrowdControlAbility)a ;
				 switch(temp.getEffect().getType()) {
				 case DEBUFF :{
					 c.setCurrentActionPoints(c.getCurrentActionPoints()+a.getRequiredActionPoints());
					 c.setMana(c.getMana()+a.getManaCost());
					 a.setCurrentCooldown(0);
					 throw new InvalidTargetException();
				 }
				 case BUFF : {
					 c.setMana(c.getMana()+a.getManaCost());
					 ArrayList<Damageable> lis = new  ArrayList<Damageable>();
					 lis.add(getCurrentChampion());
					 a.execute(lis);
					 c.setMana(c.getMana()-a.getManaCost());
				break ;  }
				 }
			 }
			 if(a instanceof HealingAbility) {
			 ArrayList<Damageable> lis = new  ArrayList<Damageable>();
			 lis.add(getCurrentChampion());
			 a.execute(lis);
			 }
		 break ; }
		 case TEAMTARGET : {
			 if(a instanceof HealingAbility) {
				 getfiendinrange(targets, a.getCastRange());
				 a.execute(targets);
				 return ;
			 }
			 if(a instanceof DamagingAbility) {
				 getenemyinrange(targets, a.getCastRange());
				 a.execute(targets);
				 
				 removeAlldead(targets); 
			 }
			 if(a instanceof CrowdControlAbility) {
				 CrowdControlAbility tempa = (CrowdControlAbility)a ;
				 switch(tempa.getEffect().getType()) {
				 case BUFF:{
					 c.setMana(c.getMana()+a.getManaCost());
					 getfiendinrange(targets, a.getCastRange());
					 a.execute(targets);
					 c.setMana(c.getMana()-a.getManaCost());
						 break ;}
				 case DEBUFF :{
					 getenemyinrange(targets, a.getCastRange());
					 a.execute(targets);
				break ; }
				 }
			 }
		break ; }
		 case SURROUND : {
			 if(a instanceof HealingAbility) {
				 getSurrondfriends(targets);
				 a.execute(targets);
				 return ;
			 }
			 if(a instanceof DamagingAbility) {
				getSurrondenemy(targets);
				getSurrondCover(targets);
				 a.execute(targets);
				 removeAlldead(targets); 
			 }
			 if(a instanceof CrowdControlAbility) {
				 CrowdControlAbility tempa = (CrowdControlAbility)a ;
				 switch(tempa.getEffect().getType()) {
				 case BUFF:{
					 getSurrondfriends(targets);
					 a.execute(targets);
						 break ;}
				 case DEBUFF :{
					 getSurrondenemy(targets);
					 a.execute(targets);
				break ; }
				 default :break ; 
				 }
			 }
		break ;  }
		 default : return ; 
		 }
		 
	 }
	 public void getfiendinrange(ArrayList<Damageable> targets, int range) {
		 Champion c = getCurrentChampion() ;
		 
		 if(inteam1(c)) {
			 for(int i = 0 ; i< this.firstPlayer.getTeam().size() ; i++) {
				 Point p = this.firstPlayer.getTeam().get(i).getLocation();
				 if(Manhatanlength(c.getLocation(), p)<=range)
					 targets.add(this.firstPlayer.getTeam().get(i));
			 }
		 }
		 if(inteam2(c)) {
			 for(int i = 0 ; i< this.secondPlayer.getTeam().size() ; i++) {
				 Point p = this.secondPlayer.getTeam().get(i).getLocation();
				 if(Manhatanlength(c.getLocation(), p)<=range)
					 targets.add(this.secondPlayer.getTeam().get(i));
			 }
		 }	
	 }
	 public void getenemyinrange(ArrayList<Damageable> targets, int range) {
		 Champion c = getCurrentChampion() ;
		 if(inteam2(c)) {
			 for(int i = 0 ; i< this.firstPlayer.getTeam().size() ; i++) {
				 Point p = this.firstPlayer.getTeam().get(i).getLocation();
				 if(Manhatanlength(c.getLocation(), p)<=range)
					 targets.add(this.firstPlayer.getTeam().get(i));
			 }
		 }
		 if(inteam1(c)) {
			 for(int i = 0 ; i< this.secondPlayer.getTeam().size() ; i++) {
				 Point p = this.secondPlayer.getTeam().get(i).getLocation();
				 if(Manhatanlength(c.getLocation(), p)<=range)
					 targets.add(this.secondPlayer.getTeam().get(i));
			 }
		 }	 
	 }
	 public void getSurrondfriends (ArrayList<Damageable> targets) {
		 Champion c = getCurrentChampion();
		 Point p = c.getLocation();
		 int x = (int)p.getX();
		 int y = (int)p.getY();
		 Champion t ;
		 if((x+1)<5) {
			 for(int i = -1 ; i<=1; i++) {
				 int s = y +i ; 
				 if(s<5 &&s>=0) {
					 if(board[x+1][y+i]!= null) {
						 if(board[x+1][y+i] instanceof Champion) {
							 t= (Champion)board[x+1][y+i] ;
							 if(arefriends(c, t))
								 targets.add(t);
						 }
					 }
				 }
			 }
		 }
		 if((x-1)>=0) {
			 for(int i = -1 ; i<=1; i++) {
				 int s = y +i ; 
				 if(s<5 &&s>=0) {
					 if(board[x-1][y+i]!= null) {
						 if(board[x-1][y+i] instanceof Champion) {
							 t= (Champion)board[x-1][y+i] ;
							 if(arefriends(c, t))
								 targets.add(t);
						 }
					 }
				 }
			 }
		 }
		 for(int i = -1 ; i<=1; i++) {
			 if(i != 0) {
			 int s = y +i ; 
			 if(s<5 &&s>=0) {
				 if(board[x][y+i]!= null) {
					 if(board[x][y+i] instanceof Champion) {
						 t= (Champion)board[x][y+i] ;
						 if(arefriends(c, t))
							 targets.add(t);
					 }
				 }
			 }
		 }
			 }
		 
	 }
	 public void getSurrondenemy (ArrayList<Damageable> targets) {
		 Champion c = getCurrentChampion();
		 Point p = c.getLocation();
		 int x = (int)p.getX();
		 int y = (int)p.getY();
		 Champion t ;
		 if((x+1)<5) {
			 for(int i = -1 ; i<=1; i++) {
				 int s = y +i ; 
				 if(s<5 &&s>=0) {
					 if(board[x+1][y+i]!= null) {
						 if(board[x+1][y+i] instanceof Champion) {
							 t= (Champion)board[x+1][y+i] ;
							 if(!arefriends(c, t))
								 targets.add(t);
						 }
					 }
				 }
			 }
		 }
		 if((x-1)>=0) {
			 for(int i = -1 ; i<=1; i++) {
				 int s = y +i ; 
				 if(s<5 &&s>=0) {
					 if(board[x-1][y+i]!= null) {
						 if(board[x-1][y+i] instanceof Champion) {
							 t= (Champion)board[x-1][y+i] ;
							 if(!arefriends(c, t))
								 targets.add(t);
						 }
					 }
				 }
			 }
		 }
		 for(int i = -1 ; i<=1; i++) {
			 if(i != 0) {
			 int s = y +i ; 
			 if(s<5 &&s>=0) {
				 if(board[x][y+i]!= null) {
					 if(board[x][y+i] instanceof Champion) {
						 t= (Champion)board[x][y+i] ;
						 if(!arefriends(c, t))
							 targets.add(t);
					 }
				 }
			 }
		 }
			 }
		 
	 }
	 public void getSurrondCover (ArrayList<Damageable> targets) {
		 Champion c = getCurrentChampion();
		 Point p = c.getLocation();
		 int x = (int)p.getX();
		 int y = (int)p.getY();
		 Cover t ;
		 if((x+1)<5) {
			 for(int i = -1 ; i<=1; i++) {
				 int s = y +i ; 
				 if(s<5 &&s>=0) {
					 if(board[x+1][y+i]!= null) {
						 
					  if(board[x+1][y+i] instanceof Cover) {
						  t= (Cover)board[x+1][y+i] ;
							 targets.add(t);
					  } 
					 }
				 }
			 }
		 }
		 if((x-1)>=0) {
			 for(int i = -1 ; i<=1; i++) {
				 int s = y +i ; 
				 if(s<5 &&s>=0) {
					 if(board[x-1][y+i]!= null) {
						 if(board[x-1][y+i] instanceof Cover) {
							 t= (Cover)board[x-1][y+i] ;
								 targets.add(t);
						 }
					 }
				 }
			 }
		 }
		 for(int i = -1 ; i<=1; i++) {
			 if(i != 0) {
			 int s = y +i ; 
			 if(s<5 &&s>=0) {
				 if(board[x][y+i]!= null) {
					 if(board[x][y+i] instanceof Cover) {
						 t= (Cover)board[x][y+i] ;
							 targets.add(t);
					 }
				 }
			 }
		 }
			 }
		 
	 }
	
	
	
	
	
	
	public void move (Direction d) throws ChampionDisarmedException, NotEnoughResourcesException, CloneNotSupportedException, UnallowedMovementException {
		Champion c = getCurrentChampion();
		if (d==null)
			throw new UnallowedMovementException();
		if (c.getCurrentActionPoints()<1)
			throw new NotEnoughResourcesException();
		Point p = c.getLocation();
		 for (Effect e: c.getAppliedEffects()) {
	     if (e instanceof Root)
	       throw new UnallowedMovementException();
		 }
		  int x=(int)p.getX();
		  int y=(int)p.getY();
		   switch(d) {
		   case UP : x = x+1 ; break ;
		   case DOWN : x = x-1 ; break ;
		   case RIGHT : y= y+1 ;break; 
		   case LEFT : y = y-1 ; break ; 
		   default : break; 
		   }
		/*  if (d == Direction.UP);
		  x=x+1;
		  
		  if (d==Direction.DOWN);
		  x=x-1;
		  
		  if (d==Direction.RIGHT);
		  y=y+1;
		  
		  if (d==Direction.LEFT);
		  y=y-1;
		  */
		  if ( x<0||y<0||x>4||y>4 )
			  throw new UnallowedMovementException();
		  if(board[x][y]!= null)
			  throw new UnallowedMovementException();
			  
		  this.getBoard()[x][y]= getCurrentChampion();
		  this.getBoard()[p.x][p.y]=null;
		  c.setLocation(new Point(x,y));
		  c.setCurrentActionPoints(c.getCurrentActionPoints()-1);
		   	
	}
	public void endTurn() throws CloneNotSupportedException {
		this.turnOrder.remove();
		if(this.turnOrder.isEmpty())
			prepareChampionTurns();
		//PriorityQueue temp = new PriorityQueue(6);
		while(((Champion)(turnOrder.peekMin())).getCondition() == Condition.INACTIVE) {
			dectimers();
			turnOrder.remove();
			if(turnOrder.isEmpty()) {
				prepareChampionTurns();	
			}
		}
		dectimers();
		Champion c = (Champion) turnOrder.peekMin();
		c.setCurrentActionPoints(c.getMaxActionPointsPerTurn());
	/*	do {
			this.turnOrder.remove();
			if(turnOrder.isEmpty()) {
				prepareChampionTurns();	
			}
			dectimers();
			
		}while(((Champion)(turnOrder.peekMin())).getCondition() == Condition.INACTIVE);
		*/
		
	}
	public void dectimers() throws CloneNotSupportedException {
		Champion c = (Champion) turnOrder.peekMin();
		//c.setCurrentActionPoints(c.getMaxActionPointsPerTurn());
		
		for(int i = 0 ; i< c.getAppliedEffects().size(); i++) {	
			int d = c.getAppliedEffects().get(i).getDuration();
			c.getAppliedEffects().get(i).setDuration(d-1);
			if(d <=1 ) {
			//	Effect temp = (Effect)c.getAppliedEffects().get(i).clone();
				//c.getAppliedEffects().remove(i);
				Effect temp = c.getAppliedEffects().remove(i);
				temp.remove(c);	
				i--;
			}
			
		}
		for(int i = 0 ; i < c.getAbilities().size(); i++) {
			int d = c.getAbilities().get(i).getCurrentCooldown();
			c.getAbilities().get(i).setCurrentCooldown(d-1);
		}
	}
	public Champion getNextChampion() {
		boolean flag = false ;
		
		Champion temp = (Champion)turnOrder.remove();
		if(this.turnOrder.isEmpty()) {
			prepareChampionTurns();
			flag = true ;
			}
		Champion r = (Champion)turnOrder.peekMin();
		if(flag) {
			turnOrder = new PriorityQueue(6);
			turnOrder.insert(temp);
		}else {
		turnOrder.insert(temp);
		}
		return r ;
		}
	// M2 
}
