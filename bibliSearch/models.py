from django.db import models
import os
from .parser import Parser
from .indexing import Indexing
from .kmp import KMP
from .regex import Regex
from .betweenness import Betweenness
from .pagerank import PageRank
from .suggestion import Suggestion
from .classement import Classement

class Book(models.Model):

    def __init__(self, filepath) :
        self.bookFile = open(filepath, mode="r", encoding="UTF-8")
        self.nameFile = os.path.basename(self.bookFile.name)
        self.author = "Unknown"
        self.title = "No title"
        self.postingDate = ""
        self.releaseDate = ""
        self.language = ""
        self.parseMetadata()
        #self.bookFile.close()
        
    def getFile(self) :
        return self.bookFile

    def getNameFile(self) :
        return self.nameFile

    def getTitle(self) :
        return self.title
    
    def getAuthor(self) :
        return self.author

    def getPostingDate(self) :
        return self.postingDate

    def getReleaseDate(self) :
        return self.releaseDate

    def getLanguage(self) :
        return self.language


    def setNameFile(self, nameFile) :
        self.nameFile = nameFile

    def setTitle(self, title) :
        self.title = title

    def setAuthor(self, author) :
        self.author = author

    def setPostingDate(self, postingDate) :
        self.postingDate = postingDate

    def setReleaseDate(self, releaseDate) :
        self.releaseDate = releaseDate

    def setLanguage(self, language) :
        self.language = language 


    def parseMetadata(self) :
        parser = Parser()
        parser.parseMetadata(self) 

class Library(models.Model):

    books = []

    def __init__(self, folderPath) :
        for filename in os.listdir(folderPath):
            self.books.append(Book(folderPath+"/"+filename))

    def getBooks(self) :
        """
        pagerank = PageRank()
        
        matrix = [[False, False, False, False],
                  [True, False, True, False],
                  [True, False, False, False],
                  [True, True, True, False]]

        vectorInit = pagerank.firstVectorRank(len(matrix))

        print(pagerank.pageRankDumpingFactor(vectorInit, matrix, 20, 0.85))
        """
        return self.books        


    def getFilteredSuggestions(self, bookPath):
        suggestion = Suggestion()
        result = suggestion.neighborsList(bookPath)
        return result
        

    def getFilteredBooksRegexp(self, pattern, folder_path, betweenness, pagerank, mix) :
        regex = Regex()
        result = regex.recherche(pattern, folder_path)
#        if(not betweenness and not pagerank and not mix):
#            return result
#        else:
        classement = Classement()
        return classement.sortBooks(result, betweenness, pagerank, mix)
    

    def getFilteredBooksUnion(self, pattern, folder_path, betweenness, pagerank, mix) :
        l = pattern.split()
        result = []
        indexing = Indexing() 
        for p in l:
            tmp = indexing.recherche(p.lower(), folder_path)
            for t in tmp:
                if t not in result:
                    result.append(t)

        classement = Classement()
        return classement.sortBooks(result, betweenness, pagerank, mix)

    def getFilteredBooksKMP(self, pattern, folder_path, betweenness, pagerank, mix) :
        kmp = KMP()
        result = kmp.recherche(pattern, folder_path)
#        if(not betweenness and not pagerank and not mix):
#            return result
#        else:
        classement = Classement()
        return classement.sortBooks(result, betweenness, pagerank, mix)

    def getFilteredBooksIndex(self, pattern, folder_path, betweenness, pagerank, mix) :
        indexing = Indexing() 
        result = indexing.recherche(pattern.lower(), folder_path)
#        if(not betweenness and not pagerank and not mix):
#            print("NO INDEXCENTRALITY")
#            print("ICII ",result)
#            return result
#        else:
        classement = Classement()
#            print("YES INDEXCENTRALITY")
        return classement.sortBooks(result, betweenness, pagerank, mix)
#            print("ICII ",result)
#            return result


    def getFilteredBooksByTitle(self, pattern, books):
        result = []
        for book in books:
            if pattern.lower() in book.getTitle().lower():
                result.append(book)
            
        return result


    def getFilteredBooksByAuthor(self, pattern, books):
        result = []
        for book in books:            
            if pattern.lower() in book.getAuthor().lower():
                result.append(book)
            
        return result


    def getFilteredBooksByReleaseDate(self, pattern, books):
        result = []
        for book in books:
            if pattern.lower() in book.getReleaseDate().lower():
                result.append(book)
            
        return result


    def getFilteredBooksByPostingDate(self, pattern, books):
        result = []
        for book in books:
            if pattern.lower() in book.getPostingDate().lower():
                result.append(book)
            
        return result


    def getFilteredBooksByLanguage(self, pattern, books):
        result = []
        for book in books:
            if pattern.lower() in book.getLanguage().lower():
                result.append(book)
            
        return result
        
