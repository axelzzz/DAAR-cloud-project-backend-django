from django.shortcuts import get_object_or_404, render
from django.http import HttpResponse, JsonResponse
from rest_framework.renderers import JSONRenderer

from py4j.java_gateway import JavaGateway, GatewayParameters

from .models import Book, Library
from .serializers import BookSerializer

import json
import py4j
import os

database_path = "bibliSearch/static/database1664"
database_index_path = ""


library = Library(database_path)
books = library.getBooks()

def index(request):
    return render(request, 'bibliSearch/index.html')


def getBooks(request):

    booksSerializer = BookSerializer(books, many=True)
    
    return HttpResponse(JSONRenderer().render(booksSerializer.data) )        
    
def filter(request):
    pattern = request.GET['pattern']
   
    gateway = JavaGateway()
    library = gateway.entry_point.getLibrary()
    
    if('*' in pattern or '.' in pattern or '|' in pattern or '(' in pattern or ')' in pattern) :
        filteredBooks = library.getFilteredBooksRegexp(pattern)

    elif( len(pattern.split()) > 1 ) :
        filteredBooks = library.getFilteredBooksKMP(pattern)    
        
    else :
        filteredBooks = library.getFilteredBooksIndex(pattern)  
        
    jsonBooks = []

    for i in range (filteredBooks.size()):

        b = Book(
          nameFile=gateway.jvm.String(filteredBooks[i].getNameFile()),
          title=gateway.jvm.String(filteredBooks[i].getTitle()) ,
          author=gateway.jvm.String(filteredBooks[i].getAuthor()) ,
          postingDate=gateway.jvm.String(filteredBooks[i].getPostingDate()) ,
          releaseDate=gateway.jvm.String(filteredBooks[i].getReleaseDate()) ,
          language=gateway.jvm.String(filteredBooks[i].getLanguage())
        )        
        
        jsonBooks.append(b)      
    
    booksSerializer = BookSerializer(jsonBooks, many=True)
    
    return HttpResponse(JSONRenderer().render(booksSerializer.data) )              
