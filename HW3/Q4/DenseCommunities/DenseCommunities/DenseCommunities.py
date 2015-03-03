import pprint 
import time

inputFilePath = 'livejournal-undirected-small.txt'
#inputFilePath = 'sanitySmall.txt'

chunk_size = 20000

def readFileAndInitializeDegrees(FileName, chunk_size):
    largestVertexIndex = 0
    currentRead = 0;
    startTime = time.time()
    with open(FileName, 'r') as file:
        while True:
            lines = file.readlines(chunk_size)
            currentRead += 1
            if ( currentRead % 1000 == 0):
                print('Currently read in MB:', currentRead * chunk_size/ (1000000))
                print("{0:.4f}".format(time.time() - startTime))
            #print('A new chunk:')
            if not lines:
                 break
            for line in lines:
                items = line.split()
                for item in items:
                    if (largestVertexIndex < int(item)):
                        largestVertexIndex = int(item)
    print(largestVertexIndex)
        #Now will create the degrees array:
    #degrees = [0] * (largestVertexIndex + 1)
    #with open(FileName, 'r') as file:
    #    while True:
    #        lines = file.readlines(chunk_size)
    #        #print('A new chunk:')
    #        if not lines:
    #             break
    #        for line in lines:
    #            items = line.split()
    #            for item in items:
    #                degrees[int(item)] += 1
    print('Exit readfile')
    return (largestVertexIndex + 1)

def UpdateDenseSubGraph(currentS, currentRho, currenInsetDegrees, optimalVertices, epsilon):
    # Go through the edges and calculate rho(S)
    # Do a second sweep, and add or remove nodes from S
    numInsetEdges = 0
    if currentRho == 0 :
        currenInsetDegrees = [0] * len(currentS)
        currentRead = 0;
        startTime = time.time()
        with open(inputFilePath, 'r') as file:
            while True:
                lines = file.readlines(chunk_size)
                currentRead += 1
                if ( currentRead % 1000 == 0):
                    print('Currently read in MB:', currentRead * chunk_size/ (1000000))
                    print("{0:.4f}".format(time.time() - startTime))
                if not lines:
                     break
                for line in lines:
                    items = line.split()
                    if (int(items[0]) in currentS) and (int(items[1]) in currentS):
                        numInsetEdges += 1
                        currenInsetDegrees[currentS.index(int(items[0]))] += 1
                        currenInsetDegrees[currentS.index(int(items[1]))] += 1

            currentRho = numInsetEdges / len(currentS)
    # now go through the vertices in S, and only pick the ones with degrees
    # higher than threshold:

    threshold = 2 * currentRho * (1 + epsilon)
    newS = []
    with open(inputFilePath, 'r') as file:
        print('File read #1:')
        while True:
            lines = file.readlines(chunk_size)
            if not lines:
                    break
            for line in lines:
                items = line.split()
                for item in items :
                    if int(item) not in currentS :
                        continue
                    if currenInsetDegrees[currentS.index(int(item))] > threshold and int(item) not in newS: 
                        newS.append(int(item))

    # need to read the file again and find the rho of newS:
    newSetnumInsetEdges = 0
    optimalSetnumInsetEdges = 0
    newSinsetDegrees = [0] * len(newS);

    with open(inputFilePath, 'r') as file:
        print('File read #2:')
        while True:
            lines = file.readlines(chunk_size)
            if not lines:
                 break
            for line in lines:
                items = line.split()
                if (int(items[0]) in newS) and (int(items[1]) in newS):
                    newSetnumInsetEdges += 1
                    newSinsetDegrees[newS.index(int(items[0]))] += 1
                    newSinsetDegrees[newS.index(int(items[1]))] += 1

                if (int(items[0]) in optimalVertices) and (int(items[1]) in optimalVertices):
                    optimalSetnumInsetEdges += 1

        newSrho = newSetnumInsetEdges / len(newS) if (len(newS) > 0 ) else 0
        optimalrho = optimalSetnumInsetEdges / len(optimalVertices) if (len(optimalVertices) > 0 ) else 0

        if newSrho > optimalrho:
            optimalVertices = newS.copy()

    return (newS, newSrho, newSinsetDegrees, optimalVertices)

numVertices = readFileAndInitializeDegrees(inputFilePath, chunk_size)


epsilons = [.1, .5, 1, 2]
for epsilon in epsilons :
    startTime = time.time()
    rho = 0;
    insetDegrees = []
    iteration = 0;
    subGraphVertices = [x for x in range(numVertices)]
    optimalVertices = [x for x in range(numVertices)]

    while (len(subGraphVertices) > 0) :
        iteration += 1
        print("iteration: {0} numRemainedVertices:{1} currentRunTime: {2:.4f} ".format(iteration, len(subGraphVertices), time.time() - startTime))
        (subGraphVertices, rho, insetDegrees, optimalVertices) = UpdateDenseSubGraph(subGraphVertices, rho, insetDegrees, optimalVertices, epsilon)
    print("epsilon:{0} numIterations:{1} runTime:{2:.4f}".format(epsilon, iteration, time.time() - startTime))
    print("****************************************************")
    #pprint.pprint(optimalVertices)
