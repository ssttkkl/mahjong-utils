from mahjong_utils.bridge import bridge_mahjongutils


def test_bridge():
    result = bridge_mahjongutils.call("regularShanten", {"tiles": ["1m", "1m", "1m", "2m"]})
    print(result)

    assert result['shantenInfo']['type'] == 'ShantenWithoutGot'
    assert result['shantenInfo']['shantenNum'] == 0
