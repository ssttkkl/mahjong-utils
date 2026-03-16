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

object Messages {
    fun agari(lang: Language) = when (lang) {
        Language.ZH -> "和牌"
        Language.EN -> "Winning Hand"
        Language.JA -> "和了"
    }
    
    fun tenpai(lang: Language) = when (lang) {
        Language.ZH -> "听牌"
        Language.EN -> "Tenpai"
        Language.JA -> "聴牌"
    }
    
    fun shanten(n: Int, lang: Language) = when (lang) {
        Language.ZH -> "${n}向听"
        Language.EN -> "$n-shanten"
        Language.JA -> "${n}向聴"
    }
    
    fun advance(lang: Language) = when (lang) {
        Language.ZH -> "进张"
        Language.EN -> "Advance"
        Language.JA -> "有効牌"
    }
    
    fun goodShapeAdvance(lang: Language) = when (lang) {
        Language.ZH -> "好型进张"
        Language.EN -> "Good Shape Advance"
        Language.JA -> "良形有効牌"
    }
    
    fun improvement(lang: Language) = when (lang) {
        Language.ZH -> "改良张"
        Language.EN -> "Improvement"
        Language.JA -> "改良牌"
    }
    
    fun goodShapeImprovement(lang: Language) = when (lang) {
        Language.ZH -> "好型改良张"
        Language.EN -> "Good Shape Improvement"
        Language.JA -> "良形改良牌"
    }
    
    fun discardChoice(lang: Language) = when (lang) {
        Language.ZH -> "弃牌选择"
        Language.EN -> "Discard Options"
        Language.JA -> "打牌選択"
    }
    
    fun discard(lang: Language) = when (lang) {
        Language.ZH -> "打"
        Language.EN -> "Discard"
        Language.JA -> "打"
    }
    
    fun yaku(lang: Language) = when (lang) {
        Language.ZH -> "役种"
        Language.EN -> "Yaku"
        Language.JA -> "役"
    }
    
    fun han(lang: Language) = when (lang) {
        Language.ZH -> "番数"
        Language.EN -> "Han"
        Language.JA -> "翻数"
    }
    
    fun hu(lang: Language) = when (lang) {
        Language.ZH -> "符数"
        Language.EN -> "Fu"
        Language.JA -> "符数"
    }
    
    fun parentTsumo(lang: Language) = when (lang) {
        Language.ZH -> "亲家自摸"
        Language.EN -> "Parent Tsumo"
        Language.JA -> "親のツモ"
    }
    
    fun childTsumo(lang: Language) = when (lang) {
        Language.ZH -> "子家自摸"
        Language.EN -> "Child Tsumo"
        Language.JA -> "子のツモ"
    }
    
    fun parentRon(lang: Language) = when (lang) {
        Language.ZH -> "亲家荣和"
        Language.EN -> "Parent Ron"
        Language.JA -> "親のロン"
    }
    
    fun childRon(lang: Language) = when (lang) {
        Language.ZH -> "子家荣和"
        Language.EN -> "Child Ron"
        Language.JA -> "子のロン"
    }
    
    fun parent(lang: Language) = when (lang) {
        Language.ZH -> "亲"
        Language.EN -> "Parent"
        Language.JA -> "親"
    }
    
    fun child(lang: Language) = when (lang) {
        Language.ZH -> "子"
        Language.EN -> "Child"
        Language.JA -> "子"
    }
    
    fun types(lang: Language) = when (lang) {
        Language.ZH -> "种"
        Language.EN -> "types"
        Language.JA -> "種"
    }
    
    fun tiles(lang: Language) = when (lang) {
        Language.ZH -> "张"
        Language.EN -> "tiles"
        Language.JA -> "枚"
    }
    
    fun points(lang: Language) = when (lang) {
        Language.ZH -> "点"
        Language.EN -> "points"
        Language.JA -> "点"
    }
}
