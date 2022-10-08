def next_k_bit_number(x: int) -> int:
    # REF: http://realtimecollisiondetection.net/blog/?p=78
    b = x & -x
    t = x + b
    c = x ^ t
    m = (c >> 2) // b
    r = t | m
    return r


def generate_k_bit_number(k: int):
    x = (1 << k) - 1
    while True:
        yield x
        x = next_k_bit_number(x)
