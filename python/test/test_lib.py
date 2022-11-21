from mahjong_utils.lib import libmahjongutils


def test_lib():
    result = libmahjongutils.call("regularShanten", {"tiles": ["1m", "1m", "1m", "2m"]})
    print(result)

    assert result['shantenInfo']['type'] == 'ShantenWithoutGot'
    assert result['shantenInfo']['shantenNum'] == 0
