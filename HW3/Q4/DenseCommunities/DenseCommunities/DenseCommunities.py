import pprint 
import time

inputFilePath = 'livejournal-undirected.txt'
#inputFilePath = 'sanitySmall.txt'

chunk_size = 20000

def readFileAndInitializeDegrees(FileName, chunk_size):
    largestVertexIndex = 0
    currentRead = 0;
    startTime = time.time()
    initialDict = dict()
    with open(FileName, 'r') as file:
        while True:
            lines = file.readlines(chunk_size)
            currentRead += 1
            #if ( currentRead % 1000 == 0):
            #    print('Currently read in MB:', currentRead * chunk_size/ (1000000))
            #    print("{0:.4f}".format(time.time() - startTime))
            #print('A new chunk:')
            if not lines:
                 break
            for line in lines:
                items = line.split()
                for item in items:
                    if (largestVertexIndex < int(item)):
                        largestVertexIndex = int(item)
                    if item not in initialDict:
                        initialDict[item] = 0
    print(len(initialDict))
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
    return initialDict

def UpdateDenseSubGraph(currentRho, currentInsetDegreesDic, optimalVertices,optimalRho, epsilon, firstRun):
    # Go through the edges and calculate rho(S)
    # Do a second sweep, and add or remove nodes from S
    numInsetEdges = 0
    if firstRun : 
        currentRead = 0;
        startTime = time.time()
        with open(inputFilePath, 'r') as file:
            while True:
                lines = file.readlines(chunk_size)
                currentRead += 1

                if not lines:
                     break
                for line in lines:
                    items = line.split()
                    
                    if(items[0] in currentInsetDegreesDic) and (items[1] in currentInsetDegreesDic):
                        currentInsetDegreesDic[items[0]] += 1
                        currentInsetDegreesDic[items[1]] += 1
                        numInsetEdges += 1
                            

            currentRho = numInsetEdges / (len(currentInsetDegreesDic))
            print('Initial Rho:{0:.3f} InitialInsetEdges:{1}'.format(currentRho, sum(iter(currentInsetDegreesDic.values())) / 2))
            optimalRho = currentRho
    # now go through the vertices in S, and only pick the ones with degrees
    # higher than threshold:

    threshold = 2 * currentRho * (1 + epsilon)
    newSinsetDegreesDict = dict();
    with open(inputFilePath, 'r') as file:
        #print('File read #1:')
        while True:
            lines = file.readlines(chunk_size)
            if not lines:
                    break
            for line in lines:
                items = line.split()
                for item in items :
                    if item not in currentInsetDegreesDic :
                        continue
                    if currentInsetDegreesDic[item] > threshold and int(item) not in newSinsetDegreesDict: 
                        newSinsetDegreesDict[item] = 0

    # need to read the file again and find the rho of newS:
    newSetnumInsetEdges = 0
    optimalSetnumInsetEdges = 0

    with open(inputFilePath, 'r') as file:
        #print('File read #2:')
        while True:
            lines = file.readlines(chunk_size)
            if not lines:
                 break
            for line in lines:
                items = line.split()
                if (items[0] in newSinsetDegreesDict) and (items[1] in newSinsetDegreesDict):
                    newSetnumInsetEdges += 1
                    newSinsetDegreesDict[items[0]] += 1
                    newSinsetDegreesDict[items[1]] += 1
                   
        newSrho = newSetnumInsetEdges / len(newSinsetDegreesDict) if (len(newSinsetDegreesDict) > 0 ) else 0

        if newSrho > optimalRho:
            optimalRho = newSrho
            optimalVertices = list(newSinsetDegreesDict.keys())

    return (newSrho, newSinsetDegreesDict, optimalVertices, optimalRho)

initialDict = readFileAndInitializeDegrees(inputFilePath, chunk_size)
optimalVertices = []

#epsilons = [.1, .5, 1, 2]
epsilons = [.05]
for communities in range(1,21):
    print('Number of communities:{0}'.format(communities))
    rho = 0
    optimalRho = 0      
    iteration = 0
    for entry in list(optimalVertices):
        del initialDict[entry]

    optimalVertices = []

    for epsilon in epsilons :
        startTime = time.time()
        optimalVertices = list(initialDict.keys())
        insetDegreesDic = initialDict.copy()
        firstRun = True;
        while (len(insetDegreesDic) > 0) :
            iteration += 1
            print("iteration: {0} numRemainedVertices:{1}, Rho:{2:.3f}, InSetEdges:{3}  currentRunTime: {4:.3f} "
                  .format(iteration, len(insetDegreesDic), rho, sum(iter(insetDegreesDic.values())) / 2, time.time() - startTime))
            (rho, insetDegreesDic, optimalVertices, optimalRho) = UpdateDenseSubGraph(rho,
                                                                                      insetDegreesDic,
                                                                                      optimalVertices,
                                                                                      optimalRho,
                                                                                      epsilon,
                                                                                      firstRun
                                                                                      )
            firstRun = False;
        print("epsilon:{0} numIterations:{1} runTime:{2:.4f}".format(epsilon, iteration, time.time() - startTime))
        #pprint.pprint(optimalVertices)
        print("****************************************************")
    print('optimal community size:{0} optimal Rho:{1}'.format(len(optimalVertices), optimalRho))
    print("########################################################")
