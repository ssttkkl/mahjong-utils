from mahjong_utils.shanten.models import Shanten, ShantenWithGot, ShantenWithFuroChance


def test_structural_shanten_codec_and_legacy_response_fallback():
    data = {
        "type": "ShantenWithoutGot",
        "shantenNum": 1,
        "structuralShantenNum": 0,
        "advance": [],
        "advanceNum": 0,
        "goodShapeAdvance": None,
        "goodShapeAdvanceNum": None,
        "improvement": None,
        "improvementNum": None,
        "goodShapeImprovement": None,
        "goodShapeImprovementNum": None,
    }

    result = Shanten.__decode__(data)
    assert result.shanten == 1
    assert result.structural_shanten == 0
    assert result.__encode__()["structuralShantenNum"] == 0

    legacy_data = {k: v for k, v in data.items() if k != "structuralShantenNum"}
    legacy_result = Shanten.__decode__(legacy_data)
    assert legacy_result.structural_shanten == legacy_result.shanten == 1
    assert legacy_result.__encode__()["structuralShantenNum"] == 1


def test_structural_shanten_is_exposed_and_encoded_for_all_shanten_variants():
    with_got = Shanten.__decode__({
        "type": "ShantenWithGot",
        "shantenNum": 1,
        "structuralShantenNum": 0,
        "discardToAdvance": {},
        "ankanToAdvance": {},
    })
    assert isinstance(with_got, ShantenWithGot)
    assert with_got.structural_shanten == 0
    assert with_got.__encode__()["structuralShantenNum"] == 0

    furo_chance = Shanten.__decode__({
        "type": "ShantenWithFuroChance",
        "shantenNum": 1,
        "structuralShantenNum": 0,
        "pass": None,
        "chi": {},
        "pon": None,
        "minkan": None,
    })
    assert isinstance(furo_chance, ShantenWithFuroChance)
    assert furo_chance.structural_shanten == 0
    assert furo_chance.__encode__()["structuralShantenNum"] == 0

    legacy_direct_model = ShantenWithGot(
        shanten=2,
        discard_to_advance={},
        ankan_to_advance={},
    )
    assert legacy_direct_model.structural_shanten == 2
