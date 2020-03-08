from django.db import models

class Book(models.Model):

    def __init__(self, nameFile, title,
                 author, postingDate, releaseDate, language) :

        self.nameFile = nameFile
        self.title = title
        self.author = author 
        self.postingDate = postingDate 
        self.releaseDate = releaseDate
        self.language = language
    
    def print(self) :

        return self.nameFile
