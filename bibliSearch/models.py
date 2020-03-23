from django.db import models
import os
from .parser import Parser
from .indexing import Indexing
from .kmp import KMP
from .regex import Regex
from .betweenness import Betweenness

class Book(models.Model):

    def __init__(self, filepath) :
        self.bookFile = open(filepath, "r")
        self.nameFile = os.path.basename(self.bookFile.name)
        self.author = ""
        self.title = ""
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
        return self.books        

    def getFilteredBooksRegexp(self, pattern, folder_path) :
        regex = Regex()
        result = regex.recherche(pattern, folder_path)
        betweenness = Betweenness()
        return betweenness.classement(0.75, result)
        

    def getFilteredBooksKMP(self, pattern, folder_path) :
        kmp = KMP()
        result = kmp.recherche(pattern, folder_path)
        betweenness = Betweenness()
        return betweenness.classement(0.75, result)

    def getFilteredBooksIndex(self, pattern, folder_path) :
        indexing = Indexing() 
        result = indexing.recherche(pattern, folder_path)
        betweenness = Betweenness()
        return betweenness.classement(0.75, result)
        
