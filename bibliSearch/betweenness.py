import os
import bibliSearch.models
import re
import networkx as nx
from queue import Queue
import collections

class Betweenness():

    def calculateJaccardDistance(self, m1, m2):
        numerator = 0
        denominator = 0

        for word in m1:
            k1 = m1[word]
            k2 = m2[word]
            Max = max(k1,k2)
            Min = min(k1,k2)
            numerator = numerator + (Max - Min)
            denominator = denominator + Max

        for word in m2:
            if word not in m1:
                k1 = m1[word]
                k2 = m2[word]
                Max = max(k1,k2)
                Min = min(k1,k2)
                numerator = numerator + (Max - Min)
                denominator = denominator + Max

        return (numerator/denominator)

    def createGraph(self, allFiles, threshold):
        collectionsList = list()
        for f in allFiles:
            with open(f,'r',encoding='UTF-8') as file1:
                str1=re.split('[^a-zA-Z]', file1.read())
            collectionsList.append(collections.Counter(str1))

        G = nx.Graph()
        
        for i in range(len(allFiles)):
            for j in range(len(allFiles))[i+1:]:
                if calculateJaccardDistance(collectionsList[i],collectionsList[j]) >= threshold :
                    G.add_edge(allFiles[i], allFiles[j])
        
        return G

    def BC(self, threshold, allFiles):

        G = self.createGraph(allFiles, threshold)

        CB = dict.fromkeys(G,0.0)
        for s in G.nodes():
            Pred = {w:[] for  w in G.nodes()}
            dist = dict.fromkeys(G,None)
            sigma = dict.fromkeys(G,0.0)
            dist[s] = 0
            sigma[s] = 1
            Q = Queue()
            Q.put(s)
            S = []
            while not Q.empty():
                v = Q.get()
                S.append(v)
                for w in G.neighbors(v):
                    if dist[w] == None:
                        dist[w] = dist[v] + 1
                        Q.put(w)
                    if dist[w] == dist[v] + 1:
                        sigma[w] += sigma[v]
                        Pred[w].append(v)
            delta = dict.fromkeys(G,0.0)
            for w in S[::-1]:
                for v in Pred[w]:
                    delta[v] += sigma[v]/sigma[w]*(1+delta[w])
                if w != s:
                    CB[w] += delta[w]
        max = 0
        for v in CB:
            CB[v] /= 2.0
            if CB[v]>max:
                max = CB[v]
        for v in CB:
            CB[v] /= max
        
        l = sorted(CB.items(), key=lambda d: d[1], reverse=True)
        return [i[0] for i in l]


