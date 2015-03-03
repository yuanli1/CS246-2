from array import array
class hash_func(object):
    params = list()
    functions = list()
    p = 0;
    n_buckets = 0;
    def __init__(self, parameters_path, p, n_buckets):
        self.p = p
        self.n_buckets = n_buckets
        with open(parameters_path, 'r') as file:
            while True:
                line = file.readline()
                if not line:
                    break
                self.params.append(line)                
        
        for line in self.params:
            parameters = line.split()
            a = int(parameters[0])
            b = int(parameters[1])
            self.functions.append(self.hash_fun(a,b))

    def hash_fun(self, a, b):
        def newFunc(x):
            y = x % self.p
            hash_val = (a*y+b) % self.p
            return hash_val % self.n_buckets
        return newFunc


