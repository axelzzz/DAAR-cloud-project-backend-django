import os
import bibliSearch.models
import re

class Classement():

    def sortBooks(self, books):
        indexBC = []
        fileIndexBCPath = "bibliSearch/static/indice_BC.txt"

        with open(fileIndexBCPath,'r',encoding='UTF-8') as file:
            str1=re.split(';', file.read())

        for book in books:
            for l in (str1):
                livre_bc = l.split(',')
                if (livre_bc[0]==book):
                    indexBC.append(float(livre_bc[1]))
                    break

        """
        indexPR = []
        fileIndexPRPath = "bibliSearch/static/indice_PR.txt"
       
        with open(fileIndexPRPath,'r',encoding='UTF-8') as file:
            str1=re.split(';', file.read())

        for book in books:
            for l in (str1):
                livre_bc = l.split(',')
                if (livre_bc[0]==book):
                    indexPR.append(float(livre_bc[1]))
                    break
        """

        tmp = []
        for i in range(len(books)):
            #tmp.append((books[i],indexBC[i]+indexPR[i]))
            tmp.append((books[i],indexBC[i]))
        
        booksSorded = sorted(tmp, key=lambda d: d[1], reverse=True)
        
        print ([i[0] for i in booksSorded])

