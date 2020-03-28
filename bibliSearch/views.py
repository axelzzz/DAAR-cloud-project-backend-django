from django.shortcuts import get_object_or_404, render
from django.http import HttpResponse, JsonResponse
from rest_framework.renderers import JSONRenderer

from .models import Book, Library
from .serializers import BookSerializer

import json
import os

#database_path = "bibliSearch/static/database1500"
database_path = "bibliSearch/static/PPPP1664"

library = Library(database_path)
books = library.getBooks()

def index(request):
    return render(request, 'bibliSearch/index.html')


def getBooks(request):
    booksSerializer = BookSerializer(books, many=True)
    return HttpResponse(JSONRenderer().render(booksSerializer.data) )        
    
def getSuggestions(request):
    fileName = request.GET['nameFile']
    bookPath = database_path + fileName
    print ("AAAAAAAAAAAAAAAAAAAAA")
    bookSuggestions = library.getFilteredSuggestions(bookPath)

    booksSerializer = BookSerializer(bookSuggestions, many=True)
    return HttpResponse(JSONRenderer().render(booksSerializer.data) ) 
    

def filter(request):

    filteredBooks = []

    pattern = request.GET['pattern']
    isSearchByTitle = request.GET['isSearchByTitle']
    isSearchByAuthor = request.GET['isSearchByAuthor']
    isSearchByReleaseDate = request.GET['isSearchByReleaseDate']
    isSearchByPostingDate = request.GET['isSearchByPostingDate']
    isSearchByLanguage = request.GET['isSearchByLanguage']

    if(isSearchByTitle == "false"):
        isSearchByTitle = False
    else:
        isSearchByTitle = True

    if(isSearchByAuthor == "false"):
        isSearchByAuthor = False
    else:
        isSearchByAuthor = True

    if(isSearchByReleaseDate == "false"):
        isSearchByReleaseDate = False
    else:
        isSearchByReleaseDate = True

    if(isSearchByPostingDate == "false"):
        isSearchByPostingDate = False
    else:
        isSearchByPostingDate = True

    if(isSearchByLanguage == "false"):
        isSearchByLanguage = False
    else:
        isSearchByLanguage = True


    if(isSearchByTitle or isSearchByAuthor or isSearchByPostingDate or isSearchByReleaseDate or isSearchByLanguage):     
        patterns = pattern.split(" ")

        for pattern in patterns:          
            if(isSearchByTitle) :
                filteredBooks.extend(library.getFilteredBooksByTitle(pattern, books))
                    
            if(isSearchByAuthor) :
                filteredBooks.extend(library.getFilteredBooksByAuthor(pattern, books))
                    
            if(isSearchByPostingDate) :
                filteredBooks.extend(library.getFilteredBooksByPostingDate(pattern, books))
                    
            if(isSearchByReleaseDate) :
                filteredBooks.extend(library.getFilteredBooksByReleaseDate(pattern, books))
                    
            if(isSearchByLanguage) :
                filteredBooks.extend(library.getFilteredBooksByLanguage(pattern, books))  
           
    elif('*' in pattern or '.' in pattern or '|' in pattern or '(' in pattern or ')' in pattern) :
        filteredBooks = library.getFilteredBooksRegexp(pattern, database_path)

    elif( len(pattern.split()) > 1 ) :
        filteredBooks = library.getFilteredBooksKMP(pattern, database_path)    
        
    else :
        filteredBooks = library.getFilteredBooksIndex(pattern, database_path)  
            
    booksSerializer = BookSerializer(filteredBooks, many=True)    
    return HttpResponse(JSONRenderer().render(booksSerializer.data) )              
