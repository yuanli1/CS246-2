import math
class CountStreamWords(object):
    num_slots_per_hash = 0
    num_hash_functions = 0;
    functions = []
    hash_matrix =[[]]
    def __init__(self, hash_functions, num_hash_funcs, num_slots):
        self.functions = hash_functions
        self.num_slots_per_hash = num_slots
        self.num_hash_functions = num_hash_funcs
        self.hash_matrix = [[0 for x in range(self.num_slots_per_hash)] for x in range(self.num_hash_functions)]

    def add(self,index):
        for i in range(self.num_hash_functions):
            hash_val = self.functions[i](index)
            self.hash_matrix[i][hash_val] += 1

