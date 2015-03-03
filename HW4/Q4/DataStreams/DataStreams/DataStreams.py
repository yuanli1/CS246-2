import math
import time
from hash_func import *
from DataStreamUtilities import *
from CountStreamWords import *
from pprint import pprint as pp
import concurrent.futures

def main():

    paramsFilePath = '.\data\hash_params.txt'
    wordsStreamFilePath = '.\data\words_stream_tiny.txt'
    countsFilePath = '.\data\counts_tiny.txt'
    epsilon = math.pow(10, -4) * math.e
    delta = math.pow((math.e), -5)
    num_hash_function = math.ceil(math.log(1/delta))
    num_slots_per_hash = math.ceil(math.e / epsilon)
    funcs_object = hash_func(paramsFilePath, 123457, num_slots_per_hash)
    streamCount = CountStreamWords(funcs_object.functions,num_hash_function, num_slots_per_hash)
    count = 0
    chunk_size = 10
    executors = concurrent.futures.ThreadPoolExecutor(chunk_size)
    processes = concurrent.futures.ProcessPoolExecutor(chunk_size)
    startTime = time.time()
    with open(wordsStreamFilePath, 'r') as file:
        while True:
            words = file.readlines(chunk_size)
            count += chunk_size
            if (count % 100) == 0:
                print('elapsed time:{0:.3f}, count:{1}'.format(time.time() - startTime, count))
            futures = [processes.submit(streamCount.add(int(word))) for word in words]
            
            concurrent.futures.wait(futures)
            #for word in words:
            #    count += 1
            #    if (count % 100) == 0:
            #        print('elapsed time:{0:.3f}, count:{1}'.format(time.time() - startTime, count))
            #        break
            #    if not word:
            #        break
            #    streamCount.add(int(word))

    elapsedTime = time.time() - startTime
    print('elapsed time:{0:.3f}, count:{1}'.format(elapsedTime, count))
   # pp(streamCount.hash_matrix)

if __name__ == "__main__":
    main()
