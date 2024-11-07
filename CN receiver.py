def read_channel():
    with open("Channel.txt", "r") as f:
        return f.read()

def detect_error(arr, nr):
    n = len(arr)
    res = 0

    for i in range(nr):
        val = 0
        for j in range(1, n + 1):
            if(j & (2**i) == (2**i)):
                val = val ^ int(arr[-1 * j])

        res = res + val * (10**i)

    return int(str(res), 2)

def correct_error(arr, pos):
    if pos != 0:
        arr = arr[:pos-1] + str(1-int(arr[pos-1])) + arr[pos:]
    return arr

def remove_redundant_bits(arr, r):
    n = len(arr)
    res = ""
    j = 0

    for i in range(1, n + 1):
        if(i != (2**j)):
            res = res + arr[-1 * i]
        else:
            j += 1

    return res[::-1]

def binary_to_text(binary_data):
    text = ""
    for i in range(0, len(binary_data), 8):
        byte = binary_data[i:i+8]
        text += chr(int(byte, 2))
    return text

def calculate_redundant_bits(m):
    for i in range(m):
        if (2**i >= m + i + 1):
            return i

def receiver():
    hamming_data = read_channel()
    nr = calculate_redundant_bits(len(hamming_data))
    error_pos = detect_error(hamming_data, nr)
    
    if error_pos == 0:
        print("No error detected.")
        correct_data = hamming_data
    else:
        print(f"Error detected at position: {error_pos}")
        correct_data = correct_error(hamming_data, error_pos)
    
    data_without_redundant_bits = remove_redundant_bits(correct_data, nr)
    ascii_text = binary_to_text(data_without_redundant_bits)
    print(f"Received text: {ascii_text}")

# Example usage
receiver()
