package me.theheyway.GPP.Util;

import java.util.HashMap;
import java.util.Map;

//Thanks to bukkit for most of the ideas here

public enum GPPMaterial {
	AIR(0),
	APPLE(260),
	ARROW(262),
	BED(355),
	BEDBLOCK(26),
	BEDROCK(7),
	BLAZEPOWDER(377),
	BLAZEROD(369),
	BOAT(333),
	BONE(352),
	BOOK(340),
	BOOKSHELF(47),
	BOW(261),
	BOWL(281),
	BREAD(297),
	BREWINGSTANDBLOCK(117), // Swapped out BREWINGSTANDITEM Bukkit.org.Material for this instead; makes more sense for this plugin's uses
	BREWINGSTAND(379), //117 is the actual block, players will only be using 379 to spawn items
	BRICK(45),
	BRICKSTAIRS(108),
	BROWNMUSHROOM(39),
	BUCKET(325),
	BURNINGFURNACE(62),
	ONFURNACE(62),
	CACTUS(81),
	CAKE(354),
	CAKEBLOCK(92),
	CAULDRON(380), // same case as brewing stand
	CAULDRONBLOCK(118),
	//Chainmail items
	CHAINMAILBOOTS(305),
	CHAINMAILSHOES(305),
	CHAINMAILFEET(305),
	CHAINBOOTS(305),
	CHAINSHOES(305),
	CHAINFEET(305),
	CMBOOTS(305),
	CMSHOES(305),
	CMFEET(305),
	CHAINMAILCHESTPLATE(303), // Add more ways to refer to the same thing, we'll be seeing this a lot
	CHAINCHESTPLATE(303),
	CMCHESTPLATE(303),
	CHAINMAILCHEST(303),
	CHAINCHEST(303),
	CMCHEST(303),
	CHAINMAILPLATE(303),
	CHAINPLATE(303),
	CMLATE(303),
	CHAINMAILHELMET(302),
	CHAINMAILHEAD(302),
	CHAINHELMET(302),
	CHAINHEAD(302),
	CMHELMET(302),
	CMHEAD(302),
	CHAINMAILLEGGINGS(304),
	CHAINMAILPANTS(304),
	CHAINMAILLEGS(304),
	CHAINLEGGINGS(304),
	CHAINPANTS(304),
	CHAINLEGS(304),
	CMLEGGINGS(304),
	CMPANTS(304),
	CMLEGS(304),
	CHEST(54),
	CLAY(337), // block ID = 82, referring to by Item ID 337
	CLAYBALL(337),
	CLAYBRICK(336),
	COAL(263),
	COALORE(16),
	COBBLESTONE(4),
	COBBLESTONESTAIRS(67),
	COMPASS(345),
	COOKEDBEEF(364),
	COOKEDCHICKEN(366),
	COOKEDFISH(350),
	COOKIE(357),
	CROPS(59),
	DEADBUSH(32),
	DETECTORRAIL(28),
	DRAIL(28),
	//Diamond stuff
	DIAMOND(264),
	DIAMONDAXE(279),
	DAXE(279),
	DIAMONDBLOCK(57),
	DBLOCK(57),
	DIAMONDBOOTS(313),
	DBOOTS(313),
	DIAMONDCHESTPLATE(311),
	DCHESTPLATE(311),
	DIAMONDHELMET(310),
	DHELMET(310),
	DHEAD(310),
	DIAMONDHOE(293),
	DHOE(293),
	DIAMONDLEGGINGS(312),
	DIAMONDPANTS(312),
	DIAMONDLEGS(312),
	DLEGGINGS(312),
	DPANTS(312),
	DLEGS(312),
	DIAMONDORE(56),
	DORE(56),
	DIAMONDPICKAXE(278),
	DIAMONDPICK(278),
	DPICKAXE(278),
	DPICK(278),
	DIAMONDSPADE(277),
	DSPADE(277),
	DIAMONDSHOVEL(277),
	DSHOVEL(277),
	DIAMONDSWORD(276),
	DSWORD(276),
	DIODE(356),
	DIODEBLOCKOFF(93),
	DIODEBLOCKON(94),
	DIODEOFF(93),
	DIODEON(94),
	DIRT(3),
	DISPENSER(23),
	DOUBLESTEP(43),
	DRAGONEGG(122),
	EGG(344),
	ENCHANTMENTTABLE(116),
	ENCHANTTABLE(116),
	ENDERPEARL(368),
	ENDERPORTAL(119),
	ENDERPORTALFRAME(120),
	ENDERSTONE(121),
	ENDSTONE(121),
	EYEOFENDER(381),
	FEATHER(288),
	FENCE(85),
	FENCEGATE(107),
	FERMENTEDSPIDEREYE(376),
	FIRE(51),
	FISHINGROD(346),
	FLINT(318),
	FLINTANDSTEEL(259),
	FURNACE(61),
	GHASTTEAR(370),
	GLASS(20),
	GLASSBOTTLE(374),
	GLOWINGREDSTONEORE(74),
	GLOWSTONE(89),
	GLOWSTONEDUST(348),
	GOLDAXE(286),
	GAXE(286),
	GOLDBLOCK(41),
	GBLOCK(41),
	GOLDBOOTS(317),
	GOLDSHOES(317),
	GOLDFEET(317),
	GBOOTS(317),
	GSHOES(317),
	GFEET(317),
	GOLDCHESTPLATE(315),
	GOLDPLATE(315),
	GOLDCHEST(315),
	GCHESTPLATE(315),
	GPLATE(315),
	GCHEST(315),
	GOLDHELMET(314),
	GOLDHEAD(314),
	GHELMET(314),
	GHEAD(314),
	GOLDHOE(294),
	GOLDINGOT(266),
	GINGOT(266),
	GOLDLEGGINGS(316),
	GOLDPANTS(316),
	GOLDLEGS(316),
	GLEGGINGS(316),
	GPANTS(316),
	GLEGS(316),
	GOLDNUGGET(371),
	GNUGGET(371),
	GOLDORE(14),
	GORE(14),
	GOLDPICKAXE(285),
	GOLDPICK(285),
	GPICKAXE(285),
	GPICK(285),
	GOLDRECORD(2256),
	GOLDSPADE(284),
	GOLDSHOVEL(284),
	GSHOVEL(284),
	GSPADE(284),
	GOLDSWORD(283),
	GSWORD(283),
	GOLDENAPPLE(322),
	GAPPLE(322),
	GRASS(2),
	GRAVEL(13),
	GREENRECORD(2257),
	GRILLEDPORK(320),
	HUGEMUSHROOM1(99),
	HUGEMUSHROOM2(100),
	ICE(79),
	INKSACK(351),
	IRONAXE(258),
	IAXE(258),
	IRONBLOCK(42),
	IBLOCK(42),
	IRONBOOTS(309),
	IRONSHOES(309),
	IRONFEET(309),
	IBOOTS(309),
	ISHOES(309),
	IFEET(309),
	IRONCHESTPLATE(307),
	IRONPLATE(307),
	IRONCHEST(307),
	ICHESTPLATE(307),
	IPLATE(307),
	ICHEST(307),
	IRONDOOR(330),
	IDOOR(330),
	IRONDOORBLOCK(71),
	IDOORBLOCK(71),
	IRONFENCE(101),
	IFENCE(101),
	IRONHELMET(306),
	IRONHEAD(306),
	IHELMET(306),
	IHEAD(306),
	IRONINGOT(265),
	IINGOT(265),
	IRONLEGGINGS(308),
	IRONPANTS(308),
	IRONLEGS(308),
	ILEGGINGS(308),
	IPANTS(308),
	ILEGS(308),
	IRONORE(15),
	IORE(15),
	IRONPICKAXE(257),
	IRONPICK(257),
	IPICKAXE(257),
	IPICK(257),
	IRONSPADE(256),
	IRONSHOVEL(256),
	ISPADE(256),
	ISHOVEL(256),
	IRONSWORD(267),
	ISWORD(267),
	JACKOLANTERN(91),
	JUKEBOX(84),
	LADDER(65),
	LAPISBLOCK(22),
	LAPISORE(21),
	LAVA(10),
	LAVABUCKET(327),
	LEATHER(334),
	LEATHERBOOTS(301),
	LEATHERSHOES(301),
	LEATHERFEET(301),
	LBOOTS(301),
	LSHOES(301),
	LFEET(301),
	LEATHERCHESTPLATE(299),
	LEATHERPLATE(299),
	LEATHERCHEST(299),
	LCHESTPLATE(299),
	LPLATE(299),
	LCHEST(299),
	LEATHERHELMET(298),
	LEATHERHEAD(298),
	LHELMET(298),
	LHEAD(298),
	LEATHERLEGGINGS(300),
	LEATHERPANTS(300),
	LEATHERLEGS(300),
	LLEGGINGS(300),
	LPANTS(300),
	LLEGS(300),
	LEAVES(18),
	LEVER(69),
	LOCKEDCHEST(95),
	LOG(17),
	LONGGRASS(31),
	MAGMACREAM(378),
	MAP(358),
	MELON(360),
	MELONBLOCK(103),
	MELONSEEDS(362),
	MELONSTEM(105),
	MILKBUCKET(335),
	MINECART(328),
	MOBSPAWNER(52),
	MONSTEREGGS(97),
	MOSSYCOBBLESTONE(48),
	MOSSYCOBBLE(48),
	MUSHROOMSOUP(282),
	MYCELIUM(110),
	MYCEL(110),
	NETHERBRICK(112),
	NBRICK(112),
	NETHERBRICKSTAIRS(114),
	NBRICKSTAIRS(114),
	NETHERFENCE(113),
	NFENCE(113),
	NETHERSTALK(372),
	NSTALK(372),
	NETHERWARTS(115),
	NWARTS(115),
	NETHERRACK(87),
	NRACK(87),
	NOTEBLOCK(25),
	OBSIDIAN(49),
	PAINTING(321),
	PAPER(339),
	PISTONBASE(33),
	PISTONEXTENSION(34),
	PISTONMOVINGPIECE(36),
	PISTONSTICKYBASE(29),
	PORK(319),
	PORTAL(90),
	POTION(373),
	POWEREDMINECART(343),
	POWEREDCART(343),
	POWEREDRAIL(27),
	BOOSTERRAIL(27),
	PUMPKIN(86),
	PUMPKINSEEDS(361),
	PUMPKINSTEM(104),
	RAILS(66),
	RAIL(66),
	TRACK(66),
	RAWBEEF(363),
	RAWCHICKEN(365),
	RAWFISH(349),
	RECORD10(2265),
	RECORD11(2266),
	RECORD3(2258),
	RECORD4(2259),
	RECORD5(2260),
	RECORD6(2261),
	RECORD7(2262),
	RECORD8(2263),
	RECORD9(2264),
	REDMUSHROOM(40),
	REDROSE(38),
	REDSTONE(331),
	REDSTONEORE(73),
	REDSTONETORCHOFF(75),
	REDSTONETORCHON(76),
	REDSTONEWIRE(55),
	ROTTENFLESH(367),
	SADDLE(329),
	SAND(12),
	SANDSTONE(24),
	SAPLING(6),
	SEEDS(295),
	SHEARS(359),
	SIGN(323),
	SIGNPOST(63),
	SLIMEBALL(341),
	SMOOTHBRICK(98),
	SMOOTHSTAIRS(109),
	SNOW(78),
	SNOWBALL(332),
	SNOWBLOCK(80),
	SOIL(60),
	SOULSAND(88),
	SPECKLEDMELON(382),
	SPIDEREYE(375),
	SPONGE(19),
	STATIONARYLAVA(9),
	STATIONARYWATER(11),
	STICK(280),
	STONE(1),
	STONEAXE(275),
	SAXE(275),
	STONEBUTTON(77),
	STONEHOE(291),
	SHOE(291),
	STONEPICKAXE(274),
	STONEPICK(274),
	SPICKAXE(274),
	SPICK(274),
	STONEPLATE(70),
	SPLATE(70),
	STONESPADE(273),
	STONESHOVEL(273),
	SSPADE(273),
	SSHOVEL(273),
	STONESWORD(272),
	SSWORD(272),
	STORAGEMINECART(342),
	STORAGECART(342),
	STRING(287),
	SUGAR(353),
	SUGARCANE(338),
	SUGARCANEBLOCK(83),
	SULPHUR(289),
	GUNPOWDER(289),
	THINGLASS(102),
	TNT(46),
	BOOM(46),
	TORCH(50),
	TRAPDOOR(96),
	VINE(106),
	WALLSIGN(68),
	WATCH(347),
	WATER(8),
	WATERBUCKET(326),
	WATERLILY(111),
	LILYPAD(111),
	WEB(30),
	WHEAT(296),
	WOOD(5),
	WOODAXE(271),
	WAXE(271),
	WOODDOOR(324),
	WDOOR(324),
	DOOR(324), // this is what most people mean when they say "door", right?
	WOODHOE(290),
	WHOE(290),
	WOODPICKAXE(270),
	WOODPICK(270),
	WPICKAXE(270),
	WPICK(270),
	WOODPLATE(72),
	WPLATE(72),
	WOODSPADE(269),
	WOODSHOVEL(269),
	WSPADE(269),
	WSHOVEL(269),
	WOODSTAIRS(53),
	WSTAIRS(53),
	WOODSWORD(269),
	WSWORD(269),
	WOODENDOOR(64), // wooddoor vs woodendoor, that's not confusing. this is a block. I can tell by the numbers and having seen quite a few bukkit.org.Materials in my time
	WOOL(35),
	WORKBENCH(58),
	YELLOWFLOWER(37);
	//who typed and looked up all this by hand? that's right, me, and it was fucking exhausting

	private final int id;
	private static final Map<Integer, GPPMaterial> lookupId = new HashMap<Integer, GPPMaterial>();
	private static final Map<String, GPPMaterial> lookupName = new HashMap<String, GPPMaterial>();
	
	private GPPMaterial(final int id) {
		this.id = id;
	}
	
	public int getID() {
		return id;
	}
	
	static {
        for (GPPMaterial material : values()) {
            lookupId.put(material.getID(), material);
            lookupName.put(material.name(), material);
        }
    }

}
