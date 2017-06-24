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
	}};
	
	@SuppressWarnings("serial")
	public static final List<String> CANT_DODGE = new ArrayList<String>()
	{{
		add("Mocchi_Thrust");
	}};
	
	@SuppressWarnings("serial")
	public static final List<String> HALF_DODGE = new ArrayList<String>()
	{{
		add("Mocchi_1-2 Thrust");
	}};
	
	@SuppressWarnings("serial")
	static final List<String> AOE_DAMAGE = new ArrayList<String>()
	{{
		add("Mocchi_Petal Wind");
	}};
	
	//Special Cards
	public static final String MOCCHI_HIDE = "Mocchi_Hide";
}
