package card.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardNameConstants {

	@SuppressWarnings("serial")
	public static final Map<String,Integer> BLOCK_AMOUNT = new HashMap<String,Integer>() 
	{{ 
		put("Mocchi_Round",3);
		put("Dino_Endure",3);
	}};
	
	@SuppressWarnings("serial")
	public static final Map<String,Integer> GUTS_DAMAGE = new HashMap<String,Integer>()
	{{
		put("Mocchi_Licking", 1);
	}};
	
	@SuppressWarnings("serial")
	public static final Map<String,Integer> RECOIL_DAMAGE = new HashMap<String,Integer>()
	{{
		put("Mocchi_Press", 1);
		put("Tiger_Charge", 1);
		put("Dino_Charge", 1);
		put("Dino_Fire Dash", 3);
	}};
	
	@SuppressWarnings("serial")
	public static final List<String> CANT_DODGE = new ArrayList<String>()
	{{
		add("Mocchi_Thrust");
		add("Tiger_Sonic Move");
		add("Dino_Leap Kick");
	}};
	
	@SuppressWarnings("serial")
	public static final List<String> HALF_DODGE = new ArrayList<String>()
	{{
		add("Mocchi_1-2 Thrust");
	}};
	
	@SuppressWarnings("serial")
	public static final List<String> AOE_DAMAGE = new ArrayList<String>()
	{{
		add("Mocchi_Petal Wind");
		add("Tiger_Roar");
	}};
	
	@SuppressWarnings("serial")
	public static final List<String> TIGER_COMBO = new ArrayList<String>()
	{{
		add("Tiger_Left Claw");
		add("Tiger_Right Claw");
		add("Tiger_Horn");
	}};
	
	@SuppressWarnings("serial")
	public static final List<String> USE_AFTER_POW = new ArrayList<String>()
	{{
		add("Tiger_Sonic Move");
		add("Tiger_Jump");
		add("Dino_Endure");
		add("Dino_Leap Kick");
	}};
	
	@SuppressWarnings("serial")
	public static final List<String> STUN = new ArrayList<String>()
	{{
		add("Tiger_Ice Bomb");
		add("Tiger_Roar");
		add("Dino_Dust Kick");
		add("Dino_Throw Away");
	}};
	
	//Special Cards
	public static final String MOCCHI_HIDE = "Mocchi_Hide";
}
