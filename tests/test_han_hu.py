import pytest

from mahjong_utils.point_by_han_hu import get_parent_point_by_han_hu, get_child_point_by_han_hu


def test_get_parent_point_by_han_hu():
    assert get_parent_point_by_han_hu(2, 30) == (2900, 1000)
    assert get_parent_point_by_han_hu(4, 40) == (12000, 4000)
    assert get_parent_point_by_han_hu(5, 40) == (12000, 4000)
    assert get_parent_point_by_han_hu(16, 40) == (48000, 16000)

    with pytest.raises(ValueError):
        get_parent_point_by_han_hu(-1, 20)

    with pytest.raises(ValueError):
        get_parent_point_by_han_hu(3, 23)

    with pytest.raises(ValueError):
        get_parent_point_by_han_hu(114, 514)


def test_get_child_point_by_han_hu():
    assert get_child_point_by_han_hu(2, 30) == (2000, 1000, 500)
    assert get_child_point_by_han_hu(4, 40) == (8000, 4000, 2000)
    assert get_child_point_by_han_hu(5, 40) == (8000, 4000, 2000)
    assert get_child_point_by_han_hu(16, 40) == (32000, 16000, 8000)

    with pytest.raises(ValueError):
        get_child_point_by_han_hu(-1, 20)

    with pytest.raises(ValueError):
        get_child_point_by_han_hu(3, 23)

    with pytest.raises(ValueError):
        get_child_point_by_han_hu(114, 514)
