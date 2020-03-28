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
        self.bookFile = open(filepath, "r")
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
        

    def getFilteredBooksRegexp(self, pattern, folder_path) :
        regex = Regex()
        result = regex.recherche(pattern, folder_path)
        #return result
        classement = Classement()
        return classement.sortBooks(result)
    

    def getFilteredBooksKMP(self, pattern, folder_path) :
        kmp = KMP()
        result = kmp.recherche(pattern, folder_path)
        return result

    def getFilteredBooksIndex(self, pattern, folder_path) :
        indexing = Indexing() 
        result = indexing.recherche(pattern.lower(), folder_path)
        return result


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
        
