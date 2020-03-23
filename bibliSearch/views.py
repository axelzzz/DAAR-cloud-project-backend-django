from django.shortcuts import get_object_or_404, render
from django.http import HttpResponse, JsonResponse
from rest_framework.renderers import JSONRenderer

from .models import Book, Library
from .serializers import BookSerializer

import json
import os

database_path = "bibliSearch/static/database1500"


library = Library(database_path)
books = library.getBooks()

def index(request):
    return render(request, 'bibliSearch/index.html')


def getBooks(request):
    booksSerializer = BookSerializer(books, many=True)
    return HttpResponse(JSONRenderer().render(booksSerializer.data) )        
    
def filter(request):
    pattern = request.GET['pattern']
   
    if('*' in pattern or '.' in pattern or '|' in pattern or '(' in pattern or ')' in pattern) :
        filteredBooks = library.getFilteredBooksRegexp(pattern, database_path)

    elif( len(pattern.split()) > 1 ) :
        filteredBooks = library.getFilteredBooksKMP(pattern, database_path)    
        
    else :
        filteredBooks = library.getFilteredBooksIndex(pattern, database_path)  
            
    booksSerializer = BookSerializer(filteredBooks, many=True)    
    return HttpResponse(JSONRenderer().render(booksSerializer.data) )              
