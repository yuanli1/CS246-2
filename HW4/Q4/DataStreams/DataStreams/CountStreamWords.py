import math
import concurrent.futures
class CountStreamWords(object):
    num_slots_per_hash = 0
    num_hash_functions = 0;
    functions = []
    hash_matrix =[[]]
    executors = concurrent.futures.ThreadPoolExecutor(5)
    def __init__(self, hash_functions, num_hash_funcs, num_slots):
        self.functions = hash_functions
        self.num_slots_per_hash = num_slots
        self.num_hash_functions = num_hash_funcs
        self.hash_matrix = [[0 for x in range(self.num_slots_per_hash)] for x in range(self.num_hash_functions)]

    def add(self,index):
        for i in range(self.num_hash_functions):
            hash_val = self.functions[i](index)
            self.hash_matrix[i][hash_val] += 1

    def single_add_func(self, id, hash_function_index):
        hash_val = self.functions[hash_function_index](index)
        self.hash_matrix[hash_function_index][hash_val] += 1

    def parallel_add(self, id):
        futures = [self.executors.submit(self.single_add_func, id, hash_func_id) for hash_func_id in range(5)]
        #concurrent.futures.wait(futures) 
            



