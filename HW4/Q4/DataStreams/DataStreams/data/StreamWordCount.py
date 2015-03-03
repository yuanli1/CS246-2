import math
class StreamWordCount(object):
    num_hash_functions = 0
    num_slots_per_hash = 0
    counts_hash_matrix = [[]]
    hash_functions = []
    def __init__(self, epsilon, delta, hash_functions):
        self.num_hash_functions = math.ceil(math.log(1/delta))
        self.num_slots_per_hash = math.ceil(math.e / epsilon)
        counts_hash_matrix = [[0 for x in range(0,self.num_slots_per_hash)] for x in self.num_hash_functions]
        self.hash_functions = hash_functions
    
    def add(self, point):
        for i in range(0, self.hash_functions.count()):
            hash_function = hash_functions(i)
            hash_val = hash_function(point)
            self.counts_hash_matrix[i][hash_val] += 1
                   