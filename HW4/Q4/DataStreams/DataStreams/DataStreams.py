import math
from hash_func import *
from DataStreamUtilities import *

def main():

    paramsFilePath = '.\data\hash_params.txt'
    wordsStreamFilePath = '.\data\words_stream_tiny.txt'
    countsFilePath = '.\data\counts_tiny.txt'
    epsilon = math.pow(10, -4) * math.e
    delta = math.pow((math.e), -5)
    num_buckets = math.ceil(math.log(1/delta))
    funcs_object = hash_func(paramsFilePath, 123457, 50)
    func1 = funcs_object.functions[0]
    func2 = funcs_object.functions[1]
    val1 = func1(1)
    val2 = func2(1)

if __name__ == "__main__":
    main()
