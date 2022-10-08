from mahjong_utils.algorithm.syanten import syanten
from mahjong_utils.models.tile import tiles

hand = tiles("1122m2356689p78s3z")
ans = syanten(hand)
print(ans)
