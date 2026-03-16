package mahjongutils.cli

enum class Language {
    ZH, EN, JA;
    
    companion object {
        fun detect(): Language {
            val lang = System.getenv("LANG") ?: System.getenv("LC_ALL") ?: ""
            return when {
                lang.startsWith("zh", ignoreCase = true) -> ZH
                lang.startsWith("ja", ignoreCase = true) -> JA
                else -> EN
            }
        }
    }
}

interface Messages {
    val agari: String
    val tenpai: String
    fun shanten(n: Int): String
    val advance: String
    val goodShapeAdvance: String
    val improvement: String
    val goodShapeImprovement: String
    val discardChoice: String
    val discard: String
    val yaku: String
    val han: String
    val hu: String
    val parentTsumo: String
    val childTsumo: String
    val parentRon: String
    val childRon: String
    val parent: String
    val child: String
    val types: String
    val tiles: String
    val points: String
    val winningTile: String
    val tsumo: String
    val ron: String
    val dora: String
    
    companion object {
        fun get(lang: Language): Messages = when (lang) {
            Language.ZH -> ChineseMessages
            Language.EN -> EnglishMessages
            Language.JA -> JapaneseMessages
        }
    }
}

object ChineseMessages : Messages {
    override val agari = "和牌"
    override val tenpai = "听牌"
    override fun shanten(n: Int) = "${n}向听"
    override val advance = "进张"
    override val goodShapeAdvance = "好型进张"
    override val improvement = "改良张"
    override val goodShapeImprovement = "好型改良张"
    override val discardChoice = "弃牌选择"
    override val discard = "打"
    override val yaku = "役种"
    override val han = "番数"
    override val hu = "符数"
    override val parentTsumo = "亲家自摸"
    override val childTsumo = "子家自摸"
    override val parentRon = "亲家荣和"
    override val childRon = "子家荣和"
    override val parent = "亲"
    override val child = "子"
    override val types = "种"
    override val tiles = "张"
    override val points = "点"
    override val winningTile = "和牌张"
    override val tsumo = "自摸"
    override val ron = "荣和"
    override val dora = "宝牌"
}

object EnglishMessages : Messages {
    override val agari = "Winning Hand"
    override val tenpai = "Tenpai"
    override fun shanten(n: Int) = "$n-shanten"
    override val advance = "Advance"
    override val goodShapeAdvance = "Good Shape Advance"
    override val improvement = "Improvement"
    override val goodShapeImprovement = "Good Shape Improvement"
    override val discardChoice = "Discard Options"
    override val discard = "Discard"
    override val yaku = "Yaku"
    override val han = "Han"
    override val hu = "Fu"
    override val parentTsumo = "Parent Tsumo"
    override val childTsumo = "Child Tsumo"
    override val parentRon = "Parent Ron"
    override val childRon = "Child Ron"
    override val parent = "Parent"
    override val child = "Child"
    override val types = "types"
    override val tiles = "tiles"
    override val points = "points"
    override val winningTile = "Winning tile"
    override val tsumo = "Tsumo"
    override val ron = "Ron"
    override val dora = "Dora"
}

object JapaneseMessages : Messages {
    override val agari = "和了"
    override val tenpai = "聴牌"
    override fun shanten(n: Int) = "${n}向聴"
    override val advance = "有効牌"
    override val goodShapeAdvance = "良形有効牌"
    override val improvement = "改良牌"
    override val goodShapeImprovement = "良形改良牌"
    override val discardChoice = "打牌選択"
    override val discard = "打"
    override val yaku = "役"
    override val han = "翻数"
    override val hu = "符数"
    override val parentTsumo = "親のツモ"
    override val childTsumo = "子のツモ"
    override val parentRon = "親のロン"
    override val childRon = "子のロン"
    override val parent = "親"
    override val child = "子"
    override val types = "種"
    override val tiles = "枚"
    override val points = "点"
    override val winningTile = "和了牌"
    override val tsumo = "ツモ"
    override val ron = "ロン"
    override val dora = "ドラ"
}
