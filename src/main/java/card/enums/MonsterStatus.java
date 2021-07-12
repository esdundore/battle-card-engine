package card.enums;

public enum MonsterStatus { 
	/** This monster's type is AIR. **/
	AIRBORNE,
	/** This monster can use multiple skill cards this turn. **/
	BERSERK,
	/** This monster cannot use skills besides "Emerge" **/
	COCOON,
	/** Must combo with a POW attack. This attack becomes any POW card from your deck. **/
	COPY_CAT,
	/** All POW or INT attacks deal double damage. **/
	DMGx2,
	/** This breeder will get an extra turn after this one. **/
	EXTRA_TURN,
	/** All INT attacks will deal double damage next turn if this monster is not damaged. **/
	FOCUSINTx2,
	/** All POW attacks will deal double damage next turn if this monster is not damaged. **/
	FOCUSPOWx2,
	/** Cannot use "Grit" again. **/
	GRIT,
	/** Prevent damage to this monster. **/
	INVULNERABLE,
	/** All INT attacks deal double damage. **/
	INTx2,
	/** This breeder cannot lose guts. **/
	MEDITATING,
	/** Can use other attacks with the MULTI keyword. **/
	MULTI,
	/** Cannot use BLK skills. **/
	NOBLOCK,
	/** Cannot user DGE skills. **/
	NODODGE,
	/** All POW attacks deal double damage. **/
	POWx2,
	/** All POW attacks deal triple damage. **/
	POWx3,
	/** Cannot use attack skills. **/
	STUNNED,
	/** Opponent's monsters can only attack this monster. **/
	TAUNTING,
	/** This monster cannot be targeted by attacks. **/
	UNTARGETABLE
}