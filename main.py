from mahjong_utils.syanten import syanten_with_got_tile
from mahjong_utils.models.tile import tiles

hand = tiles("34568m235p368s")
syanten, discard_advance_mapping = syanten_with_got_tile(hand)
print(syanten)
for t in discard_advance_mapping:
    print("打", end='')
    print(t, end=' ')

    print("进", end='')
    for t2 in sorted(discard_advance_mapping[t]):
        print(t2, end=' ')
    print()
