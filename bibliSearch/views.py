from django.shortcuts import get_object_or_404, render
from django.http import HttpResponse, JsonResponse
from rest_framework.renderers import JSONRenderer

from py4j.java_gateway import JavaGateway

from .models import Book
from .serializers import BookSerializer

import json
import py4j

def index(request):
    return render(request, 'bibliSearch/index.html')


def getBooks(request):
    gateway = JavaGateway()
    library = gateway.entry_point.getLibrary()
    books = library.getBooks()
    jsonBooks = []

    for i in range (books.size()):

        b = Book(
          nameFile=gateway.jvm.String(books[i].getNameFile()),
          title=gateway.jvm.String(books[i].getTitle()) ,
          author=gateway.jvm.String(books[i].getAuthor()) ,
          postingDate=gateway.jvm.String(books[i].getPostingDate()) ,
          releaseDate=gateway.jvm.String(books[i].getReleaseDate()) ,
          language=gateway.jvm.String(books[i].getLanguage())
        )        
        
        jsonBooks.append(b)      
    
    booksSerializer = BookSerializer(jsonBooks, many=True)
    
    return HttpResponse(JSONRenderer().render(booksSerializer.data) )        
    

#def searchPattern(request):
    #return render(request, 'bibliSearch/index.html')
    #return HttpResponse("you searched %s" %request.GET.get('book_title', ' hum '))
    #json_books = {"books" : [request.GET.get('book_title'), "bible"]}
    #return JsonResponse(json_books)
    #return HttpResponse(json.dumps(json_books), content_type="application/json")
    #return HttpResponse("il y a %i livres" %getNbLivres())
    #return HttpResponse(getLivres())