
#import time
#import concurrent.futures
#executors = concurrent.futures.ThreadPoolExecutor(5)
#def parallelTest(index):
#    print('index:{0}\n'.format(index))
#    time.sleep(5)
#    print('after sleep index:{0}\n'.format(index))


##for i in range(5):
#futures = [executors.submit(parallelTest,x) for x in range(5)]
#concurrent.futures.wait(futures)
##while (False == result.):
##    continue
#print('done')



