import os
import bibliSearch.models
import re

class Suggestion():

    def neighborsList(self, bookPath):
        bookPath = "PPPP1664/"+bookPath
        fileIndexBCPath = "bibliSearch/static/indice_BC.txt"
        result = []

        with open(fileIndexBCPath,'r',encoding='UTF-8') as file:
            str1=re.split(';', file.read())
            for i,l in enumerate(str1):
                livre_bc = l.split(',')
                if (livre_bc[0]==bookPath):
                    if (i==0):
                        result.append(str1[0].split(',')[0])
                        result.append(str1[1].split(',')[0])
                        result.append(str1[2].split(',')[0])
                    elif (i==1663):
                        result.append(str1[1661].split(',')[0])
                        result.append(str1[1662].split(',')[0])
                        result.append(str1[1663].split(',')[0])
                    else:
                        result.append(str1[i-1].split(',')[0])
                        result.append(str1[i].split(',')[0])
                        result.append(str1[i+1].split(',')[0])  

        result_objet = []
        for b in result:
            result_objet.append(bibliSearch.models.Book("bibliSearch/static/"+b))
            
        return result_objet


