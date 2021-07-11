package card.util;

import java.util.ArrayList;
import java.util.Arrays;

import card.enums.SkillKeyword;

public class KeywordUtil {

	/** Deals damage to all monsters (including allies). **/
	public static final ArrayList<SkillKeyword> ALL = new ArrayList<>(Arrays.asList(
			SkillKeyword.ALL, 
			SkillKeyword.ALL_GRD));
	
	/** Deals damage to all enemy monsters. **/
	public static final ArrayList<SkillKeyword> AOE = new ArrayList<>(Arrays.asList(
			SkillKeyword.ALL,
			SkillKeyword.ALL_GRD,
			SkillKeyword.AOE, 
			SkillKeyword.AOE_AIR, 
			SkillKeyword.AOE_GRD,
			SkillKeyword.COMBO_DRAGON));
	
	/** Deals damage to AIR monsters only. **/
	public static final ArrayList<SkillKeyword> HIT_AIR_ONLY = new ArrayList<>(Arrays.asList(
			SkillKeyword.AOE_AIR));
	
	/** Deals damage to GRD monsters only. **/
	public static final ArrayList<SkillKeyword> HIT_GROUND_ONLY = new ArrayList<>(Arrays.asList(
			SkillKeyword.ALL_GRD,  
			SkillKeyword.AOE_GRD));
	
	/** Can only be used in response to a POW attack **/
	public static final ArrayList<SkillKeyword> POW_DEFENSE = new ArrayList<>(Arrays.asList(
			SkillKeyword.BLOCK_POW,
			SkillKeyword.BOUNCE,
			SkillKeyword.COUNTER,
			SkillKeyword.DGE_POW,
			SkillKeyword.FLY_AWAY,
			SkillKeyword.JUMP,
			SkillKeyword.JUMP_IN,
			SkillKeyword.SOAR));
	
	/** Can only be used in response to a INT attack **/
	public static final ArrayList<SkillKeyword> INT_DEFENSE = new ArrayList<>(Arrays.asList(
			SkillKeyword.BLOCK_INT,
			SkillKeyword.DEFLECT,
			SkillKeyword.DGE_INT));
	
	/** This defense can be used without being targeted **/
	public static final ArrayList<SkillKeyword> UTIL_DEFENSE = new ArrayList<>(Arrays.asList(
			SkillKeyword.BLOW_HOLE,
			SkillKeyword.INTERCEPT,
			SkillKeyword.PERSISTANCE,
			SkillKeyword.PRAY));
	
	/** Reduces damage from attack by x. **/
	public static final ArrayList<SkillKeyword> BLOCK = new ArrayList<>(Arrays.asList(
			SkillKeyword.BLOCK,
			SkillKeyword.BLOCK_POW,
			SkillKeyword.BLOCK_INT));
	
	/** Must combo with a POW or INT attack. **/
	public static final ArrayList<SkillKeyword> COMBO_POW_INT = new ArrayList<>(Arrays.asList(
			SkillKeyword.CONCENTRATE,
			SkillKeyword.CRITICAL,
			SkillKeyword.GRAND_END,
			SkillKeyword.POWER_OF_SUN,
			SkillKeyword.WILL_POWER));
	
	/** Must combo with a POW attack. **/
	public static final ArrayList<SkillKeyword> COMBO_POW = new ArrayList<>(Arrays.asList(
			SkillKeyword.COMBO_UNDGE,
			SkillKeyword.COPY_CAT));
	
	/** Must combo with another card **/
	public static final ArrayList<SkillKeyword> COMBO = new ArrayList<>(Arrays.asList(
			SkillKeyword.COMBO_UNDGE,
			SkillKeyword.CONCENTRATE,
			SkillKeyword.COPY_CAT,
			SkillKeyword.CRITICAL,
			SkillKeyword.GRAND_END,
			SkillKeyword.POWER_OF_SUN,
			SkillKeyword.SUPPORT,
			SkillKeyword.WILL_POWER));

}
