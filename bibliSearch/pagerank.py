class PageRank():

    def firstVectorRank(self, size):
        vector = [None] * size
        for i in range(0, size):
            vector[i] = 1.0/size
        return vector


    def initVectorRank(self, size):
        return [0] * size

    def sumLinks(self, matrixOfLinks):
        sum = [0] * len(matrixOfLinks)
        for i in range(0, len(matrixOfLinks)):
            for j in range(0, len(matrixOfLinks)):
                if(matrixOfLinks[i][j]):
                    sum[i] += 1
        return sum


    def pageRankDumpingFactor(self, ranks, links,
                              nbIte, alpha):
        for i in range(0, nbIte):
            ranks = self.rankingDumpingFactor(ranks, links, alpha)
        
        return ranks


    def rankingDumpingFactor(self, ranks, links, alpha):
        realRanks = self.ranking(ranks, links)
        
        for i in range(0, len(realRanks)):
            realRanks[i] += (1 - alpha) * alpha * realRanks[i]

        return realRanks


    def ranking(self, ranks, links):

        ranking = self.initVectorRank(len(ranks))
        sum = self.sumLinks(links)

        #pour chaque page, calcul du pagerank
        for i in range(0, len(ranks)):
            for j in range(0, len(links[0])):
                if(i!=j and links[i][j] == 1):
                    if(sum[i] > 0):
                        ranking[j] += ranks[i] / sum[i]

        for i in range(0, len(ranking)):
            if(ranking[i] == 0):
                ranking[i] = ranks[i]

        return ranking